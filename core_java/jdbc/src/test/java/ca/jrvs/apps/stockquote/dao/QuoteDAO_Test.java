package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
    }

    @After
    public void tearDown() throws SQLException {
        QuoteDao quoteDao = new QuoteDao(connection);
        quoteDao.deleteAll();
        connection.close();
    }

    @Test
    public void save() {
        QuoteDao quoteDao = new QuoteDao(connection);
        Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            Quote quote = new Quote();
            quote.setTicker("AAPL");
            quote.setOpen(181.74);
            quote.setHigh(182.43);
            quote.setLow(180.0);
            quote.setPrice(181.56);
            quote.setVolume(49402449);
            quote.setLatestTradingDay(new Date(124, 9, 10));
            quote.setPreviousClose(182.31);
            quote.setChange(-0.75);
            quote.setChangePercent("-0.4114%");
            quote.setTimestamp(testTimestamp);
            Quote savedQuote = quoteDao.save(quote);

            assertEquals(savedQuote, quote);

            assertEquals("AAPL", quote.getTicker());
            assertEquals(181.74, quote.getOpen(), 0.001);
            assertEquals(182.43, quote.getHigh(), 0.001);
            assertEquals(180.0, quote.getLow(), 0.001);
            assertEquals(181.56, quote.getPrice(), 0.001);
            assertEquals(49402449, quote.getVolume(), 0.001);
            assertEquals(new Date(124, 9, 10), quote.getLatestTradingDay());
            assertEquals(182.31, quote.getPreviousClose(), 0.001);
            assertEquals(-0.75, quote.getChange(), 0.001);
            assertEquals("-0.4114%", quote.getChangePercent());
            assertEquals(testTimestamp, quote.getTimestamp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findByID() {
        QuoteDao quoteDao = new QuoteDao(connection);
        Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            Quote quote = new Quote();
            quote.setTicker("AAPL");
            quote.setOpen(181.74);
            quote.setHigh(182.43);
            quote.setLow(180.0);
            quote.setPrice(181.56);
            quote.setVolume(49402449);
            quote.setLatestTradingDay(new Date(124, 9, 10));
            quote.setPreviousClose(182.31);
            quote.setChange(-0.75);
            quote.setChangePercent("-0.4114%");
            quote.setTimestamp(testTimestamp);
            quoteDao.save(quote);

            Optional<Quote> savedQuote = quoteDao.findById("AAPL");

            assertEquals("AAPL", savedQuote.get().getTicker());
            assertEquals(181.74, savedQuote.get().getOpen(), 0.001);
            assertEquals(182.43, savedQuote.get().getHigh(), 0.001);
            assertEquals(180.0, savedQuote.get().getLow(), 0.001);
            assertEquals(181.56, savedQuote.get().getPrice(), 0.001);
            assertEquals(49402449, savedQuote.get().getVolume(), 0.001);
            assertEquals(new Date(124, 9, 10), savedQuote.get().getLatestTradingDay());
            assertEquals(182.31, savedQuote.get().getPreviousClose(), 0.001);
            assertEquals(-0.75, savedQuote.get().getChange(), 0.001);
            assertEquals("-0.4114%", savedQuote.get().getChangePercent());
            assertEquals(testTimestamp, savedQuote.get().getTimestamp());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findAll() {
        QuoteDao quoteDao = new QuoteDao(connection);
        Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            Quote quote = new Quote();
            quote.setTicker("AAPL");
            quote.setOpen(181.74);
            quote.setHigh(182.43);
            quote.setLow(180.0);
            quote.setPrice(181.56);
            quote.setVolume(49402449);
            quote.setLatestTradingDay(new Date(124, 9, 10));
            quote.setPreviousClose(182.31);
            quote.setChange(-0.75);
            quote.setChangePercent("-0.4114%");
            quote.setTimestamp(testTimestamp);
            quoteDao.save(quote);

            Quote quote2 = new Quote();
            quote2.setTicker("MSFT");
            quote2.setOpen(281.74);
            quote2.setHigh(282.43);
            quote2.setLow(280.0);
            quote2.setPrice(281.56);
            quote2.setVolume(49402449);
            quote2.setLatestTradingDay(new Date(124, 9, 10));
            quote2.setPreviousClose(282.31);
            quote2.setChange(-0.75);
            quote2.setChangePercent("-0.4114%");
            quote2.setTimestamp(testTimestamp);
            quoteDao.save(quote2);

            Iterable<Quote> quotes = quoteDao.findAll();
            boolean found1 = false;
            boolean found2 = false;

            for (Quote q : quotes) {
                if (q.getTicker().equals("AAPL")) {
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
                } else if (q.getTicker().equals("MSFT")) {
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
        Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
        QuoteDao quoteDao = new QuoteDao(connection);
        try {
            Quote quote = new Quote();
            quote.setTicker("AAPL");
            quote.setOpen(181.74);
            quote.setHigh(182.43);
            quote.setLow(180.0);
            quote.setPrice(181.56);
            quote.setVolume(49402449);
            quote.setLatestTradingDay(new Date(124, 9, 10));
            quote.setPreviousClose(182.31);
            quote.setChange(-0.75);
            quote.setChangePercent("-0.4114%");
            quote.setTimestamp(testTimestamp);
            quoteDao.save(quote);

            quoteDao.deleteById("AAPL");

            Optional<Quote> deletedQuote = quoteDao.findById("AAPL");
            assertFalse(deletedQuote.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteAll() {
        QuoteDao quoteDao = new QuoteDao(connection);
        Timestamp testTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            Quote quote = new Quote();
            quote.setTicker("AAPL");
            quote.setOpen(181.74);
            quote.setHigh(182.43);
            quote.setLow(180.0);
            quote.setPrice(181.56);
            quote.setVolume(49402449);
            quote.setLatestTradingDay(new Date(124, 9, 10));
            quote.setPreviousClose(182.31);
            quote.setChange(-0.75);
            quote.setChangePercent("-0.4114%");
            quote.setTimestamp(testTimestamp);
            quoteDao.save(quote);

            Quote quote2 = new Quote();
            quote2.setTicker("MSFT");
            quote2.setOpen(281.74);
            quote2.setHigh(282.43);
            quote2.setLow(280.0);
            quote2.setPrice(281.56);
            quote2.setVolume(49402449);
            quote2.setLatestTradingDay(new Date(124, 9, 10));
            quote2.setPreviousClose(282.31);
            quote2.setChange(-0.75);
            quote2.setChangePercent("-0.4114%");
            quote2.setTimestamp(testTimestamp);
            quoteDao.save(quote2);

            quoteDao.deleteAll();

            Optional<Quote> deletedQuote1 = quoteDao.findById("AAPL");
            Optional<Quote> deletedQuote2 = quoteDao.findById("MSFT");
            assertFalse(deletedQuote1.isPresent());
            assertFalse(deletedQuote2.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
