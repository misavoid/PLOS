# Backend API Endpoints

The PLOS backend exposes a simple REST API for automations, data ingestion, and communication with the desktop client.

## Base URL
http://localhost:8080/

## Endpoints

### `GET /hello`

- **Description:** Health check endpoint. Returns a hello message from the backend.
- **Response:**
    ```json
    {
      "text": "Hello from the PLOS backend 🚀"
    }
    ```

---

### `GET /test-emails`

- **Description:** Fetches the latest emails from your configured IMAP inbox (e.g., iCloud).
- **Requires:** IMAP credentials set as environment variables.
- **Response:** List of recent emails in JSON:
    ```json
    [
      {
        "messageId": "<id-string>",
        "subject": "Your subject here",
        "from": "sender@example.com",
        "sentDate": "Thu Jan 04 12:34:56 CET 2025"
      }
    ]
    ```
- **Errors:**
    - `500 Internal Server Error` if environment variables are missing or IMAP connection fails.

---

## Authentication

Currently, endpoints are protected only by your local network—no authentication or HTTPS is enabled by default. In production, run behind a reverse proxy and/or enable authentication.

---

*See also: [docs/modules/email.md](../modules/email.md) for more on how email fetching works.*
