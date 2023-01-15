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

    /** 현재 날씨 예보 (return을 String이 아닌 다른 값으로 변경하여 사용가능. 현재는 간이용)**/
    public String nowWeatherData() throws IOException {
        WeatherDTO nowWeatherDTO = getWeatherData.weatherFrcst();
        String nowWeather = "";

        log.info(String.valueOf(nowWeatherDTO));
        System.out.println("nowWeatherDTO.getSky() : " + nowWeatherDTO.getSky());
        System.out.println("nowWeatherDTO.getTemp() : " + nowWeatherDTO.getTemp());
        System.out.println("nowWeatherDTO.getPty() : " + nowWeatherDTO.getPty());
        System.out.println("nowWeatherDTO.getRn1() : " + nowWeatherDTO.getRn1());
        System.out.println("nowWeatherDTO.getLgt() : " + nowWeatherDTO.getLgt());
        System.out.println("nowWeatherDTO.getReh() : " + nowWeatherDTO.getReh());
        System.out.println("nowWeatherDTO.getVec() : " + nowWeatherDTO.getVec());
        System.out.println("nowWeatherDTO.getWsd() : " + nowWeatherDTO.getWsd());


        if (nowWeatherDTO.getSky() == 1) {
            nowWeather += "오늘 하늘은 맑음!";
        } else if (nowWeatherDTO.getSky() == 2) {
            nowWeather += "오늘 하늘은 구름 조금!";
        } else if (nowWeatherDTO.getSky() == 3) {
            nowWeather += "오늘 하늘은 구름 많음!";
        } else if (nowWeatherDTO.getSky() == 4) {
            nowWeather += "오늘 하늘은 흐림!";
        }

        nowWeather += "현재 기온 " + nowWeatherDTO.getTemp() + "℃로,";

        if (nowWeatherDTO.getPty() == 0) {
            nowWeather += "강수 없음~!";
        } else if (nowWeatherDTO.getPty() == 1) {
            nowWeather += "비가 옵니다.";
        } else if (nowWeatherDTO.getPty() == 2) {
            nowWeather += "비와 눈이 옵니다.";
        } else if (nowWeatherDTO.getPty() == 3) {
            nowWeather += "눈이 옵니다.";
        } else if (nowWeatherDTO.getPty() == 5) {
            nowWeather += "빗방울이 조금 내립니다.";
        } else if (nowWeatherDTO.getPty() == 6) {
            nowWeather += "빗방울눈날림!";
        } else if (nowWeatherDTO.getPty() == 7) {
            nowWeather += "눈날림";
        }


        return nowWeather;
    }

    /** 하루 날씨 예보 (추후 업데이트)**/
    public void todayWeatherData() {
    }
}