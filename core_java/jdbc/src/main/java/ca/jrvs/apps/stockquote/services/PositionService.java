package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PositionService {
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private PositionDao posDao;
    private QuoteService quoteService;

    public PositionService(final PositionDao posDao, final QuoteService quoteService) {
        this.posDao = posDao;
        this.quoteService = quoteService;
    }

    /**
     * Processes a buy order and updates the database accordingly
     *
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        infoLogger.info("Position service: Buy Position service running");
        Position position = new Position();
        Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(ticker);

        if (quoteOptional.isEmpty()) {
            errorLogger.error("Position service: Can not find quote for ticker {}", ticker);
            throw new IllegalArgumentException("Quote not found for ticker: " + ticker);
        }

        Quote quote = quoteOptional.get();

        // Check if the numberOfShares being bought is less than or equal to the available volume
        if (numberOfShares > quote.getVolume()) {
            errorLogger.error("Position service: Says Trying to buy more shares than exists!");
            throw new IllegalArgumentException("Cannot buy more shares than available volume");
        }

        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(price);
        infoLogger.info("Position service: Has successfully purchased stock");
        return posDao.save(position);
    }

    /**
     * Sells all shares of the given ticker symbol
     *
     * @param ticker
     */
    public void sell(String ticker) {
        Optional<Position> positionOptional = posDao.findById(ticker);
        if (positionOptional.isEmpty()) {
            errorLogger.error("Position service: You do not own this stock");
            System.out.println("You do not own this stock");
            return;
        }
        infoLogger.info("Position service: Stock has been deleted");
        posDao.deleteById(ticker);
    }

    public Iterable<Position> viewAll() {
        return posDao.findAll();
    }

    public Optional<Position> view(String ticker) {
        Optional<Position> positionOptional = posDao.findById(ticker);
        if (positionOptional.isEmpty() || positionOptional == null) {
            errorLogger.error("Position service: You do not own this stock");
            throw new IllegalArgumentException("You do not own this stock");
        }
        infoLogger.info("Position service: Returned stock to view");
        return positionOptional;
    }
}