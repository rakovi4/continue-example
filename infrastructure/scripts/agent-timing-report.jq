# agent-timing-report.jq — aggregation core for agent-timing-report.sh
# (Task 8 Step 2, speed-up T1 / checklist C2). Input: slurped array of
# agent-timing JSONL records. Output: one JSON report object.
#
# Durations come ONLY from subagent_start/subagent_stop pairs — the tool
# family stamps launch acknowledgment for background dispatches, not
# completion. Guards (Step 1/1b/2 review passes):
#   - a stop pairs with the LATEST unmatched start of the same agent_id
#     (SendMessage resume emits multiple cycles per id).
#   - a stop with no unmatched start is FATAL only when the agent was
#     visibly tracked (known agent_type, or its id appears in a start or
#     tool-family record) — that is a pairing bug or lost start. Other
#     stop-without-start records are routine: SubagentStop fires for
#     spawn paths SubagentStart never records (Step 2 premortem
#     Incident 1) — they land in untracked_stops, reported not fatal.
#   - lifecycle records with EMPTY agent_id are unpairable — concurrent
#     empty-id agents would silently cross-pair (Step 2 agent-review
#     Finding 1). Reported in unidentified_lifecycle — caller fails.
#   - agent_type is asserted on BOTH kinds of agent_id join: the
#     tool-family label join (type_mismatches) and inside each
#     start/stop pair (pair_type_mismatches, Step 2 agent-review
#     Finding 2) — caller fails loudly on either.
#   - tool records lacking a caller_id key (pre-Step-1b schema, where
#     agent_id was the CALLER's id) are excluded from agent_id joins.
#   - sub-second dispatch->return deltas are flagged in launch_acks.
#   - a step containing an unclosed start (member still running when
#     the log was read) carries partial > 0 so the under-count travels
#     WITH the table row (Step 2 premortem Incident 2).
# A "step" is a maximal set of time-overlapping lifecycle intervals —
# one dispatch batch of a work unit.

def t:
  if test("\\.[0-9]+Z$") then
    capture("^(?<d>.+)\\.(?<ms>[0-9]+)Z$")
    | (.d + "Z" | fromdateiso8601)
      + ((.ms | tonumber) / pow(10; (.ms | length)))
  else fromdateiso8601 end;

def known: . != null and . != "" and . != "unknown";

def pair_lifecycle:
  map(select(.event == "subagent_start" or .event == "subagent_stop"))
  | map(select((.agent_id // "") == "")) as $unidentified
  | map(select((.agent_id // "") != ""))
  | sort_by(.ts)
  | reduce .[] as $r ({open: {}, intervals: [], orphans: [],
                       pair_mismatches: []};
      $r.agent_id as $id
      | if $r.event == "subagent_start" then
          .open[$id] = ((.open[$id] // []) + [$r])
        else
          (.open[$id] // []) as $stk
          | if ($stk | length) == 0 then .orphans += [$r]
            else $stk[-1] as $s
            | .open[$id] = $stk[:-1]
            | (if ($s.agent_type | known) and ($r.agent_type | known)
                  and $s.agent_type != $r.agent_type then
                 .pair_mismatches += [{agent_id: $id,
                                       start_type: $s.agent_type,
                                       stop_type: $r.agent_type}]
               else . end)
            | .intervals += [{
                agent_id: $id,
                agent_type: (if ($s.agent_type | known) then $s.agent_type
                             else ($r.agent_type // "unknown") end),
                start: $s.ts, stop: $r.ts,
                dur: (($r.ts | t) - ($s.ts | t)) }]
            end
        end)
  | { intervals: (.intervals | sort_by(.start)),
      orphans: .orphans,
      pair_mismatches: .pair_mismatches,
      unclosed: [.open[] | .[]],
      unidentified: $unidentified };

def pair_tools:
  map(select((.event == "dispatch" or .event == "return"
              or .event == "return_failure")
             and ((.tool_use_id // "") != "")))
  | group_by(.tool_use_id)
  | map({ tool_use_id: .[0].tool_use_id,
          dispatch: (map(select(.event == "dispatch")) | first),
          ret: (map(select(.event != "dispatch")) | first) })
  | map(select(.dispatch != null and .ret != null)
        | .delta = ((.ret.ts | t) - (.dispatch.ts | t))
        | .launch_ack = (.delta < 1));

def join_labels($intervals):
  reduce (.[] | select(((.ret.agent_id // "") != "")
                       and (.ret | has("caller_id")))) as $p
    ({labels: {}, mismatches: []};
      [$intervals[] | select(.agent_id == $p.ret.agent_id)] as $ms
      | [$ms[] | select((.agent_type | known)
                        and ($p.ret.agent_type | known)
                        and .agent_type != $p.ret.agent_type)] as $bad
      | if ($bad | length) > 0 then
          .mismatches += [{ agent_id: $p.ret.agent_id,
                            tool_family_type: $p.ret.agent_type,
                            lifecycle_type: $bad[0].agent_type }]
        elif ($ms | length) > 0 then
          .labels[$p.ret.agent_id] =
            ($p.dispatch.description // $p.ret.description // "")
        else . end);

def clusters:
  reduce .[] as $iv ([];
    if length == 0 then [[$iv]]
    else (.[-1] | map(.stop | t) | max) as $edge
    | if ($iv.start | t) > $edge then . + [[$iv]]
      else .[:-1] + [(.[-1] + [$iv])] end
    end);

def cluster_stats:
  ((map(.stop | t) | max) - (map(.start | t) | min)) as $wall
  | (map(.dur) | add) as $busy
  | { start: (map(.start) | min), stop: (map(.stop) | max),
      wall: $wall, busy: $busy,
      overlap_saved: ($busy - $wall),
      serial_tail: ($wall - (map(.dur) | max)),
      members: map({agent_type, agent_id, dur, description}) };

def mark_partial($unclosed):
  . as $step
  | $step + { partial: ([$unclosed[]
                         | select(.ts >= $step.start and .ts <= $step.stop)]
                        | length) };

# tracked = ids the instrumentation visibly saw begin: any start record,
# or any tool-family record carrying a dispatched id
def is_tracked($tracked):
  (.agent_type | known)
  or ((.agent_id // "") as $i | ($tracked | index($i)) != null);

pair_lifecycle as $L
| pair_tools as $T
| ($T | join_labels($L.intervals)) as $J
| ([.[] | select(.event == "subagent_start" or .event == "dispatch"
                 or .event == "return" or .event == "return_failure")
    | (.agent_id // "")] | map(select(. != "")) | unique) as $tracked
| { session_id: (first(.[].session_id // empty) // "unknown"),
    unidentified_lifecycle: ($L.unidentified | map({event, ts})),
    orphan_stops: [$L.orphans[] | select(is_tracked($tracked))
                   | {agent_id, agent_type, ts}],
    untracked_stops: [$L.orphans[] | select(is_tracked($tracked) | not)
                      | {agent_id, ts}],
    type_mismatches: $J.mismatches,
    pair_type_mismatches: $L.pair_mismatches,
    unclosed_starts: ($L.unclosed | map({agent_id, agent_type, ts})),
    launch_acks: [$T[] | select(.launch_ack)
                  | { tool_use_id,
                      agent_type: (.dispatch.agent_type // "unknown"),
                      delta }],
    steps: ($L.intervals
            | map(.description = ($J.labels[.agent_id] // ""))
            | clusters
            | map(cluster_stats | mark_partial($L.unclosed))) }
