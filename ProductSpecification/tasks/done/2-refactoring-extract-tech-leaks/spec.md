# Task 2: Extract Tech Leaks

Type: refactoring

## Problem

Universal layers (agents, rules, skills) contain Java/Spring-specific grep patterns, tool names, and endpoints that silently fail in non-Java projects. Template code examples with Java syntax are fine — Claude adapts them across languages. But detection/enforcement patterns that grep for `assertThat`, `RestAssured`, `@Autowired`, `JpaRepository` literally match nothing in Python/Go, so violations go undetected.

Three categories:
- **Agent grep patterns**: test-review-agent checklist greps for AssertJ methods, RestAssured, `private record`, `@Autowired`. refactor-agent uses Java inheritance terms. scan-checklist has JPA-specific storage checks.
- **Skill tool names**: test-coverage mentions `JaCoCo`, test-review uses `.java` extension and AssertJ names, test-acceptance uses `/actuator/health`.
- **Rule named libraries**: tdd-rules names `rest-assured, HttpClient, fetch`, frontend-rules has `describe()`, `it()`, coding-rules has "immutable annotations".

## Solution

Move, don't delete. Each Java-specific pattern moves to the tech binding (`.claude/tech/java-spring/`), and the universal layer gets a generic version that references the tech binding.

Pattern for agents: the checklist item keeps the universal *check description* (what to look for) but replaces the hardcoded grep pattern with "see tech binding for patterns". The tech binding gets a new section with the actual grep patterns per checklist item. Agents already load tech bindings — they just need to load the grep patterns from there too.

Pattern for rules: replace named libraries with generic descriptions ("the project's HTTP testing library" instead of "rest-assured, HttpClient").

Pattern for skills: replace tool names with references to technology.md Conventions table ("coverage tool" instead of "JaCoCo", "health endpoint" instead of "/actuator/health").

This way Java projects keep full detection — nothing is lost. Other profiles define their own grep patterns.

## Key Files

### Agents (move grep patterns to tech binding)
- `.claude/agents/test-review-agent.md` — checklist grep patterns → `.claude/tech/java-spring/tdd.md`
- `.claude/agents/refactor-agent.md` — Java inheritance terms → `.claude/tech/java-spring/coding.md`
- `.claude/templates/refactoring/scan-checklist.md` — JPA storage checks → `.claude/tech/java-spring/coding.md`

### Skills (replace tool names with Conventions table references)
- `.claude/skills/test-coverage/SKILL.md` — JaCoCo → "coverage tool (see technology.md)"
- `.claude/skills/test-review/SKILL.md` — .java, AssertJ → generic
- `.claude/skills/test-acceptance/SKILL.md` — /actuator/health → "health endpoint (see technology.md)"

### Rules (replace named libraries with generic descriptions)
- `.claude/rules/tdd-rules.md` — named libraries → generic
- `.claude/rules/frontend-rules.md` — Jest/Mocha syntax → generic
- `.claude/rules/coding-rules.md` — Lombok term → generic

### Tech bindings (receive extracted patterns)
- `.claude/tech/java-spring/tdd.md` — receives test-review grep patterns
- `.claude/tech/java-spring/coding.md` — receives refactor-agent and scan-checklist patterns
