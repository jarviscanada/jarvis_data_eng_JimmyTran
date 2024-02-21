package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PositionService_UnitTest {
    @Mock
    private PositionDao mockPositionDao;
    @InjectMocks
    private PositionService positionService;

    @Mock
    private QuoteDao mockQuoteDao;

    @Test
    public void buy_Pass() {
        String ticker = "AAPL";
        int numOfShares = 5;
        double price = 150.0;

        Quote mockQuote = new Quote();
        mockQuote.setVolume(10);
        when(mockQuoteDao.findById(ticker)).thenReturn(Optional.of(mockQuote));

        Position mockPosition = new Position();
        when(mockPositionDao.save(any(Position.class))).thenReturn(mockPosition);

        Position result = positionService.buy(ticker, numOfShares, price);

        verify(mockQuoteDao).findById(ticker);
        verify(mockPositionDao).save(any());
        assertEquals(result, mockPosition);

    }

    @Test(expected = IllegalArgumentException.class)
    public void buy_Fail_TooManyShares() {
        //You cant buy more shares than the volume has.
        String ticker = "AAPL";
        int numOfShares = 15; //Shares now exceed volume
        double price = 150.0;

        Quote mockQuote = new Quote();
        mockQuote.setVolume(10); //insufficient volume
        when(mockQuoteDao.findById(ticker)).thenReturn(Optional.of(mockQuote));

        Position mockPosition = new Position();
        when(mockPositionDao.save(any(Position.class))).thenReturn(mockPosition);

        positionService.buy(ticker, numOfShares, price);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buy_Fail_TickerDoesntExist() {
        String ticker = "AAPL";
        int numberOfShares = 5;
        double price = 150.0;

        when(mockQuoteDao.findById(ticker)).thenReturn(Optional.empty());

        positionService.buy(ticker, numberOfShares, price);
    }

    @Test
    public void sell_Pass() {
        String ticker = "AAPL";

        when(mockPositionDao.findById(ticker)).thenReturn(Optional.of(new Position()));

        positionService.sell(ticker);

        verify(mockPositionDao).deleteById(ticker);
    }

    @Test
    public void sell_Fail() {
        String ticker = "AAPL";

        when(mockPositionDao.findById(ticker)).thenReturn(Optional.empty());

        positionService.sell(ticker);

        verify(mockPositionDao, times(0)).deleteById(ticker);
    }


}
