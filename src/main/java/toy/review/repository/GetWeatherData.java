package toy.review.repository;

import toy.review.domain.WeatherDTO;

import java.io.IOException;
import java.util.List;

public interface GetWeatherData {

    WeatherDTO weatherFrcst() throws IOException;

}
