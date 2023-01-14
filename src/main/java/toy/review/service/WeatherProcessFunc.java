package toy.review.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import toy.review.domain.WeatherDTO;
import toy.review.repository.GetWeatherData;

import java.io.IOException;

@Slf4j
@Component
public class WeatherProcessFunc {
    private final GetWeatherData getWeatherData;

    public WeatherProcessFunc(GetWeatherData getWeatherData) {
        this.getWeatherData = getWeatherData;
    }

    public String nowWeatherProcessedData() throws IOException {
        WeatherDTO nowWeatherDTO = getWeatherData.weatherFrcst();

        log.info(String.valueOf(nowWeatherDTO));
        System.out.println("nowWeatherDTO.getSky() : " + nowWeatherDTO.getSky());
        System.out.println("nowWeatherDTO.getTemp() : " + nowWeatherDTO.getTemp());
        System.out.println("nowWeatherDTO.getPty() : " + nowWeatherDTO.getPty());
        System.out.println("nowWeatherDTO.getRn1() : " + nowWeatherDTO.getRn1());
        System.out.println("nowWeatherDTO.getLgt() : " + nowWeatherDTO.getLgt());
        System.out.println("nowWeatherDTO.getReh() : " + nowWeatherDTO.getReh());
        System.out.println("nowWeatherDTO.getVec() : " + nowWeatherDTO.getVec());
        System.out.println("nowWeatherDTO.getWsd() : " + nowWeatherDTO.getWsd());



        return null;
    }
}