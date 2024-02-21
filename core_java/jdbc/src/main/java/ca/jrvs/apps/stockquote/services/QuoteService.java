package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.QuoteDao;

import java.util.Optional;

public class QuoteService {

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(final QuoteDao dao, final QuoteHttpHelper httpHelper) {
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
        try {
            Optional<Quote> quote = Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));
            dao.save(quote.get());
            return quote;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();

        }
    }
}