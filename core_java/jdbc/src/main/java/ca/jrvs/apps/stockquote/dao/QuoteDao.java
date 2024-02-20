package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.Quote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
    private static final String GET_ONE = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote WHERE symbol = ?";
    private static final String GET_ALL = "SELECT * FROM quote";
    private static final String DELETE_ONE = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";

    public QuoteDao(Connection connection) {
        this.c = connection;
    }

    /**
     * Saves a given entity. Used for create and update
     *
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (this.findById(entity.getTicker()).isEmpty()) {
            try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {
                statement.setString(1, entity.getTicker());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, entity.getLatestTradingDay());
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getTimestamp());
                statement.execute();
                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
                statement.setDouble(1, entity.getOpen());
                statement.setDouble(2, entity.getHigh());
                statement.setDouble(3, entity.getLow());
                statement.setDouble(4, entity.getPrice());
                statement.setInt(5, entity.getVolume());
                statement.setDate(6, entity.getLatestTradingDay());
                statement.setDouble(7, entity.getPreviousClose());
                statement.setDouble(8, entity.getChange());
                statement.setString(9, entity.getChangePercent());
                statement.setTimestamp(10, entity.getTimestamp());
                statement.setString(11, entity.getTicker());
                statement.execute();
                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves an entity by its id
     *
     * @param s - must not be null
     * @return Entity with the given id or empty optional if none found
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = new Quote();
        try (PreparedStatement statement = this.c.prepareStatement(GET_ONE)) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
                return Optional.of(quote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    /**
     * Retrieves all entities
     *
     * @return All entities
     */
    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (PreparedStatement statement = this.c.prepareStatement(GET_ALL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Quote quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
                quotes.add(quote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return quotes;
    }

    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     *
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ONE)) {
            statement.setString(1, s);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao

}