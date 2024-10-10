# Kalban Green Bag

![Build and deploy JAR app to Azure Web App - KALBAN](https://github.com/nguyenhuubao20/Kalban_Green_Bag/actions/workflows/main_kalban.yml/badge.svg)

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Overview
Kalban Green Bag is a web application that promotes green habits by encouraging the use of reusable bags. The project focuses on environmental sustainability and provides users with the ability to track their usage of eco-friendly bags.

This project is built using Java Spring Boot and is deployed to Azure Web App, ensuring seamless scalability and performance.

## Features
- 🛒 **Track Bag Usage:** Easily track the number of reusable bags used in daily life.
- 🌍 **Environmental Impact:** Get insights into how your bag usage contributes to saving the planet.
- 📝 **User Accounts:** Manage your personal account and bag usage history.
- 📊 **Statistics Dashboard:** View charts and graphs on bag usage statistics.

## Technologies Used
- **Java Spring Boot** - Back-end framework.
- **Azure Web App** - Cloud platform for deployment.
- **MySQL** - Relational database for storing user data.
- **Docker** - Containerization for consistency across environments.
- **GitHub Actions** - CI/CD pipelines for automatic build and deployment.
- **Thymeleaf** - Template engine for rendering HTML views.

## Installation
Follow the steps below to get the application up and running on your local machine:

1. Clone the repository:
    ```bash
    git clone https://github.com/nguyenhuubao20/Kalban_Green_Bag.git
    cd Kalban_Green_Bag
    ```

2. Set up the MySQL database and configure the connection in `application.properties`.

3. Build and run the project using Maven:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

4. The app will be available at `http://localhost:8080`.

## Usage
- **Registration and Login:** Sign up and log in to start tracking your reusable bag usage.
- **Track Bags:** Log how many bags you used, and view your environmental impact.
- **Dashboard:** View detailed insights and statistics about your green habits.

## Contributing
We welcome contributions to make Kalban Green Bag better! Here’s how you can contribute:

1. Fork the project.
2. Create a feature branch: `git checkout -b feature/new-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature/new-feature`.
5. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.
