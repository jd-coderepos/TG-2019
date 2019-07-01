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

            int end = line.length < 51 ? line.length : 51;

            List<String> temp = Arrays.asList(Arrays.copyOfRange(line, 1, end));
            List<String> conceptsList = new ArrayList<>(temp);
            if (conceptsList.contains("")) {
                conceptsList.remove("");
            }

            concepts.put(line[0], conceptsList);
        }
    }

    public static Map<String, Map<String, List<String>>> conceptRelations = new HashMap<>();

    public static void setConceptRelations(List<String[]> lines) {
        for (String[] line : lines) {
            if (line.length == 1) continue;

            String term = line[0];

            Map<String, List<String>> relations = conceptRelations.get(term);
            if (relations == null) conceptRelations.put(term, relations = new HashMap<>());

            for (int i=1; i < line.length; i++) {
                String[] components = line[i].split("/");
                List<String> relatedTerms = relations.get(components[1]);
                if (relatedTerms == null) relations.put(components[1], relatedTerms = new ArrayList<>());
                if (components[0].equals(term)) relatedTerms.add(components[1]);
                else if (components[1].equals(term)) relatedTerms.add(components[0]);
            }
        }
    }

    public static Map<String, List<String>> wikiCategories = new HashMap<>();

    public static void setWikiCategories(List<String[]> lines) {
        for (String[] line : lines) {
            if (line.length == 1) continue;

            List<String> temp = Arrays.asList(Arrays.copyOfRange(line, 1, line.length));
            List<String> categoriesList = new ArrayList<>(temp);
            if (categoriesList.contains("")) {
                categoriesList.remove("");
            }

            wikiCategories.put(line[0], categoriesList);
        }
    }

    public static Map<String, List<String>> wikiTitles = new HashMap<>();

    public static void setWikiTitles(List<String[]> lines) {
        for (String[] line : lines) {
            if (line.length == 1) continue;

            int end = line.length < 51 ? line.length : 51;
            //int end = line.length < 101 ? line.length : 101;
            //int end = line.length < 151 ? line.length : 151;

            wikiTitles.put(line[0], Arrays.asList(Arrays.copyOfRange(line, 1, end)));
        }
    }

    public static Map<String, String> frames = new HashMap<>();

    public static void setFrames(String[] lines) {
        for (String line : lines) {
            String[] tokens = line.split("\\|\\|");
            String identifier = tokens[0];
            //String[] fills = tokens[1].split("\t");
            //String[] predicates = tokens[2].split("\t");
            //String[] arguments = tokens[3].split("\t");

            line = line.replace(identifier+"||", "");
            frames.put(identifier, line);
        }
    }

}
