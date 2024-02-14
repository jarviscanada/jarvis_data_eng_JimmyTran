package ca.jrvs.apps.stockquote.dao;

import java.sql.Date;
import java.sql.Timestamp;

public class Quote {

    private String ticker; //id
    private double open;
    private double high;
    private double low;
    private double price;
    private int volume;
    private Date latestTradingDay;
    private double previousClose;
    private double change;
    private String changePercent;
    private Timestamp timestamp; //time when the info was pulled

}
