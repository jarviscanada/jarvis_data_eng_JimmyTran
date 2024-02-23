package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuoteService_IntTest {
    private static final String apiKey = "bf054731femsh35fa8d48cc951a8p11d1d6jsnb77c1a86a057";
    private DatabaseConnectionManager dcm;
    private Connection connection;
    private QuoteHttpHelper httpHelper;
    private QuoteService quoteService;
    private String ticker;
    private String invalidTicker;

    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        httpHelper = new QuoteHttpHelper(apiKey, new OkHttpClient());
        quoteService = new QuoteService(new QuoteDao(connection), httpHelper);

        ticker = "AAPL";
        invalidTicker = "INVALIDSTOCKTICKER";
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void fetchQuoteDataFromAPI_Test_Pass() {
        Optional<Quote> fetchedQuote = quoteService.fetchQuoteDataFromAPI(ticker);
        assertTrue(fetchedQuote.isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fetchQuoteDataFromAPI_Test_Fail() {
        Optional<Quote> fetchedQuote = quoteService.fetchQuoteDataFromAPI(invalidTicker);
        assertFalse(fetchedQuote.isPresent());
    }
}
