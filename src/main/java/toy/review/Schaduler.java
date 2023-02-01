package toy.review;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

@Component
public class Schaduler {
    @Scheduled(cron = "10 * * * * *")
    public void printDate () {
        System.out.println(new Date().toString());

        try {
            // Run script
            Process process = Runtime.getRuntime().exec("/home/ubuntu/app/git/./pull.sh");

            // Read output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            System.out.println(output.toString());
            if (output.toString().contains("Already up to date.")) {
                System.out.println("git 레파지토리 변경 사항 무");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
