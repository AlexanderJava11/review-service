# Review Service

Review Service är en fristående Spring Boot-mikrotjänst som äger all data
om recensioner. Den exponerar ett REST-API som **Booking Service**
använder för att visa och hantera recensioner i sitt webb-UI.

Tjänsten har ingen egen webb-vy och anropar inga andra tjänster själv –
den är den enklaste av de tre och tar bara emot anrop.

## Vad tjänsten gör

- Lagrar recensioner (`Review`: kund-id, betyg 1–5, kommentar) i sin egen
  MySQL-databas (`reviewdb`).
- REST-API under `/api/reviews`:
  - `GET /api/reviews` – lista alla recensioner
  - `GET /api/reviews/{id}` – hämta en recension
  - `POST /api/reviews` – skapa en ny recension (`201 Created`)
  - `PUT /api/reviews/{id}` – uppdatera en recension
  - `DELETE /api/reviews/{id}` – ta bort en recension (`204 No Content`)
- Validerar inkommande data (`@Valid`, t.ex. betyg mellan 1 och 5 och att
  kommentar inte är tom).

## Hur tjänsten pratar med de andra

```
+------------------+   GET/POST/PUT/DELETE   +------------------+
| Booking Service  | ----------------------> |  Review Service  |
|   (port 8080)    |      /api/reviews       |   (port 8082)    |
+------------------+                         +------------------+
```

- **Inkommande**: Booking Service (via `ReviewClient`) anropar detta API
  för att lista, hämta, skapa, uppdatera och ta bort recensioner, och
  visar resultatet i sin `/reviews`-vy.
- Review Service anropar själv **ingen** annan tjänst.
- Kommunikationen sker via **REST/JSON över HTTP**.
- Egen databas – delar inte databas med de andra tjänsterna.

## Konfiguration (miljövariabler)

| Variabel | Standardvärde (lokalt) | Beskrivning |
|---|---|---|
| `REVIEW_DB_URL` | `jdbc:mysql://localhost:3309/reviewdb...` | JDBC-URL till recensionsdatabasen |
| `REVIEW_DB_USERNAME` | `root` | DB-användare |
| `REVIEW_DB_PASSWORD` | `reviewpassword` | DB-lösenord |

Tjänsten körs på **port 8082**.

## Så här startar du hela systemet

Den här tjänsten är tänkt att köras tillsammans med `booking-service` och
`customer-service` via `docker-compose.yml`, som ligger i
**booking-service**-repot. Klona alla tre repon som syskonkataloger:

```
projekt/
├── booking-service/    <- docker-compose.yml ligger här
├── customer-service/
└── review-service/
```

```bash
git clone https://github.com/AlexanderJava11/booking-service.git
git clone https://github.com/AlexanderJava11/customer-service.git
git clone https://github.com/AlexanderJava11/review-service.git

cd booking-service
docker compose up --build
```

Det startar bland annat:

- `review-db` – MySQL på värdport `3309` (databas `reviewdb`)
- `review-service` – detta API på port `8082`

Testa att det fungerar:

```bash
curl http://localhost:8082/api/reviews
```

Stoppa hela systemet igen från `booking-service`-katalogen:

```bash
docker compose down
```

## Köra bara den här tjänsten lokalt (utan Docker)

```bash
./mvnw spring-boot:run
```

Kräver en lokal MySQL på port `3309` med databasen `reviewdb` (eller
egna värden via miljövariablerna ovan), t.ex.:

```bash
export REVIEW_DB_URL="jdbc:mysql://localhost:3306/reviewdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export REVIEW_DB_PASSWORD=reviewpassword
./mvnw spring-boot:run
```

## Testning

```bash
./mvnw test
```
