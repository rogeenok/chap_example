package app_chapp;

import java.util.Random;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class ChallengeGenerator implements Generator {

    /*
    this method was implemented by project's author
     */
    public String generate() {
        Random random = new Random();
        int current = 8;

        // value will be from 8 to 128
        for (int i = 0; i < 3; i++) {
            current = current + 8 * random.nextInt(6);
        }

        String result = new String(Integer.valueOf(current).toString());

        while (result.length() < 3) {
            result = "0" + result;
        }

        return result;
    }
}
