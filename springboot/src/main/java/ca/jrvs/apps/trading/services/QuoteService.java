package ca.jrvs.apps.trading.services;

import ca.jrvs.apps.trading.IexQuote;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuoteService {

    private final MarketDataDao marketDataDao;

    private Logger logger = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    public QuoteService(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
    }

    /**
     * Find an IexQuote by ticker
     *
     * @param ticker the ticker symbol of the stock
     * @return IexQuote object
     * @throws IllegalArgumentException if ticker is invalid or not found
     */
    public IexQuote findIexQuoteByTicker(String ticker) {
        Optional<IexQuote> quoteOptional = marketDataDao.findById(ticker);
        return quoteOptional.orElseThrow(() -> new IllegalArgumentException("Invalid or not found ticker: " + ticker));
    }
}
