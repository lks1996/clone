package toy.review.repository;

import toy.review.domain.WeatherDTO;

import java.io.IOException;

public interface GetWeatherData {

    WeatherDTO weatherFrcst() throws IOException;

}
