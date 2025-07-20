# Playbook

## 📅 Phase 1: Foundations (Weeks 1–2)
1. **Project scaffolding**
    - Initialize a Git repo.
    - Set up Kotlin/Ktor backend module and Compose Desktop client module.
    - Configure Gradle with Exposed ORM, Jakarta Mail, Ktor client/server, coroutines.

2. **Infrastructure prep**
    - Choose your DB (SQLite for v1; Postgres if you want early scale).
    - Provision a dev server or Docker Compose stack.
    - Set up CI (simple “build & test” on GitHub Actions).

## 📥 Phase 2: Email Ingestion (Weeks 3–4)
3. **IMAP connector**
    - Write a service that connects to one IMAP account (e.g. Gmail).
    - Fetch headers & bodies, store raw messages in DB.

4. **Basic parser & storage**
    - Extract Message-ID, date, subject.
    - Deduplicate via Message-ID.
    - Save parsed metadata tables (sender, date, subject).

## 📆 Phase 3: Calendar & Reminders (Weeks 5–6)
5. **CalDAV sync**
    - Integrate a CalDAV client (iCloud/Google).
    - Pull events/reminders, normalize into an “events” table.

6. **iOS Shortcuts bridge (optional)**
    - If you need deeper Reminders access, build a tiny Swift agent that pushes to your Ktor API.

## 🏃‍♂️ Phase 4: Health & Fitness (Weeks 7–8)
7. **iOS HealthKit agent**
    - Build a minimal Swift iOS app that exports daily HealthKit summaries to your backend.
    - Define data schema (steps, sleep, workouts).

8. **Backend ingestion & storage**
    - Create endpoints, data models, Exposed tables.
    - Schedule daily fetch jobs via coroutines.

## 💳 Phase 5: Bank & Finance (Weeks 9–10)
9. **Statement parser**
    - Start with CSV import.
    - Write parser to detect transaction date, amount, description.
    - Normalize into “transactions” table.

10. **Receipt matching**
    - Link email receipts to bank transactions by date+amount heuristics.

## 🔄 Phase 6: Normalization & Core ETL (Weeks 11–12)
11. **Unified schema**
    - Build views or ETL jobs that merge data sources (email, calendar, health, finance).
    - Tag each record with “source” and “type” for querying.

12. **Data quality**
    - Add dedupe, timestamp alignment, basic anomaly detection rules.

## 🤖 Phase 7: Automation & Alerts (Weeks 13–14)
13. **Scheduled jobs**
    - Use coroutines + cron to run daily/weekly reports.
    - Implement alerts (e.g., subscription renewal in 7 days).

14. **Telegram integration**
    - Hook in a bot to push summaries or alerts on demand.

## 🖥️ Phase 8: Dashboard & UI (Weeks 15–16)
15. **Compose Desktop dashboard**
    - Show key metrics: monthly spend, calendar load, step trends.
    - Allow manual sync, settings, account management.

16. **Offline mode & caching**
    - Implement local cache fallback for when server/API is unreachable.

## 🚀 Phase 9: Polish & Extend
17. **Secure & deploy**
    - Enable TLS on your Ktor API.
    - Containerize services & roll out to your production box.

18. **Module expansion**
    - Add new sources (To-Do apps, home-lab telemetry).
    - Swap SQLite → Postgres if needed.
    - Introduce ML parsing microservice for smarter entity extraction.

> **Pro tip:** Ship a minimal working slice every 2 weeks. Iterate fast, refine later.
