# NewBank Project - Group 2

## General Information

The NewBank is an operational banking system that interacts with users via a simple command-line interface.

This repository is based upon the task set as part of the unit software engineering 2 at the University of Bath. 


## Table of contents

- [NewBank Project - Group 2](#newbank-project---group-2)
  * [General Information](#general-information)
  * [Table of contents](#table-of-contents)
  * [Technologies used](#technologies-used)
  * [Features](#features)
  * [Project Overview](#project-overview)
    + [Example of initial user interaction](#example-of-initial-user-interaction)
    + [Example of verifying current customer](#example-of-verifying-current-customer)
    + [Example of current customer interaction menu](#example-of-current-customer-interaction-menu)
    + [Users Database](#users-database)
    + [Account Type Database](#account-type-database)
  * [Authors and Acknowledgements](#authors-and-acknowledgements)
  * [Trello](#trello)
  * [How to Contribute to the Project](#how-to-contribute-to-the-project)
  


## Technologies used
- Java
- SQlite3
- JDBC 


## Features 

Functions include: 
- Displaying a user's current account
- Creating a new account
- Transferring funds to other owned accounts on the system.
- Transactional processes in the existing account.
- The removal of an account.

Any user data inputted into the system is updated on the database. 


## Project Overview

### Example of initial user interaction

Once starting NewBank, the user will be prompted with this menu.

<img width="460" alt="Screenshot 2021-12-15 at 02 53 41" src="https://user-images.githubusercontent.com/64655977/146114572-dec76480-5c24-4e13-9104-e5f68eb12e9d.png">

### Example of verifying current customer

The customer will then input their username and password, which is checked using the data within the database.

<img width="394" alt="Screenshot 2021-12-15 at 02 54 04" src="https://user-images.githubusercontent.com/64655977/146114579-ff7d99c5-035f-4db6-ab6c-a77ba745564a.png">

### Example of current customer interaction menu

After verification, the user is then prompted by the features provided in NewBank.

<img width="447" alt="Screenshot 2021-12-15 at 02 54 15" src="https://user-images.githubusercontent.com/64655977/146114590-4bb9d900-6c7f-4cb9-a273-933ebccd8340.png">


### Users Database

All customer data is contained within this table and is used to check or update user records. 

<img width="952" alt="Screenshot 2021-12-15 at 01 46 27" src="https://user-images.githubusercontent.com/64655977/146112126-b6a717eb-5d58-49cf-a8d8-f3b676065957.png">

### Account Type Database

The account table contains a foreign key that links to the user type database as shown below to accommodate different account types.

<img width="845" alt="Screenshot 2021-12-15 at 01 43 30" src="https://user-images.githubusercontent.com/64655977/146112164-05cd9235-2474-434d-b695-f18da293a089.png">


## Authors and Acknowledgements 
- Matija Komorcec 
- Portia McDowell
- Spyridon Giannakopoulos
- Stephen Lyen


## Trello 
<https://trello.com/b/DVUFnnnK/newbank-software>
We used Trello to track all of our tasks and plan features.


## How to Contribute to the Project
Pull requests are welcome. Please open an issue first to discuss what you would like to change for significant changes.

Please make sure to update tests as appropriate.

## Last updated on 14/12/21
