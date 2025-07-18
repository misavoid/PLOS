# Email Ingestion Module

The Email Ingestion module fetches, normalizes, and stores your email data in PLOS. It is designed to securely pull receipts, subscriptions, invoices, and more from any IMAP-compatible inbox (e.g., iCloud, Gmail, Outlook).

---

## ✉️ Overview

- Connects to your configured IMAP inbox
- Extracts metadata: **Message-ID**, **subject**, **sender**, **sent date**
- Returns results via an API endpoint (`/test-emails`)
- Credentials are provided securely via environment variables

---

## ⚙️ Setup

1. **Generate or locate your IMAP credentials.**
    - For iCloud, you must use an [app-specific password](https://support.apple.com/en-us/HT204397).
    - Example environment variables:
        ```
        export APPLE_USERNAME="yourname@icloud.com"
        export APPSPECIFIC_PASSWORD_PLOS="your-app-specific-password"
        ```

2. **Set these variables in your terminal, `.env` file, or IDE Run Configuration before starting PLOS backend.**

---

## 🔌 How It Works

- On startup, the backend exposes a `/test-emails` API endpoint.
- When you call this endpoint, the backend:
    1. Loads your IMAP credentials from environment variables
    2. Connects securely to the IMAP server (e.g., `imap.mail.me.com` for iCloud)
    3. Fetches a list of recent emails from your inbox
    4. Extracts relevant fields from each email
    5. Returns the list as a JSON array

---

## 🛡️ Security Notes

- **Never commit real credentials to code or version control.**
- **App passwords** are safer than your main password and can be revoked at any time.
- For additional privacy, always run the backend on trusted hardware and/or behind a VPN.

---

## 🗂️ Example Response

```json
[
  {
    "messageId": "<id-string>",
    "subject": "Welcome to PLOS!",
    "from": "plos@example.com",
    "sentDate": "Mon Jan 08 15:14:27 CET 2025"
  },
  ...
]
