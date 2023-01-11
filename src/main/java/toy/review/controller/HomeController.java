package toy.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import toy.review.domain.Member;
import toy.review.service.MemberService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    private final MemberService memberService;

    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(@CookieValue(name = "memberId", required = false) String memberId,
                       Model model) throws IOException {

        //요청 시각 조회
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int hour = now.getHour();
        int min = now.getMinute();
        if(min <= 30) { // 해당 시각 발표 전에는 자료가 없음 - 이전시각을 기준으로 해야함
            hour -= 1;
        }

        String hourStr = hour + "00"; // 정시 기준

        log.info("API 요청 발송 >>>  연월일: {}, 시각: {}", yyyyMMdd, hourStr);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=ivNP9N3WhsAtIQfRAFMkoItOO72%2B3vIu77yuIWqinqqL1sxddWgtHeAIrY9rp1WRkeqsz%2FSqtJ%2F1DJskkOrezg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("0", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
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
            // JSON 데이터를 파싱
            obj = (JSONObject)parser.parse(sb.toString());
        } catch (ParseException e) {
            System.out.println("변환에 실패");
            e.printStackTrace();
        }

        System.out.println(obj);
        System.out.println(obj.get("response").getClass().getName());//JSONObject
        System.out.println(obj.get("response"));

        // JSON 데이터로 부터 response 찾기
        JSONObject jsonObj_response = (JSONObject) obj.get("response");

        // response 로 부터 body 찾기
        JSONObject jsonObj_body = (JSONObject) jsonObj_response.get("body");

        // body 로 부터 items 찾기
        JSONObject jsonObj_items = (JSONObject) jsonObj_body.get("items");

        // items로 부터 item을  JSONArray로 받기
        JSONArray jsonArr_item = (JSONArray) jsonObj_items.get("item");



        String weather = null;

       for(int i=0; i<jsonArr_item.size(); i++) {
           jsonObj_items = (JSONObject) jsonArr_item.get(i);

           System.out.println(jsonObj_items);

           String fcstValue = (String) jsonObj_items.get("fcstValue");
           String category = (String) jsonObj_items.get("category");

           System.out.println(fcstValue);
           System.out.println(category);


           System.out.println("??????????????????????????"+weather);

           if (weather.equals(null)) {
               if (category.equals("SKY")) {

                   weather = "현재 날씨는 ";
                   if (fcstValue.equals("1")) {
                       weather += "맑은 상태로";
                   } else if (fcstValue.equals("2")) {
                       weather += "비가 오는 상태로 ";
                   } else if (fcstValue.equals("3")) {
                       weather += "구름이 많은 상태로 ";
                   } else if (fcstValue.equals("4")) {
                       weather += "흐린 상태로 ";
                   }
               }
           }


           String temperature = null;
           if (category.equals("T3H") || category.equals("T1H")) {
               temperature = "기온은 " + fcstValue + "℃ 입니다.";
           }

           System.out.println("WEATER_TAG" + weather + temperature);

       }




           if (memberId == null) {
               return "home";
           }

           // 쿠키에 대응되는 멤버 있는지 확인
           Optional<Member> loginMember = memberService.findOne(memberId);

           log.info("loginMember" + loginMember.get().getUser_id());

           if (loginMember == null) {
               return "home";
           }

           //정상 로그인
           String loginMemberName = loginMember.get().getUser_id();
           model.addAttribute("loginMemberName", loginMemberName);
           return "loginHome";
       }
}



