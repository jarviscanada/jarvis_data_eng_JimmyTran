package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.QuoteDao;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExecutor {

    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "stock_quote", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            QuoteDao quoteDao = new QuoteDao(connection);
            QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper();
            Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

            //save new stock(CREATE UPDATE)
            Quote stock = quoteDao.save(quote);
            System.out.println(stock);
            //find single stock(READ)
            // Optional<Quote> testStock = quoteDao.findById("AAPL");
            // System.out.println(testStock);
            //print all(READ)
            //quoteDao.findAll().forEach(System.out::println);
            //DELETE STOCK(DELETE)
            //quoteDao.deleteById("MSFT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}