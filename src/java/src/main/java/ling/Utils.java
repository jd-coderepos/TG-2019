package ling;

import main.Main;
import markup.Sentence;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author jld
 */
public class Utils {

    public static String getFeatureStr(Main main, Sentence q, Sentence a, Sentence expl, String source) {
        return main.getLemma().toSVMRankString(q, a, expl)+" "+
                main.getTs().toSVMRankString(source)+" "+
                main.getAffix().toSVMRankString(q, a, expl)+" "+
                main.getCon().toSVMRankString(q, a, expl);//+" "+
                //main.getPosTags().toSVMRankString(q, a, expl);
                //main.getDep().toSVMRankString(q, a, expl)+" "+
                //main.getOpenRel().toSVMRankString(q, a, expl)+" "+
                //main.getConceptNet().toSVMRankString(q, a, expl)+" "+
                //main.getCnsyn().toSVMRankString(q, a, expl);
                //main.getCnisa().toSVMRankString(q, a, expl);
                //main.getCnsim().toSVMRankString(q, a, expl)+" "+
                //main.getCnrelto().toSVMRankString(q, a, expl);
                //main.getCnrel().toSVMRankString(q, a, expl);
                //main.getRwc().toSVMRankString(q, a, expl);
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
        /*Set<String> terms1Set = new HashSet<>(terms1);
        Set<String> terms2Set = new HashSet<>(terms2);
        terms2Set.retainAll(terms1Set);
        return new ArrayList<>(terms2Set);*/

        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        terms1.retainAll(terms2);
        return terms1;
    }

    /*public static List<String> getCommonList(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        terms1.retainAll(terms2);
        return terms1;
    }*/

    public static List<String> getGroup(List<String> terms1, List<String> terms2) {
        /*Set<String> terms1Set = new HashSet<>(terms1);
        Set<String> terms2Set = new HashSet<>(terms2);
        terms2Set.addAll(terms1Set);*/

        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        for (String term : terms2) {
            if (!terms1.contains(term)) terms1.add(term);
        }
        //return new ArrayList<>(terms2Set);
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
