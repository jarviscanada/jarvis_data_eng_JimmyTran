package ca.jrvs.apps.stockquote;

import okhttp3.OkHttpClient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuoteHttpHelper_Test {
    private static final String apiKey = "bf054731femsh35fa8d48cc951a8p11d1d6jsnb77c1a86a057";

    @Test
    public void fetchQuoteInfo_Test() {
        QuoteHttpHelper quoteHttpHelper = new QuoteHttpHelper(apiKey, new OkHttpClient());
        Quote quote = quoteHttpHelper.fetchQuoteInfo("AAPL");
        assertEquals("AAPL", quote.getTicker());
    }
}
