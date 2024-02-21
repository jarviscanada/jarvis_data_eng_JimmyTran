package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.Position;
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

    @Before
    public void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
    }

    @After
    public void tearDown() throws SQLException {
        PositionDao positionDao = new PositionDao(connection);
        positionDao.deleteAll();
        connection.close();
    }

    @Test
    public void save() {
        PositionDao positionDao = new PositionDao(connection);
        try {
            Position position = new Position();
            position.setTicker("AAPL");
            position.setNumOfShares(10);
            position.setValuePaid(20.50);
            Position savedPosition = positionDao.save(position);

            assertEquals(savedPosition, position);

            assertEquals("AAPL", position.getTicker());
            assertEquals(20.50, position.getValuePaid(), 0.001);
            assertEquals(10, position.getNumOfShares());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findByID() {
        PositionDao positionDao = new PositionDao(connection);
        try {
            Position position = new Position();
            position.setTicker("AAPL");
            position.setValuePaid(30.50);
            position.setNumOfShares(15);
            positionDao.save(position);

            Optional<Position> foundPosition = positionDao.findById("AAPL");

            assertTrue(foundPosition.isPresent());
            assertEquals("AAPL", foundPosition.get().getTicker());
            assertEquals(30.50, foundPosition.get().getValuePaid(), 0.001);
            assertEquals(15, foundPosition.get().getNumOfShares());

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findAll() {
        PositionDao positionDao = new PositionDao(connection);

        try {
            Position position = new Position();
            position.setTicker("AAPL");
            position.setValuePaid(40.50);
            position.setNumOfShares(20);
            positionDao.save(position);

            Position position2 = new Position();
            position2.setTicker("MSFT");
            position2.setValuePaid(50.50);
            position2.setNumOfShares(30);
            positionDao.save(position2);

            Iterable<Position> positions = positionDao.findAll();
            boolean found1 = false;
            boolean found2 = false;

            for (Position p : positions) {
                if (p.getTicker().equals("AAPL")) {
                    found1 = true;
                    assertEquals(40.50, p.getValuePaid(), 0.001);
                    assertEquals(20, p.getNumOfShares());
                } else if (p.getTicker().equals("MSFT")) {
                    found2 = true;
                    assertEquals(50.50, p.getValuePaid(), 0.001);
                    assertEquals(30, p.getNumOfShares());
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
        PositionDao positionDao = new PositionDao(connection);
        try {
            Position position = new Position();
            position.setTicker("AAPL");
            position.setValuePaid(50.50);
            position.setNumOfShares(25);
            positionDao.save(position);

            positionDao.deleteById("AAPL");

            Optional<Position> deletedPosition = positionDao.findById("AAPL");
            assertFalse(deletedPosition.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteAll() {
        PositionDao positionDao = new PositionDao(connection);

        try {
            Position position = new Position();
            position.setTicker("AAPL");
            position.setValuePaid(40.50);
            position.setNumOfShares(20);
            positionDao.save(position);

            Position position2 = new Position();
            position2.setTicker("MSFT");
            position2.setValuePaid(50.50);
            position2.setNumOfShares(30);
            positionDao.save(position2);

            positionDao.deleteAll();

            Optional<Position> deletedPosition1 = positionDao.findById("AAPL");
            Optional<Position> deletedPosition2 = positionDao.findById("MSFT");
            assertFalse(deletedPosition1.isPresent());
            assertFalse(deletedPosition2.isPresent());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
