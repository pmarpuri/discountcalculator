# Discount Calculator

## Overview

This project is a Spring Boot application for calculating discounts on bills. It includes endpoints for calculating discounts and generating reports. This README explains how to run the application, execute tests, and generate code coverage reports.

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17 or later**: [Download and install](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Maven**: [Download and install](https://maven.apache.org/download.cgi)

## Running the Application

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/pmarpuri/discountcalculator
    cd discountcalculator
    ```

2. **Build the Project:**

    ```bash
    mvn clean install
    ```

3. **Run the Application:**

    ```bash
    mvn spring-boot:run
    ```

    The application will start on `http://localhost:8080`.

## Running Tests

1. **Run Unit Tests:**

    ```bash
    mvn test
    ```

    This command will execute all unit tests in the project.

## Generating Code Coverage Reports

1. **Run Tests and Generate Coverage Report:**

    ```bash
    mvn clean verify
    ```

    This will generate the coverage report using JaCoCo.

2. **View Coverage Report:**

    The coverage report is generated in the `target/site/jacoco` directory. enter the below command to view the report in a web browser.
   ```
    start "" "target/site/jacoco/index.html" 
    ```
## UML Class Diagram
	UML class diagram can be found at the \src\main\resources\templates\UML_class_diagram.png
