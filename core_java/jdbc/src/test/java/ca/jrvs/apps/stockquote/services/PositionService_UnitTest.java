package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Position;
import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.Before;
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
    @Mock
    private QuoteService mockQuoteService;
    @InjectMocks
    private PositionService positionService;

    private String ticker;
    private int numOfShares;
    private double price;
    private Position mockPosition;
    private Quote mockQuote;

    @Before
    public void setUp() {
        ticker = "AAPL";
        numOfShares = 5;
        price = 150.0;
        mockQuote = new Quote();
        mockQuote.setVolume(10);
        mockPosition = new Position();
        mockPosition.setNumOfShares(10);
        mockPosition.setValuePaid(150.0);
    }

    @Test
    public void buy_Pass() {
        when(mockQuoteService.fetchQuoteDataFromAPI(ticker)).thenReturn(Optional.of(mockQuote));
        when(mockPositionDao.save(any(Position.class))).thenReturn(mockPosition);

        Position result = positionService.buy(ticker, numOfShares, price);

        verify(mockQuoteService).fetchQuoteDataFromAPI(ticker);
        verify(mockPositionDao).save(any());
        assertEquals(result, mockPosition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buy_Fail_TooManyShares() {
        when(mockQuoteService.fetchQuoteDataFromAPI(ticker)).thenReturn(Optional.of(mockQuote));

        //You cant buy more shares than the volume has.
        int numOfShares = 15; //Shares now exceed volume

        positionService.buy(ticker, numOfShares, price);
    }

    @Test(expected = IllegalArgumentException.class)
    public void buy_Fail_TickerDoesntExist() {
        when(mockQuoteService.fetchQuoteDataFromAPI(ticker)).thenReturn(Optional.empty());

        positionService.buy(ticker, numOfShares, price);
    }

    @Test
    public void sell_Pass() {
        when(mockPositionDao.findById(ticker)).thenReturn(Optional.of(new Position()));

        positionService.sell(ticker);

        verify(mockPositionDao).deleteById(ticker);
    }

    @Test
    public void sell_Fail() {
        when(mockPositionDao.findById(ticker)).thenReturn(Optional.empty());

        positionService.sell(ticker);

        verify(mockPositionDao, times(0)).deleteById(ticker);
    }

    @Test
    public void testView_ValidTicker() {
        when(mockPositionDao.findById(ticker)).thenReturn(Optional.of(mockPosition));

        Optional<Position> result = positionService.view(ticker);

        assertEquals(Optional.of(mockPosition), result);
        verify(mockPositionDao).findById(ticker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void view_Fail_InvalidTicker() {
        when(mockPositionDao.findById(ticker)).thenReturn(Optional.empty());

        positionService.view(ticker);
    }

    @Test
    public void testViewAll() {
        positionService.viewAll();

        verify(mockPositionDao).findAll();
    }
}
