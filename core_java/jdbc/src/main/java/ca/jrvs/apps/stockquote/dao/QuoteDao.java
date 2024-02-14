package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

    private Connection c;

    /**
     * Saves a given entity. Used for create and update
     *
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        return null;
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
        return Optional.empty();
    }

    /**
     * Retrieves all entities
     *
     * @return All entities
     */
    @Override
    public Iterable<Quote> findAll() {
        return null;
    }

    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     *
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {

    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {

    }

    //implement all inherited methods
    //you are not limited to methods defined in CrudDao

}