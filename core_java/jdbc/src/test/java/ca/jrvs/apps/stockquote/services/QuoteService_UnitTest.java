package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.Quote;
import ca.jrvs.apps.stockquote.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuoteService_UnitTest {
    @Mock
    private QuoteDao mockQuoteDao;
    @Mock
    private QuoteHttpHelper mockHttpHelper;
    @InjectMocks
    private QuoteService mockQuoteService;

    @Test
    public void fetchQuoteDataFromAPI_Pass() {
        String ticker = "AAPL";
        Quote mockQuote = new Quote();

        when(mockHttpHelper.fetchQuoteInfo(ticker)).thenReturn(mockQuote);

        Optional<Quote> result = mockQuoteService.fetchQuoteDataFromAPI(ticker);

        verify(mockHttpHelper).fetchQuoteInfo(ticker);
        verify(mockQuoteDao).save(mockQuote);
        assertEquals(Optional.of(mockQuote), result);
    }

    @Test(expected = NoSuchElementException.class)
    public void fetchQuoteDataFromAPI_Fail_TickerDoesntExist() {
        String nonExistentTicker = "INVALIDSTOCKTICKER";
        when(mockHttpHelper.fetchQuoteInfo(nonExistentTicker)).thenReturn(null);

        mockQuoteService.fetchQuoteDataFromAPI(nonExistentTicker);
    }
}
