# Initial Problem Statement

Recorded 2026-07-15. Reproduced from the initiating prompt (typos preserved)
so later re-reads audit against the *original* ask, not a paraphrase. The traceability
mapping for every numbered item below is in [checklist.md](checklist.md).

---

We have multiple problems with the framework. let's create tasks to fix them

1. At the beginning of the framework I had an average speed of ~7 test scenarios per day (20 feb - 86 backend, 45 frontend test-cases in done; 19 mar - 239 back, 85 front in done). Currently I see my team after adopting the framework goes at the average speed of about 3 test-cases per fulltime day, excluding weekends.
I can see several caveats here that can slow them down.

1.1. First, of course, that was my peak speed at 6 sessions in parallel. They do 3-4 sessions max.

1.2. I added new review agents at the end of every continue step. That really slows down every step, adding several minutes after each one of them.
- We need to measure the execution time of the review agents
- We need to run them in parallel with every quality gate agent of continue step if it is there
- We need to find away to speed them up

1.3. We may try to find a way to run quailty gate agents in parallel, where it's possible.

1.4. We may try to split and parallelize quality gate agents where it's possible

1.5. We need to run in parallel red-adapter+green-adapter cycles. For each discovered adapter: run red-adapter+green-adapter sequence in parallel. For example: red-rest+green-rest in parallel with red-storage+green-storage. And remove human review step between them

1.6. We need to run in parallel red-coverage steps. I.e. after 4 gaps are found we add 3 red-usecase steps that are run in parallel and 1 green-usecase that is run after. And we don't need to wait until the original green-usecase is over. Currently it is green-agent -> test-review x3 -> coverage-agent -> refactor + review agents (in parallel). And we don't need to wait till the refactor and review agents are done. We can run red-agent for each gap in parallel with the original green-usecase refactor+review agents gate.

1.7. We need to verify our refactor agents run in parallel, not in sequence. Same for test-review.

1.8. We need to verify our refactor agents run in parallel with review agents.

1.9. We need to think about frontend flow. Maybe running application red-green and client red-green in parallel. Of course that means eliminitaion of the human review phase between the red-greens cycles.

Concerns:
- That is complex context engineering. If we could make it somehow more determenistic that would be amazing
- We need to estimate every step in terms of how much it can cut from our execution time
- We need to create an ideal benchmark test to measure the current version with the new one. I think of running the story 2 first scenario for backend without any review from red to green acceptance. For frontend we can use 1st ui scenario for story 1. What do you think?

2. Auto-accept, auto-apply and commit the review agent concerns fixes. Currently they are only displayed at the end as informational notes and we manually ask to fix the concerns after each continue.

3. /story skill concept is commonly misunderstood. Because it is outdated as hell.
- I think we need to downgrade that from skills to prompt template for /continue skill because we never should call it separately
- And for the /story skill we should create a separate skill that just adds new story to backlog (stories.md) since everyones intuition is that /story should do exactly that.
- And that /story skill should be a guardrail for devs to prevent them from creation some stupid tech stories or part stories. I mean when you create a story like "add Kafka streaming" or 2 stories "login and download image" and "delete background from downloaded image" insteal of "delete background". We should consider story as a pain that should be cured or an opprotunity that should be gained.

---

## Follow-up clarifications (second message, summarized quotes)

- Speed baseline correction: *"I'd estimate my real per workday tempo as 10+ scenarios per full 8-hour workday."*
- *"I'd rather not push guys to 6 sessions limit since it's my natural ability to switch contexts fast."*
- *"We need to increase the step, scenario and story speed regardless of guys abilities."*
- Review quality context: *"Here I can see an urge for development speed and the review is not very thorough. Moreover some steps are considered by guys as paranoic bureaucratic and they partly right."*
- New pain: *"agents… are not very successfull in parallelizing the stories and tasks and some tasks are growing up to 100 test-cases that is huge and means they are going to end them only in several weeks and that's unacceptable."*
- Auto-apply is already de-facto practice: *"They already updated their copies of framework to auto-accept those fixes and just review those post-factum. So we just need to reflect the common practice here."*
- /story has no muscle-memory constraint: *"no one uses it. It is not supposed to be used outside the /continue."*
- Benchmark scope: *"I'd measure only theoretical ceiling excluding human factor at all. No-review flow for old implementation (create branch from commit, run scenario to green-acceptance, log all the times, assess) and for new one. Then compare."*
