package utils;

import java.util.*;

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

    public static Map<String, List<String>> concepts = new HashMap<>();

    public static void setConcepts(List<String[]> lines) {
        for (String[] line : lines) {
            if (line.length == 1) continue;

            int end = line.length < 11 ? line.length : 11;

            List<String> temp = Arrays.asList(Arrays.copyOfRange(line, 1, end));
            List<String> conceptsList = new ArrayList<>(temp);
            if (conceptsList.contains("")) {
                conceptsList.remove("");
            }

            concepts.put(line[0], conceptsList);
        }
    }

}
