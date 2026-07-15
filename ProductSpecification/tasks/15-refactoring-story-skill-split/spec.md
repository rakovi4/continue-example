# Task 15: /story Skill Split + Story-Quality Guardrail

Type: refactoring

Framework speed-up initiative **T8** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D5) and
[checklist items 3a, 3b, 3c, N1](../../framework-speedup/checklist.md).

## Problem

The `/story` skill concept is commonly misunderstood because it is outdated:

- It generates a story spec, but it is only ever called from `/continue` —
  nobody invokes it standalone (D5), so it doesn't deserve a user-facing skill
  name.
- Everyone's intuition is that `/story` should **add a story to the backlog**
  in `stories.md` — which no skill currently does.
- Nothing guards story quality at creation: teams add tech stories ("add Kafka
  streaming") and part-stories ("login and download image" + "delete
  background from downloaded image" instead of "delete background"), and
  stories grow to ~100 test cases, taking weeks (checklist N1).

## Solution

### Step 1: Downgrade the spec generator (3a)

Move the current `/story` spec-generation prompt into a `/continue`-internal
prompt template. No standalone usage exists, so no muscle-memory break (D5).

### Step 2: New `/story` = backlog add (3b)

Create a new `/story` skill that adds a story row to the **Backlog** table in
`ProductSpecification/stories.md` (all `·` columns, per the lifecycle rules).

### Step 3: Story-quality guardrail (3c + N1)

The new `/story` acts as a guardrail at add time:

- A story is a **pain to be cured or an opportunity to be gained** — reject
  tech stories and part-stories; push back with the reformulated user-value
  story instead of silently accepting.
- **Size check**: when the described story would plausibly explode into tens of
  scenarios, propose a split into independently valuable stories before adding
  the row. Prevention half of N1 — Task 16 parallelizes the giant stories that
  exist anyway.

## Key Files

- `.claude/skills/story/SKILL.md` (replaced)
- `.claude/skills/continue/SKILL.md` (spec-phase dispatch)
- `.claude/templates/` (new internal spec-generation template)
- `ProductSpecification/stories.md` (Backlog table target)

## Dependencies

None.
