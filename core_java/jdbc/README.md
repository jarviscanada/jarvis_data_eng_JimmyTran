# Introduction

##### The Java Stock Quote app is a command-line application designed to simulate a stock wallet, enabling users to perform various operations on their virtual stock portfolio. The app leverages the Alpha Vantage REST API to fetch real-time stock data and stores it in a local PostgreSQL database using JDBC. Additionally, Maven is employed for managing dependencies, and the application is Dockerized for easy distribution.

# Implementaiton

##### The Java app utilizes the latest version (21) of the Java programming language, incorporating the Jackson Databind library for efficient serialization and deserialization of stock data obtained from the Alpha Vantage API. Maven is employed to handle project dependencies, including jUnit, Mockito, SLF4J, and LOG4j, which are crucial for testing, logging, and debugging functionalities. The project follows standard JDBC practices, organizing components into Data Access Objects (DAOs), Services, and Controllers.

## ER Diagram

![ER Diagram.PNG](src%2Fmain%2Fresources%2FER%20Diagram.PNG)

## Design Patterns

##### The application employs the Data Access Object (DAO) and Repository design patterns. The DAO pattern separates the data access logic from the business logic, encapsulating the interaction with the database within dedicated DAO classes. This enhances modularity and maintainability, allowing for easier updates to the data access layer without affecting the rest of the application. The Repository pattern provides a higher-level abstraction, allowing the application to work with objects in the business domain without worrying about the underlying data source. It also facilitates better testability by enabling the use of mock repositories in unit tests.

# Test

##### Using jUnit and Mockito to test, test data is inserted and transformed to simulate various scenarios verifying working functionality using assert and verify statements. The tests cover CRUD operations, ensuring the proper functioning of database interactions. Mockito was utilized to simulate external dependencies, enabling isolated unit testing. Query results are verified against expected outcomes to validate the correctness of data retrieval and manipulation operations. Additionally, integration tests are performed to validate the end-to-end functionality of the application, including the interaction between different layers and components.