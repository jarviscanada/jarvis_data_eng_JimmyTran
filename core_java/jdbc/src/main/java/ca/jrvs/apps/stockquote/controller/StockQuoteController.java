package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.services.PositionService;
import ca.jrvs.apps.stockquote.services.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Scanner;
import java.util.InputMismatchException;

public class StockQuoteController {
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(final QuoteService quoteService, final PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     * OPTIONS:
     * BUY, SELL, VIEW TARGET STOCK INFO, VIEW ALL OWNED STOCKS, EXIT
     * CONSTRAINTS:
     * CANT BUY MORE THAN AVAILABLE STOCK VOLUME,
     * CANT SELL STOCKS YOU DON'T OWN
     * CANT VIEW/BUY STOCKS THAT DON'T EXIST
     */
    public void initClient() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();

            try {
                int input = scanner.nextInt();
                scanner.nextLine();

                switch (input) {
                    case 1:
                        handleBuyOption(scanner);
                        infoLogger.info("StockQuoteController: Buy option activated");
                        break;
                    case 2:
                        handleSellOption(scanner);
                        infoLogger.info("StockQuoteController: Sell option activated");

                        break;
                    case 3:
                        handleViewInfoOption(scanner);
                        infoLogger.info("StockQuoteController: View option activated");

                        break;
                    case 4:
                        handleViewAllOption(scanner);
                        infoLogger.info("StockQuoteController: View positions option activated");

                        break;
                    case 5:
                        System.out.println("Thank you, Have a great day!");
                        infoLogger.info("StockQuoteController: Application Exited");
                        return;
                    default:
                        System.out.println("Invalid command. Please enter a valid command (1-5).");
                }
            } catch (InputMismatchException e) {
                errorLogger.error("StockQuoteController: Invalid input" + e.getMessage());
                System.out.println("Invalid input. Please enter a valid integer.");
                waitForEnterKey(scanner);
            }
        }
    }

    private void displayMenu() {
        System.out.println("What would you like to do today?");
        System.out.println("1: Buy a stock");
        System.out.println("2: Sell all units of a stock");
        System.out.println("3: View a selected stock info");
        System.out.println("4: View all owned stocks");
        System.out.println("5: Exit");
        System.out.println("Please type corresponding number for your command.");
    }

    private void handleBuyOption(Scanner scanner) {
        System.out.println("Enter stock symbol you want to purchase.");
        String symbol = scanner.next().toUpperCase();
        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(symbol);
        double price = quote.get().getPrice();
        System.out.println("The price for " + symbol + " is " + price);
        System.out.println("How many shares would you like to purchase?");
        int amount = scanner.nextInt();
        System.out.println("Your order for " + amount + " " + symbol + " shares will total " + price * amount);
        System.out.println("Thank you for your purchase!");
        positionService.buy(symbol.toUpperCase(), amount, price);
        waitForEnterKey(scanner);
    }

    private void handleSellOption(Scanner scanner) {
        System.out.println("Enter stock symbol you want to sell.");
        String symbol = scanner.next().toUpperCase();
        int shares = positionService.view(symbol).get().getNumOfShares();
        double price = positionService.view(symbol).get().getValuePaid();
        positionService.sell(symbol);
        System.out.println("You have successfully sold " + shares + " of " + symbol + " for " + "$" + shares * price);
        waitForEnterKey(scanner);
    }

    private void handleViewInfoOption(Scanner scanner) {
        System.out.println("Enter stock symbol you want to view.");
        String symbol = scanner.next().toUpperCase();
        Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(symbol);
        System.out.println(quoteOptional.get().toString());
        waitForEnterKey(scanner);
    }

    private void handleViewAllOption(Scanner scanner) {
        System.out.println("Here is a list of the stocks you own.");
        positionService.viewAll().forEach(System.out::println);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void waitForEnterKey(Scanner scanner) {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }
}
