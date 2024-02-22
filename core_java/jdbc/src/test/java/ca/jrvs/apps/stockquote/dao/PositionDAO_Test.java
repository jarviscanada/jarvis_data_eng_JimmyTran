package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class PositionDAO_Test {
    private DatabaseConnectionManager dcm;
    private Connection connection;
    private PositionDao positionDao;
    private Position originalAAPLState;
    private Position originalMSFTState;
    private Position testPosition1;
    private Position testPosition2;
    private String ticker1;
    private String ticker2;

    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        positionDao = new PositionDao(connection);

        ticker1 = "AAPL";
        ticker2 = "MSFT";

        originalAAPLState = positionDao.findById(ticker1).orElse(null);
        originalMSFTState = positionDao.findById(ticker2).orElse(null);

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

        if (originalAAPLState != null) {
            positionDao.save(originalAAPLState);
        }
        if (originalMSFTState != null) {
            positionDao.save(originalMSFTState);
        }
        connection.close();
    }

    @Test
    public void save() {
        try {
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
