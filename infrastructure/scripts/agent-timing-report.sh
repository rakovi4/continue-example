#!/usr/bin/env bash
# agent-timing-report.sh — fold a session's agent timing log into the
# per-step time-budget table (Task 8 Step 2, speed-up T1 / checklist C2).
# A "step" is a maximal set of time-overlapping subagent runs — one
# dispatch batch. Per step: wall-clock, busy (sum of member durations),
# overlap_saved (busy - wall), serial_tail (wall minus the longest
# member — the batch time NOT hidden under the longest agent). A step
# with a still-running member is marked PARTIAL on its own row so the
# under-count travels with a pasted table.
#
# Durations come ONLY from subagent_start/stop pairs; dispatch->return
# deltas stamp background-launch acknowledgment and are flagged, never
# summed. Fails loudly (exit 1) on: orphan stops of TRACKED agents,
# empty-agent_id lifecycle records, and agent_type contradictions on
# either join kind. Stop-without-start from spawn paths the start hook
# never saw is routine real-log noise — reported as untracked, not
# fatal. Guard rationale in the .jq header.
#
# Usage: agent-timing-report.sh [timing-log.jsonl]
#   default log: newest infrastructure/timings/agent-timing-*.jsonl
set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
filter="$script_dir/agent-timing-report.jq"
project_dir="${CLAUDE_PROJECT_DIR:-$script_dir/../..}"

command -v jq >/dev/null 2>&1 \
  || { echo "ERROR: jq not found in PATH" >&2; exit 1; }

log="${1:-}"
if [ -z "$log" ]; then
  log="$(ls -t "$project_dir"/infrastructure/timings/agent-timing-*.jsonl \
         2>/dev/null | head -1 || true)"
fi
[ -n "$log" ] && [ -f "$log" ] \
  || { echo "ERROR: no timing log found ($project_dir/infrastructure/timings)" >&2
       exit 1; }
[ -s "$log" ] \
  || { echo "ERROR: timing log is empty: $log" >&2; exit 1; }

report="$(jq -s -f "$filter" "$log")"

fail=0
guard() { # jq-length-expr header line-filter
  if [ "$(jq "$1 | length" <<<"$report")" -gt 0 ]; then
    echo "ERROR: $2" >&2
    jq -r "$1[] | $3" <<<"$report" >&2
    fail=1
  fi
}
guard '.unidentified_lifecycle' \
  'lifecycle record(s) with empty agent_id — unpairable; concurrent empty-id agents would silently cross-pair:' \
  '"  UNIDENTIFIED \(.event) ts=\(.ts)"'
guard '.orphan_stops' \
  'orphan subagent_stop for a TRACKED agent (typed, or seen by a start/tool record) — pairing bug or lost start:' \
  '"  ORPHAN stop agent_id=\(.agent_id) type=\(.agent_type // "?") ts=\(.ts)"'
guard '.pair_type_mismatches' \
  'agent_type contradiction inside a start/stop pair (same agent_id):' \
  '"  MISMATCH agent_id=\(.agent_id) start=\(.start_type) stop=\(.stop_type)"'
guard '.type_mismatches' \
  'agent_type MISMATCH across an agent_id join — tool-family and lifecycle records disagree (caller-id cross-wiring):' \
  '"  MISMATCH agent_id=\(.agent_id) tool=\(.tool_family_type) lifecycle=\(.lifecycle_type)"'
[ "$fail" -eq 0 ] || exit 1

jq -r '
  def s: ((. * 10) | round) / 10 | tostring + "s";
  "Agent time-budget — session \(.session_id) (\(.steps | length) step(s))",
  (if (.steps | length) == 0 then
    "WARN: no subagent lifecycle records — nothing to report" else empty end),
  (.steps | to_entries[]
   | "\nStep \(.key + 1): \(.value.start) -> \(.value.stop)   wall=\(.value.wall|s)  busy=\(.value.busy|s)  overlap_saved=\(.value.overlap_saved|s)  serial_tail=\(.value.serial_tail|s)\(if .value.partial > 0 then "   PARTIAL(\(.value.partial) unclosed member(s) — totals under-count)" else "" end)",
     (.value.members[]
      | "  - \(.agent_type)  \(.dur|s)\(if .description != "" then "  \(.description)" else "" end)")),
  (if (.untracked_stops | length) > 0 then
    "\nUntracked stops (spawn paths SubagentStart never recorded — excluded from every step; busy totals under-count them):",
    (.untracked_stops[] | "  - agent_id=\(.agent_id) ts=\(.ts)")
   else empty end),
  (if (.launch_acks | length) > 0 then
    "\nLaunch-ack tool pairs (dispatch->return < 1s — background launch acknowledgment, NOT agent duration):",
    (.launch_acks[] | "  - \(.agent_type)  \(.tool_use_id)  \((.delta * 1000) | round)ms")
   else empty end),
  (if (.unclosed_starts | length) > 0 then
    "\nWARN unclosed subagent_start (still running, or crashed without a stop):",
    (.unclosed_starts[] | "  - agent_id=\(.agent_id) type=\(.agent_type // "?") since \(.ts)")
   else empty end)
' <<<"$report"
