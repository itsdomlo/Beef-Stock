# Beef Stock

## A stock trading simulation for noobs

Investing can be scary when you first start it. How should I start? What stock should I buy? Will I lose all my money?

Fear not. With *Beef Stock**, you can learn the basics of stock trading without the fear of losing any money.

Using *Beef Stock*, you can:
- trade in-game stocks with **imaginary money**
- manage and grow your portfolio
- monitor market fluctuations


Let's start investing!

*The name *Beef Stock* is a play on the phrase *bull market*, which describes a phase of the financial market where prices are rising or expected to rise. 

## User Stories

- As a user, I want to create a trading account with username and password.
- As a user, I want to deposit/withdraw money in/from my trading account.
- As a user, I want to view the details of any stock on the in-game market.
- As a user, I want to buy/sell a stock.
- As a user, I want to add/remove stocks I bought/sold to/from my portfolio.
- As a user, I want to view the summary of my portfolio.
- As an admin, I want to add the trading accounts to a database.
- As a user, I want to save the accounts when quit application.
- As a user, I want to retrieve previous save of my account.

## Phase 4: Task 2

- Tue Mar 29 16:31:02 PDT 2022<br>
dominic sold 3 X TSLA at $900.00
- Tue Mar 29 16:31:10 PDT 2022<br>
dominic purchased 5 X GOOG at $3000.00
- Tue Mar 29 16:31:21 PDT 2022<br>
athena purchased 6 X AAPL at $200.00
- Tue Mar 29 16:31:27 PDT 2022<br>
athena sold 2 X MSFT at $300.00

## Phase 4: Task 3

- For GUI classes, more panels can extend the AbstractPanel to reduce code duplication and ensure uniform format
- For GUI, AccountDepositPanel and AccountWithdrawPanel is very similar. A new class/abstract class which represents the shared functionalities and formats would be useful.
- The information displayed and functionalities of the console UI (BeefStock class) and GUI (FrontGUI class) are essentially the same, they only differ in presentation. Perhaps the use of interface/abstract classes can ensure both UI to behave uniformly.
- The BeefStock class is running the entire console UI. It should split up in multiple classes to adhere to the single responsibility principle.
- As the stock market price is changing regularly, multiple labels, tables displaying information which depends on said changing stock price needs to be updated regularly as well. Implementing the Observer/Observable pattern can simplify this update process.