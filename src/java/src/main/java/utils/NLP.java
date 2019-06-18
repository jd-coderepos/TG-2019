package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class NLP {

    public static List<String> stopwords = new ArrayList<>();

    public static void setStopwords(String[] lines) {
        for (String line : lines) {
            line = line.trim();
            stopwords.add(line);
        }
    }

}
