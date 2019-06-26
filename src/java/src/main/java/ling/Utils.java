package ling;

import main.Main;
import markup.Sentence;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jld
 */
public class Utils {

    public static String getFeatureStr(Main main, Sentence q, Sentence a, Sentence expl, String source) {
        return main.getLemma().toSVMRankString(q, a, expl)+" "+
                main.getTs().toSVMRankString(source)+" "+
                main.getAffix().toSVMRankString(q, a, expl)+" "+
                main.getCon().toSVMRankString(q, a, expl)+" "+
                main.getOpenRel().toSVMRankString(q, a, expl)+" "+
                main.getCnrel().toSVMRankString(q, a, expl);
                //main.getCnrelext().toSVMRankString(q, a, expl);
    }

    public static String getFeature(int start, Map<String, Integer> globalfeatures, List<String> localfeatures, int end) {
        String featureStr = "";
        Set<Integer> indexes = new HashSet<>();
        for (String localfeature : localfeatures) {
            int index = globalfeatures.containsKey(localfeature) ? globalfeatures.get(localfeature)+1+start : end;
            indexes.add(index);
        }
        List<Integer> list = new ArrayList<>(indexes);
        Collections.sort(list);
        for (int index : list) {
            featureStr += index+":1 ";
        }
        //featureStr = featureStr.trim();
        return featureStr.trim();
    }

    public static String getFeature(int start, Map<String, Integer> globalfeatures, Map<String, Integer> localfeatures, int end) {
        String featureStr = "";

        Map<String, Integer> globalfeaturescopy = new HashMap<>(globalfeatures);
        globalfeaturescopy.keySet().retainAll(localfeatures.keySet());

        Map<String, Integer> sortedMap = globalfeaturescopy.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (String v : sortedMap.keySet()) {
            featureStr += sortedMap.get(v)+":1 ";
        }

        return featureStr.trim();
    }

    public static String getFeature(int start, List<String> globalfeatures, List<String> localfeatures, int end) {
        String featureStr = "";
        List<Integer> indexes = new ArrayList<>();
        for (String feat : localfeatures) {
            int index = globalfeatures.contains(feat) ? globalfeatures.indexOf(feat)+1+start : end;
            if (!indexes.contains(index)) indexes.add(index);
        }
        Collections.sort(indexes);
        for (int index : indexes) {
            featureStr += index+":1 ";
        }
        return featureStr.trim();
    }

    public static String getFeature(int start, List<String> globalfeatures, String feature, int end) {
        int index = globalfeatures.contains(feature) ? globalfeatures.indexOf(feature)+1+start : end;
        return index+":1";
    }

    public static List<String> getCommon(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        terms1.retainAll(terms2);
        return terms1;
    }

    public static Map<String, Integer> getCommon(Map<String, Integer> terms1, Map<String, Integer> terms2) {
        terms1 = new HashMap<>(terms1);
        terms2 = new HashMap<>(terms2);
        terms1.keySet().retainAll(terms2.keySet());
        return terms1;
    }

    public static List<String> getGroup(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        for (String term : terms2) {
            if (!terms1.contains(term)) terms1.add(term);
        }
        return terms1;
    }

    public static Map<String, Integer> getGroup(Map<String, Integer> terms1, Map<String, Integer> terms2) {
        terms1 = new HashMap<>(terms1);
        terms2 = new HashMap<>(terms2);
        terms1.putAll(terms2);
        return terms1;
    }

    public static List<String> getList(List<String[]> lines) {
        List<String> list = new ArrayList<>();
        for (String[] line : lines) {
            list.add(line[0]);
        }
        return list;
    }

    /*public static void main(String[] args) throws IOException {

        String dir = "C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\resources\\conceptnet";

        List<String> words = getList(utils.IO.readCSV(dir+"\\wordtriples.txt", '\t', 0));
        List<String> wordlemmas = getList(utils.IO.readCSV(dir+"\\wordlemmatriples.txt", '\t', 0));

        Date date= new Date();

        long before = date.getTime();
        System.out.println("Time in Milliseconds: " + before);
        Timestamp tsbefore = new Timestamp(before);
        System.out.println("Current Time Stamp: " + tsbefore);

        getCommonList(words, wordlemmas);

        long after = date.getTime();
        System.out.println("Time in Milliseconds: " + after);
        Timestamp tsafter = new Timestamp(after);
        System.out.println("Current Time Stamp: " + tsafter);

        System.out.println("Difference for set implementation: "+tsafter.compareTo(tsbefore));

    }*/

}
