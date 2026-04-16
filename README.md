# Continue Example — Kanban Board

A demo Kanban board (Java + React) built entirely through the [/continue framework](https://github.com/rakovi4/continue-framework) — no code written by hand.

Three planned stories:

| # | Story | Status |
|---|-------|--------|
| 1 | Create task | Backend done, frontend in progress |
| 2 | Move task | Spec done, development not started |
| 3 | Edit/delete task | Not started |

## Quick Start

### Prerequisites

- [Claude Code](https://docs.anthropic.com/en/docs/claude-code) with a Claude subscription (or Claude-compatible LLM)
- Java 17+, Node.js 18+

### Try it

```bash
git clone <repo-url> continue-example1
cd continue-example1
claude
# inside Claude Code:
/continue 1
```

Claude reads `progress.md`, finds the last completed step, and continues — writes the next test, implements code, runs quality gates, commits, and stops for your review.

Review the diff. If it looks good — run `/continue 1` again. If you have feedback — tell Claude, it will fix it.

### Parallel Work

Clone the repo 3 times to work on all stories simultaneously:

```bash
git clone <repo-url> continue-example1
git clone <repo-url> continue-example2
git clone <repo-url> continue-example3
```

Open each in a separate IDE and run:

| IDE | Command | What happens |
|-----|---------|--------------|
| 1 | `/continue 1` | Continues frontend for story 1 |
| 2 | `/continue 2` | Starts backend for story 2 (first acceptance test) |
| 3 | `/continue 3` | Starts from scratch — interview, spec, test plan |

The folder suffix (`1`, `2`, `3`) isolates ports so parallel instances don't conflict.

One `/continue` run takes 5–20 minutes. Review one stream's commits while another is working.

## Useful Commands

| Command | What it does |
|---------|--------------|
| `/continue N` | Continue working on story N |
| `/task` | Create a standalone task (bug, refactoring) |
| `/architecture` | Make an architectural decision with ADR |
| `/doc` | Document research findings |
| `/prompt-update` | Add or fix a framework rule |
| `/demo` | Run a Selenium test in visible mode |

## Start from Scratch

Want your own project? Use the [empty framework](https://github.com/rakovi4/continue-framework/tree/main):

1. Clone the empty framework
2. Fill in:
   - `ProductSpecification/BriefProductDescription.md` — product description
   - `ProductSpecification/ExpectedLoad.md` — expected load
   - `ProductSpecification/technology.md` — tech stack
   - `ProductSpecification/stories.md` — list of stories
3. Run `/continue` — and keep going

Supported stacks: Java/Spring, Go, Node/Express, Python/Django, C#/.NET, PHP/Laravel, C++/CMake (backend); React, Vue, Angular (frontend); Tailwind, plain CSS; Selenium, Playwright, Cypress.

## Author

**Rakovsky Alexander**

TG: [@RakovskyXP](https://t.me/RakovskyXP)
