# AShop

A Spring Boot project with JPA and Spring Security for Three Null Shop.

## Overview

This project is a Spring Boot application that incorporates Spring Data JPA and Spring Security, tailored for Three Null Shop. It includes dependencies for various Spring Boot starters, Thymeleaf, QueryDSL, H2 database (for testing), MySQL connector, Lombok, and more.

## Prerequisites

- Java 11
- MySQL database

## Project Structure

- **GroupId:** com.AShop
- **ArtifactId:** Ashop
- **Version:** 0.0.1-SNAPSHOT
- **Description:** Three Null Shop for Spring Boot JPA with Spring Security

## Dependencies

- Spring Boot starters for Data JPA, Thymeleaf, Web, and Security
- DevTools for runtime development support
- Testing dependencies for Spring Boot and Spring Security
- H2 database and MySQL connector for data persistence
- Lombok for simplified Java development
- QueryDSL for type-safe querying
- Thymeleaf layout dialect for enhanced Thymeleaf templating
- Validation and ModelMapper for data validation and mapping

## Build Configuration

The project uses Maven as the build tool. Notable plugins include:

- `spring-boot-maven-plugin` for packaging the application
- `apt-maven-plugin` for QueryDSL annotation processing

## Build and Run

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/Ashop.git
