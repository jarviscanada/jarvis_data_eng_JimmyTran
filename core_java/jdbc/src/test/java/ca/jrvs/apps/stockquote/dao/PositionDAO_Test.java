package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class PositionDAO_Test {
    private DatabaseConnectionManager dcm;
    private Connection connection;
    private PositionDao positionDao;
    private QuoteDao quoteDao;
    private Position testPosition1;
    private Position testPosition2;
    private String ticker1;
    private String ticker2;
    private Timestamp testTimestamp;
    private Quote testQuote1;
    private Quote testQuote2;


    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        positionDao = new PositionDao(connection);
        quoteDao = new QuoteDao(connection);

        ticker1 = "TESTSTOCK1";
        ticker2 = "TESTSTOCK2";
        testTimestamp = new Timestamp(System.currentTimeMillis());

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

        testPosition1 = new Position();
        testPosition1.setTicker(ticker1);
        testPosition1.setNumOfShares(10);
        testPosition1.setValuePaid(20.50);
        positionDao.save(testPosition1);

        testPosition2 = new Position();
        testPosition2.setTicker(ticker2);
        testPosition2.setNumOfShares(30);
        testPosition2.setValuePaid(40.50);
        positionDao.save(testPosition2);
    }

    @After
    public void tearDown() throws SQLException {
        positionDao.deleteById(ticker1);
        positionDao.deleteById(ticker2);
        quoteDao.deleteById(ticker1);
        quoteDao.deleteById(ticker2);
        connection.close();
    }

    @Test
    public void save() {
        try {

            Optional<Quote> quote = quoteDao.findById(testPosition1.getTicker());
            if (!quote.isPresent()) {
                // Handle the case where the quote does not exist (skip the test or handle it accordingly)
                System.out.println("Skipping test because the corresponding quote does not exist.");
                return;
            }
            Position savedPosition = positionDao.save(testPosition1);
            assertEquals(savedPosition, testPosition1);

            assertEquals(ticker1, testPosition1.getTicker());
            assertEquals(10, testPosition1.getNumOfShares());
            assertEquals(20.50, testPosition1.getValuePaid(), 0.001);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findByID() {
        try {
            Optional<Position> foundPosition = positionDao.findById(ticker1);

            assertTrue(foundPosition.isPresent());
            assertEquals(ticker1, foundPosition.get().getTicker());
            assertEquals(10, foundPosition.get().getNumOfShares());
            assertEquals(20.50, foundPosition.get().getValuePaid(), 0.001);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findAll() {
        try {
            Iterable<Position> positions = positionDao.findAll();
            boolean found1 = false;
            boolean found2 = false;

            for (Position p : positions) {
                if (p.getTicker().equals(ticker1)) {
                    found1 = true;
                    assertEquals(10, p.getNumOfShares());
                    assertEquals(20.50, p.getValuePaid(), 0.001);
                } else if (p.getTicker().equals(ticker2)) {
                    found2 = true;
                    assertEquals(30, p.getNumOfShares());
                    assertEquals(40.50, p.getValuePaid(), 0.001);
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
            positionDao.deleteById(ticker1);

            Optional<Position> deletedPosition = positionDao.findById(ticker1);
            assertFalse(deletedPosition.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteAll() {
        try {
            positionDao.deleteAll();

            Optional<Position> deletedPosition1 = positionDao.findById(ticker1);
            Optional<Position> deletedPosition2 = positionDao.findById(ticker2);
            assertFalse(deletedPosition1.isPresent());
            assertFalse(deletedPosition2.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
