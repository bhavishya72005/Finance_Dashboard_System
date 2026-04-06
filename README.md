# Finance_Dashboard_System
This project is a Financial Dashboard Backend built using Spring Boot and MongoDB. It provides RESTful APIs to manage financial records such as income and expenses, along with user roles and access control.

## 📌 Overview

A Spring Boot backend application to manage financial records with role-based access control and dashboard insights.

## 🚀 Features

* User Management (Admin only)
* Financial Records CRUD
* Filtering by type and category
* Dashboard APIs:

  * Summary (income, expense, balance)
  * Category-wise totals
  * Monthly trends
* Role-Based Access Control:

  * Viewer: Dashboard only
  * Analyst: Records + Dashboard
  * Admin: Full access

## 🛠️ Tech Stack

* Java (Spring Boot)
* MongoDB
* REST APIs

## 📡 API Endpoints

### Users

* POST /users?userId=ADMIN_ID
* PUT /users/{id}?userId=ADMIN_ID
* DELETE /users/{id}?userId=ADMIN_ID

### Records

* POST /records?userId=ADMIN_ID
* GET /records?userId=USER_ID
* PUT /records/{id}?userId=ADMIN_ID
* DELETE /records/{id}?userId=ADMIN_ID

### Dashboard

* GET /dashboard/summary?userId=USER_ID
* GET /dashboard/category?userId=USER_ID
* GET /dashboard/monthly?userId=USER_ID

## 🔐 Access Control

* Viewer: Dashboard only
* Analyst: View records + dashboard
* Admin: Full control

## ▶️ Run Locally

1. Clone repo
2. Run Spring Boot application
3. Use Postman to test APIs

## 👨‍💻 Author

Bhavishya
