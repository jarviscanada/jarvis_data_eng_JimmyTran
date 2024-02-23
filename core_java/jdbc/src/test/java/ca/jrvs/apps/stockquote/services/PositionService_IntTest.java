package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PositionService_IntTest {

    private DatabaseConnectionManager dcm;
    private Connection connection;
    private PositionDao positionDao;
    private QuoteDao quoteDao;
    private QuoteService quoteService;
    private PositionService positionService;
    private static final String apiKey = "bf054731femsh35fa8d48cc951a8p11d1d6jsnb77c1a86a057";
    private QuoteHttpHelper httpHelper;
    private OkHttpClient httpclient;
    private String ticker;
    private String ticker2;
    private String ticker3;
    private int numOfShares;
    private double price;


    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        httpclient = new OkHttpClient();
        quoteDao = new QuoteDao(connection);
        httpHelper = new QuoteHttpHelper(apiKey, httpclient);
        quoteService = new QuoteService(quoteDao, httpHelper);
        positionDao = new PositionDao(connection);
        positionService = new PositionService(positionDao, quoteService);

        ticker = "AAPL";
        ticker2 = "MSFT";
        ticker3 = "GOOG";
        numOfShares = 10;
        price = 150.0;
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void buyTest() {
        positionService.buy(ticker, numOfShares, price);

        Optional<Position> boughtPosition = positionDao.findById(ticker);
        assertTrue(boughtPosition.isPresent());
        assertEquals(ticker, boughtPosition.get().getTicker());
        assertEquals(numOfShares, boughtPosition.get().getNumOfShares());
        assertEquals(price, boughtPosition.get().getValuePaid(), 0.001);
    }

    @Test
    public void sellTest() {
        positionService.buy(ticker3, numOfShares, price);
        positionService.sell(ticker3);
        Optional<Position> soldPosition = positionDao.findById(ticker3);
        assertTrue(soldPosition.isEmpty());
    }

    @Test
    public void viewAllTest() {
        positionService.buy(ticker, numOfShares, price);
        positionService.buy(ticker2, numOfShares, price);

        Iterable<Position> positions = positionService.viewAll();
        assertTrue(positions.iterator().hasNext());
    }

    @Test
    public void viewTest() {
        positionService.buy(ticker2, numOfShares, price);
        Optional<Position> position = positionService.view(ticker2);
        assertTrue(position.isPresent());
    }
}
