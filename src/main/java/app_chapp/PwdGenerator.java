package app_chapp;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by Igor Konovalov on 19.11.2017.
 */
public class PwdGenerator implements Generator {

//    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
//    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//    private static final String DIGITS = "0123456789";
//    private static final String PUNCTUATION = "#$*_.";

    /*
    this method generates password 8 symbols length with numeric and alphabetical characters
     */
    public String generate() {
        String result = RandomStringUtils.random(8,true,true);
        return result;
    }
}
