#!/usr/bin/env bash
# Smoke check for agent-timing-report.sh (Task 8 Step 2, speed-up T1).
# Feeds synthetic timing logs through the real script and asserts the
# table math plus every guard: orphan-stop loud failure, empty/missing
# log assert, latest-unmatched pairing across resume cycles, sub-second
# launch-ack flagging, agent_type join-mismatch loud failure, and
# pre-amendment (no caller_id) records excluded from cross-family joins.
# Wired to the SessionStart hook in .claude/settings.json alongside
# agent-timing-smoke.sh.
#
# Usage: agent-timing-report-smoke.sh   (exit 0 = pass, non-zero = fail)
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
report="$script_dir/agent-timing-report.sh"
tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT

T() { printf '2026-07-16T10:%02d:%02d.%03dZ' $(($1 / 60)) $(($1 % 60)) "${2:-0}"; }

rec() { # offset_s event agent_id tool_use_id agent_type desc [caller] [ms]
  local ts caller=''
  ts="$(T "$1" "${8:-0}")"
  [ $# -lt 7 ] || caller=",\"caller_id\":\"$7\""
  printf '{"ts":"%s","event":"%s","session_id":"smoke","tool_use_id":"%s","agent_id":"%s"%s,"agent_type":"%s","description":"%s"}\n' \
    "$ts" "$2" "$4" "$3" "$caller" "$5" "$6"
}

expect_in() { # name haystack needle
  case "$2" in *"$3"*) ;; *) echo "FAIL $1: missing [$3] in output:"; echo "$2"; exit 1;; esac
}
expect_not_in() {
  case "$2" in *"$3"*) echo "FAIL $1: unexpected [$3] in output:"; echo "$2"; exit 1;; esac
}
expect_fail() { # name log needle — report must exit non-zero and print needle
  if out="$(bash "$report" "$2" 2>&1)"; then
    echo "FAIL $1: expected non-zero exit"; exit 1
  fi
  expect_in "$1" "$out" "$3"
}

# --- happy path: solo step, then a 3-agent overlapping batch ----------
log="$tmp/happy.jsonl"
{ rec 0   subagent_start sub-red '' red-agent ''
  rec 0   dispatch '' tu_r red-agent 'red scenario' main
  rec 0   return sub-red tu_r red-agent 'red scenario' main 20
  rec 100 subagent_stop sub-red '' red-agent ''
  rec 200 subagent_start sub-ref '' refactor-agent ''
  rec 206 subagent_start sub-rev '' agent-review-agent ''
  rec 212 subagent_start sub-pre '' premortem-agent ''
  rec 288 subagent_stop sub-ref '' refactor-agent ''
  rec 353 subagent_stop sub-rev '' agent-review-agent ''
  rec 377 subagent_stop sub-pre '' premortem-agent ''
} > "$log"
out="$(bash "$report" "$log")"
expect_in happy-solo-step "$out" 'wall=100s  busy=100s  overlap_saved=0s  serial_tail=0s'
expect_in happy-batch-math "$out" 'wall=177s  busy=400s  overlap_saved=223s  serial_tail=12s'
expect_in happy-joined-label "$out" 'red-agent  100s  red scenario'
expect_in happy-launch-ack "$out" '20ms'
echo "PASS happy-path (solo + overlapping batch, label join, launch-ack flag)"

# --- orphan stop must fail loudly --------------------------------------
log="$tmp/orphan.jsonl"
rec 0 subagent_stop ghost '' red-agent '' > "$log"
expect_fail orphan "$log" 'ORPHAN'
echo "PASS orphan-stop (loud failure)"

# --- empty and missing logs must fail ----------------------------------
: > "$tmp/empty.jsonl"
expect_fail empty-log "$tmp/empty.jsonl" 'empty'
expect_fail missing-log "$tmp/does-not-exist.jsonl" 'ERROR'
echo "PASS empty/missing-log asserts"

# --- resume: two start/stop cycles on ONE agent_id, no orphans ----------
log="$tmp/resume.jsonl"
{ rec 0  subagent_start R '' claude ''
  rec 10 subagent_stop  R '' claude ''
  rec 20 subagent_start R '' claude ''
  rec 25 subagent_stop  R '' claude ''
} > "$log"
out="$(bash "$report" "$log")"
expect_in resume-cycle-1 "$out" 'wall=10s'
expect_in resume-cycle-2 "$out" 'wall=5s'
expect_not_in resume "$out" 'ORPHAN'
echo "PASS resume-cycles (latest-unmatched pairing, 2 intervals)"

# --- agent_type mismatch across an agent_id join must fail loudly ------
log="$tmp/mismatch.jsonl"
{ rec 0 subagent_start S1 '' green-agent ''
  rec 5 subagent_stop  S1 '' green-agent ''
  rec 0 dispatch '' tu_m red-agent 'x' main
  rec 6 return S1 tu_m red-agent 'x' main
} > "$log"
expect_fail type-mismatch "$log" 'MISMATCH'
echo "PASS type-mismatch (loud failure on caller-id cross-wiring)"

# --- pre-amendment record (no caller_id): excluded from the join -------
log="$tmp/preamend.jsonl"
{ rec 0 subagent_start S1 '' green-agent ''
  rec 5 subagent_stop  S1 '' green-agent ''
  rec 0 dispatch '' tu_p red-agent 'x'
  rec 6 return S1 tu_p red-agent 'x'
} > "$log"
out="$(bash "$report" "$log")"
expect_not_in preamend-no-join "$out" 'green-agent  5s  x'
expect_not_in preamend-no-mismatch "$out" 'MISMATCH'
echo "PASS pre-amendment schema (no caller_id -> excluded from join)"

# --- unclosed start: warn, but do not fail ------------------------------
log="$tmp/unclosed.jsonl"
rec 0 subagent_start U '' claude '' > "$log"
out="$(bash "$report" "$log")"
expect_in unclosed "$out" 'WARN unclosed subagent_start'
echo "PASS unclosed-start (warn only)"

# --- real-shaped untracked stop (typeless, dispatch-less, start hook
# --- provably active): reported + excluded, table still produced --------
log="$tmp/untracked.jsonl"
{ rec 0  subagent_start W '' claude ''
  rec 30 subagent_stop  ghost2 '' '' ''
  rec 60 subagent_stop  W '' claude ''
} > "$log"
out="$(bash "$report" "$log")"
expect_in untracked-listed "$out" 'Untracked stops'
expect_in untracked-table-still "$out" 'wall=60s'
echo "PASS untracked-stop (excluded + reported, not fatal)"

# --- empty-agent_id lifecycle records: loud failure (would cross-pair) --
log="$tmp/emptyid.jsonl"
{ rec 0  subagent_start '' '' red-agent ''
  rec 10 subagent_start '' '' green-agent ''
  rec 20 subagent_stop  '' '' red-agent ''
  rec 30 subagent_stop  '' '' green-agent ''
} > "$log"
expect_fail empty-id "$log" 'empty agent_id'
echo "PASS empty-id lifecycle (loud failure, no silent cross-pairing)"

# --- agent_type contradiction INSIDE a start/stop pair: loud failure ----
log="$tmp/pairmismatch.jsonl"
{ rec 0  subagent_start P1 '' red-agent ''
  rec 60 subagent_stop  P1 '' green-agent ''
} > "$log"
expect_fail pair-mismatch "$log" 'contradiction'
echo "PASS pair-type-mismatch (loud failure inside the pair)"

# --- partial batch: 3 started, 1 unclosed -> step row marked PARTIAL ----
log="$tmp/partial.jsonl"
{ rec 200 subagent_start sub-ref '' refactor-agent ''
  rec 206 subagent_start sub-rev '' agent-review-agent ''
  rec 212 subagent_start sub-pre '' premortem-agent ''
  rec 288 subagent_stop sub-ref '' refactor-agent ''
  rec 353 subagent_stop sub-rev '' agent-review-agent ''
} > "$log"
out="$(bash "$report" "$log")"
expect_in partial-marker "$out" 'PARTIAL(1 unclosed member'
expect_in partial-warn "$out" 'WARN unclosed'
echo "PASS partial-batch (unclosed member marks the step row PARTIAL)"

echo "SMOKE OK"
