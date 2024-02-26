package ca.jrvs.apps.stockquote.dao;


import ca.jrvs.apps.stockquote.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";
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
                infoLogger.info("PositionDao: Position has been saved {}", entity);
                return entity;
            } catch (SQLException e) {
                errorLogger.error("PositionDao: Failed to save position {}" + e.getMessage());

            }
        } else {
            try (PreparedStatement statement = this.c.prepareStatement(UPDATE);) {
                statement.setInt(1, entity.getNumOfShares());
                statement.setDouble(2, entity.getValuePaid());
                statement.setString(3, entity.getTicker());
                statement.execute();
                infoLogger.info("PositionDao: Position has been updated {}", entity);
                return entity;
            } catch (SQLException e) {
                errorLogger.error("PositionDao: Failed to save position {}" + e.getMessage());

            }
        }
        return entity;
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
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                infoLogger.info("PositionDao: Position has been found {}", s, position);
                return Optional.of(position);
            }
        } catch (SQLException e) {
            errorLogger.error("PositionDao: Failed to find position " + s + e.getMessage());

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
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));
                positions.add(position);
                infoLogger.info("PositionDao: Positions have been found {}", positions);

            }
        } catch (SQLException e) {
            errorLogger.error("PositionDao: Failed to find any positions" + e.getMessage());

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
            infoLogger.info("PositionDao: Position has been deleted {}", s);
        } catch (SQLException e) {
            errorLogger.error("PositionDao: Failed to delete position " + s + e.getMessage());

        }
    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.c.prepareStatement(DELETE_ALL);) {
            statement.execute();
            infoLogger.info("PositionDao: All positions has been deleted");
        } catch (SQLException e) {
            errorLogger.error("PositionDao: Failed to delted all positions" + e.getMessage());

        }
    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao

}