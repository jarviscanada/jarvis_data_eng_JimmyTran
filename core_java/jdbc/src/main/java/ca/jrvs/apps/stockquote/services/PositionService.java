package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;

import java.util.Optional;

public class PositionService {

    private PositionDao posDao;
    private QuoteDao quoteDao;

    public PositionService(final PositionDao posDao, final QuoteDao quoteDao) {
        this.posDao = posDao;
        this.quoteDao = quoteDao;
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
        Position position = new Position();
        Optional<Quote> quoteOptional = quoteDao.findById(ticker);

        if (quoteOptional.isEmpty()) {
            throw new IllegalArgumentException("Quote not found for ticker: " + ticker);
        }

        Quote quote = quoteOptional.get();

        // Check if the numberOfShares being bought is less than or equal to the available volume
        if (numberOfShares > quote.getVolume()) {
            throw new IllegalArgumentException("Cannot buy more shares than available volume");
        }

        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(price);
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
            System.out.println("You do not own this stock");
            return;
        }
        posDao.deleteById(ticker);
    }

    public Iterable<Position> viewAll() {
        return posDao.findAll();
    }

    public Optional<Position> view(String ticker) {
        Optional<Position> positionOptional = posDao.findById(ticker);
        if (positionOptional.isEmpty() || positionOptional == null) {
            throw new IllegalArgumentException("You do not own this stock");
        }
        return positionOptional;
    }
}