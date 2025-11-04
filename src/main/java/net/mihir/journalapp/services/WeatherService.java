package net.mihir.journalapp.services; // Sonar wants this to be 'net.mihir.journalapp.services'

import net.mihir.journalapp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value; // <-- Import this
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String API_URL_TEMPLATE = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";


    private final RestTemplate restTemplate;
    private final String apiKey;

    public WeatherService(RestTemplate restTemplate,
                          @Value("${weather.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public WeatherResponse getWeather(String city){
        String finalAPI = API_URL_TEMPLATE.replace("CITY", city).replace("API_KEY", apiKey);

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }
}