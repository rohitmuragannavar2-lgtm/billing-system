# Hotel House — Restaurant Ordering & Billing System

A full-stack restaurant website with a premium food-focused frontend and MySQL-backed customer, menu, order and bill management.

## Features
- Public restaurant landing page with full-width food photography
- Customer registration and login
- Separate admin login and dashboard
- Responsive menu with Veg, Non-Veg, Beverages, Ice Cream and Desserts categories
- Food photo cards, cart quantity controls and order placement
- Admin can view customer orders and update order status
- Admin can add/remove menu items
- Admin generates customer bills (5% tax); bill and bill items are saved in MySQL
- Customers can view their order history and generated bills
- Optional email delivery if SMTP is configured
- No online payment gateway

## Technology
- Frontend: React 18, Vite, React Router, Axios
- Backend: Java 17, Spring Boot 3, Spring Security, JWT, Spring Data JPA
- Database: MySQL 8
- Schema and starter menu: Flyway SQL migrations

## Requirements
- Java 17+
- Maven 3.9+ (or Maven support in IntelliJ)
- Node.js 20+
- MySQL 8+

## 1. Set up MySQL

Start MySQL, then run the following in MySQL Workbench or DBeaver:

```sql
CREATE DATABASE IF NOT EXISTS hotel_billing;
```

The default local configuration in `backend/src/main/resources/application.properties` is:
- Database: `hotel_billing`
- Username: `root`
- Password: blank

If your MySQL username/password differs, either edit `application.properties` or set environment variables before starting the backend:

```bash
export DB_URL='jdbc:mysql://localhost:3306/hotel_billing?createDatabaseIfNotExist=true'
export DB_USERNAME='your_mysql_username'
export DB_PASSWORD='your_mysql_password'
```

On the first successful backend startup, Flyway automatically creates these tables:
- `users` — customer and admin accounts (passwords are BCrypt-hashed)
- `menu_items` — food menu and prices
- `customer_orders` — customer orders
- `order_items` — each ordered food item
- `bills` — generated bill totals, tax and bill number
- `bill_items` — saved bill line items

The initial food menu is inserted automatically by the Flyway seed migration. Do not manually recreate these tables.

## 2. Start the backend

Open Terminal:

```bash
cd hotel-billing-management-system/backend
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`.

Default admin credentials:
- Email: `admin@hotel.com`
- Password: `Admin@123`

Change the default admin password and JWT secret before any public deployment. You can override the admin credentials before first startup using `ADMIN_EMAIL` and `ADMIN_PASSWORD`.

## 3. Start the frontend

Open a second Terminal window:

```bash
cd hotel-billing-management-system/frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

Food photos and font files are loaded from external image/font CDNs, so an internet connection is needed for those visual assets. Customer accounts, menu items, orders, order items, bills and bill items are stored in your MySQL database through the Spring Boot API.

## 4. Build frontend for deployment

```bash
cd frontend
npm run build
```

The production build is created in `frontend/dist`. Deploy that directory with a web server and configure `/api` requests to reach the Spring Boot backend. Configure production database credentials, HTTPS, a strong JWT secret, backups and a production mail provider before public use.

## API overview
- `POST /api/auth/register` — create customer account
- `POST /api/auth/login` — login
- `GET /api/menu` — view active menu
- `POST /api/menu` — admin adds a menu item
- `DELETE /api/menu/{id}` — admin deactivates a menu item
- `POST /api/orders` — customer places an order
- `GET /api/orders/my` — customer's order history
- `GET /api/orders` — admin views all orders
- `PATCH /api/orders/{id}/status` — admin updates order status
- `POST /api/orders/{id}/generate-bill` — admin generates and saves bill
- `GET /api/bills/my` — customer's saved bills
- `GET /api/bills` — admin views all bills
- `POST /api/bills/{id}/email` — optional bill email (requires SMTP)

## Data storage note
All business data is persisted in MySQL, not browser local storage. Browser local storage is used only to keep the signed-in session token/profile. The backend writes accounts, menu records, orders, order lines, generated bills and bill lines to the SQL database.
