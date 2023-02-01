package toy.review;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Schaduler {
    @Scheduled(cron = "10 * * * * *")
    public void printDate () {
        System.out.println(new Date().toString());

        try {
            // Run script
            Process process = Runtime.getRuntime().exec("/home/ubuntu/app/git/./deploy.sh");

//            // Read output
//            StringBuilder output = new StringBuilder();
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line);
//            }
//
//            System.out.println(output.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
