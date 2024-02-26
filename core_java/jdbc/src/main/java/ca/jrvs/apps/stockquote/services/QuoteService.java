package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;

public class QuoteService {
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     *
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        Optional<Quote> quote = Optional.of(new Quote());
        try {
            infoLogger.info("Quote service: Fetching quote from api");
            quote = Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));
            if (quote.isEmpty() || quote.get() == null) {
                errorLogger.error("Quote service: Invalid ticker please type a valid quote ticker!");
                throw new IllegalArgumentException("Invalid ticker please type a valid quote ticker!");
            }

            if (dao.findById(ticker).isEmpty()) {
                dao.save(quote.get());
                infoLogger.info("Quote service: Quote doesnt cuurently exist in table, saving to table.");
                return quote;
            }
            infoLogger.info("Quote service: Succesffuly fetched quote from api");
            return quote;
        } catch (NoSuchElementException e) {
            errorLogger.error("Quote service: NoSuchElementException, Ticker doesnt exist" + e.getMessage());
        }
        return quote;
    }
}