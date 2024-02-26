package ca.jrvs.apps.stockquote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class QuoteHttpHelper {
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(final String apiKey, final OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     *
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        Quote quote = new Quote();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol
                        + "&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            JsonNode globalQuoteNode = jsonNode.get("Global Quote");

            if (globalQuoteNode == null || globalQuoteNode.isEmpty()) {
                return null;
            }
            quote = objectMapper.convertValue(globalQuoteNode, Quote.class);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            quote.setTimestamp(currentTimestamp);

            infoLogger.info("QuoteHttpHelper: Quote sucessfully retrieved from Alpha Vantage API");
            return quote;
        } catch (InterruptedException e) {
            errorLogger.error("QuoteHttpHelper: InterruptedException error: " + e.getMessage());
        } catch (JsonMappingException e) {
            errorLogger.error("QuoteHttpHelper: JsonMappingException error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            errorLogger.error("QuoteHttpHelper: JsonProcessingException error: " + e.getMessage());
        } catch (IOException e) {
            errorLogger.error("QuoteHttpHelper: IOException error: " + e.getMessage());

        }
        return quote;
    }

}