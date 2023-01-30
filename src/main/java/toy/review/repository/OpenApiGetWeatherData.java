package toy.review.repository;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import toy.review.domain.WeatherDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class OpenApiGetWeatherData implements GetWeatherData {

    @Override
    public WeatherDTO weatherFrcst() throws IOException {
        //요청 시각 조회
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int hour = now.getHour();
        int min = now.getMinute();
        if(min <= 30) { // 해당 시각 발표 전에는 자료가 없음 - 이전시각을 기준으로 해야함
            hour -= 1;
        }

        String hourStr = hour + "00"; // 정시 기준

        //log.info("API 요청 발송 >>>  연월일: {}, 시각: {}", yyyyMMdd, hourStr);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=ivNP9N3WhsAtIQfRAFMkoItOO72%2B3vIu77yuIWqinqqL1sxddWgtHeAIrY9rp1WRkeqsz%2FSqtJ%2F1DJskkOrezg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&").append(URLEncoder.encode("nx", "UTF-8")).append("=").append(URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try {
            // JSON 데이터 파싱
            obj = (JSONObject)parser.parse(sb.toString());
        } catch (ParseException e) {
            System.out.println("변환에 실패");
            e.printStackTrace();
        }

        // JSON 데이터로 부터 response 찾기
        JSONObject jsonObj_response = (JSONObject) obj.get("response");

        // response 로 부터 body 찾기
        JSONObject jsonObj_body = (JSONObject) jsonObj_response.get("body");

        // body 로 부터 items 찾기
        JSONObject jsonObj_items = (JSONObject) jsonObj_body.get("items");

        // items로 부터 item을  JSONArray로 받기
        JSONArray jsonArr_item = (JSONArray) jsonObj_items.get("item");

        System.out.println("jsonArr_item.size() : " + jsonArr_item.size());



        JSONArray itemList_sky = new JSONArray(); //하늘 상태 예보
        JSONArray itemList_temp = new JSONArray(); //기온 예보
        JSONArray itemList_pty = new JSONArray(); //강수형태 예보
        JSONArray itemList_rn1 = new JSONArray(); //1시간 강수 예보
        JSONArray itemList_lgt = new JSONArray(); //낙뢰예보
        JSONArray itemList_reh = new JSONArray(); //습도 예보
        JSONArray itemList_vec = new JSONArray(); //풍향 예보
        JSONArray itemList_wsd = new JSONArray(); //풍속 예보


        /* 추후에 시간대 별 날씨 데이터를 사용하 수 있으므로 미리 항목 별로 분할함. */
        for(int i=0; i<jsonArr_item.size(); i++) {
            jsonObj_items = (JSONObject) jsonArr_item.get(i);
            String category = (String) jsonObj_items.get("category");

//            switch (category) {
//                case "SKY" -> itemList_sky.add(jsonObj_items);
//                case "T1H" -> itemList_temp.add(jsonObj_items);
//                case "PTY" -> itemList_pty.add(jsonObj_items);
//                case "RN1" -> itemList_rn1.add(jsonObj_items);
//                case "LGT" -> itemList_lgt.add(jsonObj_items);
//                case "REH" -> itemList_reh.add(jsonObj_items);
//                case "VEC" -> itemList_vec.add(jsonObj_items);
//                case "WSD" -> itemList_wsd.add(jsonObj_items);
//            }

            switch (category) {
                case "SKY": itemList_sky.add(jsonObj_items);
                case "T1H": itemList_temp.add(jsonObj_items);
                case "PTY": itemList_pty.add(jsonObj_items);
                case "RN1": itemList_rn1.add(jsonObj_items);
                case "LGT" : itemList_lgt.add(jsonObj_items);
                case "REH" : itemList_reh.add(jsonObj_items);
                case "VEC" : itemList_vec.add(jsonObj_items);
                case "WSD" : itemList_wsd.add(jsonObj_items);
            }
        }

        JSONObject currentSky = (JSONObject) itemList_sky.get(0);
        JSONObject currentTemp = (JSONObject) itemList_temp.get(0);
        JSONObject currentPty = (JSONObject) itemList_pty.get(0);
        JSONObject currentRn1 = (JSONObject) itemList_rn1.get(0);
        JSONObject currentLgt = (JSONObject) itemList_lgt.get(0);
        JSONObject currentReh = (JSONObject) itemList_reh.get(0);
        JSONObject currentVec = (JSONObject) itemList_vec.get(0);
        JSONObject currentWsd = (JSONObject) itemList_wsd.get(0);


        WeatherDTO weatherDTO = new WeatherDTO();

        weatherDTO.setSky(Long.valueOf((String) currentSky.get("fcstValue")));
        weatherDTO.setTemp(Long.valueOf((String) currentTemp.get("fcstValue")));
        weatherDTO.setPty(Long.valueOf((String)currentPty.get("fcstValue")));
        weatherDTO.setRn1(String.valueOf(currentRn1.get("fcstValue")));
        weatherDTO.setLgt(Long.valueOf((String) currentLgt.get("fcstValue")));
        weatherDTO.setReh(Long.valueOf((String) currentReh.get("fcstValue")));
        weatherDTO.setVec(Long.valueOf((String) currentVec.get("fcstValue")));
        weatherDTO.setWsd(Long.valueOf((String) currentWsd.get("fcstValue")));

        return weatherDTO;

    }
}
