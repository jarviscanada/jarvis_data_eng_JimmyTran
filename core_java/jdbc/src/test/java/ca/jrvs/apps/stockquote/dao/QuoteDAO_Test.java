package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import org.junit.*;
import ca.jrvs.apps.stockquote.Quote;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.*;


public class QuoteDAO_Test {
    private DatabaseConnectionManager dcm;
    private Connection connection;
    private QuoteDao quoteDao;
    private PositionDao positionDao;
    private Quote testQuote1;
    private Quote testQuote2;
    private String ticker1;
    private String ticker2;
    private Timestamp testTimestamp;

    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        quoteDao = new QuoteDao(connection);
        positionDao = new PositionDao(connection);
        
        ticker1 = "TESTSTOCK1";
        ticker2 = "TESTSTOCK2";
        testTimestamp = new Timestamp(System.currentTimeMillis());

        //delete the positions in the positions table while the QuoteDao tests are run
        positionDao.deleteById(ticker1);
        positionDao.deleteById(ticker2);

        //populate quote table with dummy data
        testQuote1 = new Quote();
        testQuote1.setTicker(ticker1);
        testQuote1.setOpen(181.74);
        testQuote1.setHigh(182.43);
        testQuote1.setLow(180.0);
        testQuote1.setPrice(181.56);
        testQuote1.setVolume(49402449);
        testQuote1.setLatestTradingDay(new Date(124, 9, 10));
        testQuote1.setPreviousClose(182.31);
        testQuote1.setChange(-0.75);
        testQuote1.setChangePercent("-0.4114%");
        testQuote1.setTimestamp(testTimestamp);
        quoteDao.save(testQuote1);

        testQuote2 = new Quote();
        testQuote2.setTicker(ticker2);
        testQuote2.setOpen(281.74);
        testQuote2.setHigh(282.43);
        testQuote2.setLow(280.0);
        testQuote2.setPrice(281.56);
        testQuote2.setVolume(49402449);
        testQuote2.setLatestTradingDay(new Date(124, 9, 10));
        testQuote2.setPreviousClose(282.31);
        testQuote2.setChange(-0.75);
        testQuote2.setChangePercent("-0.4114%");
        testQuote2.setTimestamp(testTimestamp);
        quoteDao.save(testQuote2);
    }

    @After
    public void tearDown() throws SQLException {
        //after tests, wipe the dummy data
        quoteDao.deleteById(ticker1);
        quoteDao.deleteById(ticker2);
        connection.close();
    }

    @Test
    public void save() {
        try {
            Quote savedQuote = quoteDao.save(testQuote1);
            assertEquals(savedQuote, testQuote1);

            assertEquals(ticker1, testQuote1.getTicker());
            assertEquals(181.74, testQuote1.getOpen(), 0.001);
            assertEquals(182.43, testQuote1.getHigh(), 0.001);
            assertEquals(180.0, testQuote1.getLow(), 0.001);
            assertEquals(181.56, testQuote1.getPrice(), 0.001);
            assertEquals(49402449, testQuote1.getVolume(), 0.001);
            assertEquals(new Date(124, 9, 10), testQuote1.getLatestTradingDay());
            assertEquals(182.31, testQuote1.getPreviousClose(), 0.001);
            assertEquals(-0.75, testQuote1.getChange(), 0.001);
            assertEquals("-0.4114%", testQuote1.getChangePercent());
            assertEquals(testTimestamp, testQuote1.getTimestamp());

            quoteDao.deleteById(ticker1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findByID() {
        try {
            Optional<Quote> foundQuote = quoteDao.findById(ticker1);

            assertEquals(ticker1, foundQuote.get().getTicker());
            assertEquals(181.74, foundQuote.get().getOpen(), 0.001);
            assertEquals(182.43, foundQuote.get().getHigh(), 0.001);
            assertEquals(180.0, foundQuote.get().getLow(), 0.001);
            assertEquals(181.56, foundQuote.get().getPrice(), 0.001);
            assertEquals(49402449, foundQuote.get().getVolume(), 0.001);
            assertEquals(new Date(124, 9, 10), foundQuote.get().getLatestTradingDay());
            assertEquals(182.31, foundQuote.get().getPreviousClose(), 0.001);
            assertEquals(-0.75, foundQuote.get().getChange(), 0.001);
            assertEquals("-0.4114%", foundQuote.get().getChangePercent());
            assertEquals(testTimestamp, foundQuote.get().getTimestamp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findAll() {
        try {
            Iterable<Quote> quotes = quoteDao.findAll();
            boolean found1 = false;
            boolean found2 = false;

            for (Quote q : quotes) {
                if (q.getTicker().equals(ticker1)) {
                    found1 = true;
                    assertEquals(181.74, q.getOpen(), 0.001);
                    assertEquals(182.43, q.getHigh(), 0.001);
                    assertEquals(180.0, q.getLow(), 0.001);
                    assertEquals(181.56, q.getPrice(), 0.001);
                    assertEquals(49402449, q.getVolume(), 0.001);
                    assertEquals(new Date(124, 9, 10), q.getLatestTradingDay());
                    assertEquals(182.31, q.getPreviousClose(), 0.001);
                    assertEquals(-0.75, q.getChange(), 0.001);
                    assertEquals("-0.4114%", q.getChangePercent());
                    assertEquals(testTimestamp, q.getTimestamp());
                } else if (q.getTicker().equals(ticker2)) {
                    found2 = true;
                    assertEquals(281.74, q.getOpen(), 0.001);
                    assertEquals(282.43, q.getHigh(), 0.001);
                    assertEquals(280.0, q.getLow(), 0.001);
                    assertEquals(281.56, q.getPrice(), 0.001);
                    assertEquals(49402449, q.getVolume(), 0.001);
                    assertEquals(new Date(124, 9, 10), q.getLatestTradingDay());
                    assertEquals(282.31, q.getPreviousClose(), 0.001);
                    assertEquals(-0.75, q.getChange(), 0.001);
                    assertEquals("-0.4114%", q.getChangePercent());
                    assertEquals(testTimestamp, q.getTimestamp());
                }
            }

            assertTrue(found1);
            assertTrue(found2);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteById() {
        try {
            quoteDao.deleteById(ticker1);

            Optional<Quote> deletedQuote = quoteDao.findById(ticker1);
            assertFalse(deletedQuote.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteAll() {
        try {
            quoteDao.deleteAll();

            Optional<Quote> deletedQuote1 = quoteDao.findById(ticker1);
            Optional<Quote> deletedQuote2 = quoteDao.findById(ticker2);
            assertFalse(deletedQuote1.isPresent());
            assertFalse(deletedQuote2.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
