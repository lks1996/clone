package toy.review;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Schaduler {
    @Scheduled(cron = "10 * * * * *")
    public void printDate () {
        System.out.println(new Date().toString());
    }

}
