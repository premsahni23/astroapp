# Mindaro Backend (Spring Boot)

Backend APIs for the Mindaro app (Users + Mentors/Astrologers) using **Spring Boot + MariaDB**.

## Run

```bash
./gradlew bootRun
```

Default server: `http://localhost:3000`

## Swagger

- Swagger UI: `http://localhost:3000/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:3000/v3/api-docs`

## Database schema (IMPORTANT)

This project uses:

```properties
spring.jpa.hibernate.ddl-auto=none
```

So new tables are provided in:

- `src/main/resources/db/schema_additions.sql`

Run that SQL in your MariaDB DB (e.g. `test_advj`) before testing the new modules.

---

# API Index (one-line description per endpoint)

## Booking / Appointment APIs
- `POST /api/bookings` — Book an astrologer/mentor session for a user.
- `GET /api/bookings/user/{userId}` — Fetch all bookings for a user.
- `GET /api/bookings/astrologer/{id}` — Fetch all bookings for an astrologer/mentor.
- `PUT /api/bookings/{id}/status` — Update booking status (e.g., PENDING/CONFIRMED/COMPLETED/CANCELLED).
- `DELETE /api/bookings/{id}` — Cancel/delete a booking by id.

## Payment APIs
- `POST /api/payments/create` — Create a payment intent/record for a user.
- `POST /api/payments/verify` — Verify a payment (provider webhook/client verification).
- `GET /api/payments/user/{userId}` — Get payment history for a user.
- `POST /api/payments/refund` — Refund a payment (provider/manual).

## Rating / Review APIs
- `POST /api/reviews` — Add a review/rating for an astrologer/mentor.
- `GET /api/reviews/astrologer/{id}` — List reviews for an astrologer/mentor.
- `PUT /api/reviews/{id}` — Update an existing review.
- `DELETE /api/reviews/{id}` — Delete a review.

## Horoscope APIs
- `GET /api/horoscope/daily/{sign}` — Return daily horoscope for a zodiac sign.
- `GET /api/horoscope/weekly/{sign}` — Return weekly horoscope for a zodiac sign.
- `GET /api/horoscope/monthly/{sign}` — Return monthly horoscope for a zodiac sign.
- `POST /api/horoscope/personalized` — Return personalized horoscope (stubbed unless provider integrated).

## Kundli / Chart APIs
- `POST /api/kundli/generate` — Generate and store a kundli/birth chart for a user.
- `GET /api/kundli/user/{userId}` — Fetch stored kundli records for a user.
- `POST /api/kundli/match` — Match two kundlis (stubbed unless algorithm/provider integrated).
- `POST /api/kundli/analysis` — Provide kundli analysis (stubbed unless algorithm/provider integrated).

## App Features (Nice to have)
- `GET /api/banners` — Get banners to show in the app.
- `GET /api/testimonials` — Get testimonials to show in the app.
- `POST /api/feedback` — Submit user feedback.
- `GET /api/faqs` — Get FAQs.

## Personalization APIs
- `GET /api/recommendations/{userId}` — Fetch personalized mentor recommendations for a user.
- `POST /api/favorites` — Add mentor to user favorites.
- `GET /api/favorites/user/{userId}` — Fetch favorite mentors for a user.
- `DELETE /api/favorites/{id}` — Remove favorite by favorite id.

## Analytics / Reports
- `POST /api/analytics/event` — Track user events for analytics.
- `GET /api/analytics/dashboard` — Fetch aggregated analytics for admin dashboard.
- `GET /api/reports/usage` — Fetch usage reports.

## Notification APIs
- `POST /api/notifications/schedule` — Schedule a notification.
- `GET /api/notifications/user/{userId}` — Get notifications for a user.
- `PUT /api/notifications/{id}/read` — Mark a notification as read.
- `POST /api/notifications/preferences` — Update notification preferences.

## Video Call APIs
- `POST /api/video/session/create` — Create a video session.
- `GET /api/video/session/{id}` — Get video session details.
- `POST /api/video/session/join` — Join a video session.
- `POST /api/video/session/end` — End a video session.

## Wallet / Credits APIs
- `GET /api/wallet/balance/{userId}` — Get wallet balance for a user.
- `POST /api/wallet/add-money` — Add money to wallet and create a wallet transaction.
- `POST /api/wallet/transactions` — Get wallet transaction history for a user.
- `POST /api/credits/purchase` — Buy credits (deduct wallet balance and record credit transaction).
