package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.IexQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MarketDataDao extends JpaRepository<IexQuote, String> {
}