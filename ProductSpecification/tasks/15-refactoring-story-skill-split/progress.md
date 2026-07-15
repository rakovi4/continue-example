# Task 15: /story Skill Split + Story-Quality Guardrail -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Downgrade the spec generator
- [ ] refactor (move spec generation to a /continue-internal prompt template)
- [ ] refactor (update continue/SKILL.md spec-phase dispatch)

### Step 2: New /story = backlog add
- [ ] refactor (new /story skill adds a Backlog row to stories.md)

### Step 3: Story-quality guardrail
- [ ] refactor (pain/opportunity guardrail: reject tech stories and part-stories)
- [ ] refactor (story-size check: propose split before adding an oversized story)
