# Order Control — Food Ordering Dashboard (React Frontend)

A production-ready React frontend for the Online Food Ordering System backend
(order-service, payment-service, kitchen-service, delivery-service).

## Stack

- React 18 (functional components + hooks, no TypeScript, no Redux)
- Axios for REST calls
- Bootstrap 5 for layout/components
- Plain CSS (no CSS-in-JS, no Material UI)

## Project Structure

```
food-order-ui
├── package.json
├── public
│   └── index.html
├── src
│   ├── components
│   │   ├── OrderForm.jsx
│   │   ├── OrdersTable.jsx
│   │   ├── Navbar.jsx
│   │   ├── StatusBadge.jsx
│   │   └── LoadingSpinner.jsx
│   ├── pages
│   │   └── Dashboard.jsx
│   ├── services
│   │   └── orderService.js
│   ├── styles
│   │   └── app.css
│   ├── App.js
│   ├── index.js
│   └── index.css
├── .env.example
└── .gitignore
```

## Getting Started

1. Make sure the backend `order-service` is running on `http://localhost:8081`
   (CORS must allow requests from `http://localhost:3000`).

2. Install dependencies:

   ```bash
   npm install
   ```

3. (Optional) Copy `.env.example` to `.env` if you need to point the app at a
   different order-service URL:

   ```bash
   cp .env.example .env
   ```

4. Start the dev server:

   ```bash
   npm start
   ```

   The app opens at `http://localhost:3000`.

## Features

- **Dashboard** with live stat cards (total, in progress, delivered, failed)
- **Place Order form** with client-side validation and inline error messages
- **Orders table** showing Order ID, Customer Name, Food Item, Amount,
  Status, and Created Time
- **Auto-refresh every 2 seconds** (background polling, no visible flicker)
- **Loading spinner** on first load
- **Success / error alerts** that auto-dismiss after 4 seconds
- **Color-coded status badges**:
  - `PLACED` — Blue
  - `PAYMENT` — Orange
  - `KITCHEN_PREPARATION` — Purple
  - `DELIVERY` — Teal
  - `DELIVERED` — Green
  - `FAILED` — Red
- Fully responsive (mobile, tablet, desktop) using Bootstrap's grid

## API Contract

**Place an order**

```
POST http://localhost:8081/api/orders
Content-Type: application/json

{
  "customerName": "Susil",
  "item": "Pizza",
  "amount": 250
}
```

**List all orders**

```
GET http://localhost:8081/api/orders
```

```json
[
  {
    "id": 1,
    "customerName": "Susil",
    "foodItem": "Pizza",
    "amount": 250,
    "status": "PLACED",
    "createdAt": "2026-07-16T10:15:00"
  }
]
```

## Notes on CORS

If the browser blocks requests to `localhost:8081`, add a CORS
configuration to `order-service` allowing origin `http://localhost:3000`,
e.g. with a `WebMvcConfigurer` bean or `@CrossOrigin` on the controller.

## Build for production

```bash
npm run build
```

Outputs an optimized static bundle to the `build/` folder, ready to be
served by any static file server or reverse proxy.
