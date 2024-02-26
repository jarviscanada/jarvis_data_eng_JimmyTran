package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.services.PositionService;
import ca.jrvs.apps.stockquote.services.QuoteService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    static final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    static final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    public static void main(String[] args) {
        infoLogger.info("Main: Application Started");
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\JIMMY\\Documents\\GitHub\\Jarvis\\jarvis_data_eng_JimmyTran\\core_java\\jdbc\\src\\main\\resources\\properties.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            errorLogger.error("Main: File not found!" + e.getMessage());
        } catch (IOException e) {
            errorLogger.error("Main: IO Exception" + e.getMessage());

        }

        try {
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e) {
            errorLogger.error("Main: Class path not found!" + e.getMessage());

        }
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
        try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            QuoteDao qRepo = new QuoteDao(c);
            PositionDao pRepo = new PositionDao(c);
            QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
            QuoteService sQuote = new QuoteService(qRepo, rcon);
            PositionService sPos = new PositionService(pRepo, sQuote);
            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            con.initClient();
        } catch (SQLException e) {
            errorLogger.error("Main: SQL Exception!" + e.getMessage());
        }
    }

}