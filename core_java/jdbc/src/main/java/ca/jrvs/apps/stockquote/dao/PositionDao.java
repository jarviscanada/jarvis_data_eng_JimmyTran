package ca.jrvs.apps.stockquote.dao;


import ca.jrvs.apps.stockquote.Position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ?, WHERE symbol = ?";
    private static final String GET_ONE = "SELECT symbol, number_of_shares, value_paid FROM position WHERE symbol = ?";
    private static final String GET_ALL = "SELECT * FROM position";
    private static final String DELETE_ONE = "DELETE FROM position WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM position";
    private Connection c;

    public PositionDao(Connection connection) {
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
    public Position save(Position entity) throws IllegalArgumentException {
        if (this.findById(entity.getTicker()).isEmpty()) {
            try (PreparedStatement statement = this.c.prepareStatement(INSERT);) {
                statement.setString(1, entity.getTicker());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());
                statement.execute();
                return entity;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
                statement.setInt(1, entity.getNumOfShares());
                statement.setDouble(2, entity.getValuePaid());
                statement.setString(3, entity.getTicker());
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
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        Position position = new Position();
        try (PreparedStatement statement = this.c.prepareStatement(GET_ONE)) {
            statement.setString(1, s);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("num_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                return Optional.of(position);
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
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();
        try (PreparedStatement statement = this.c.prepareStatement(GET_ALL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Position position = new Position();
                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("num_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                positions.add(position);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return positions;
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