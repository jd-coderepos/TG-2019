package ling;

import main.Main;
import markup.Sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jld
 */
public class Utils {

    public static String getFeatureStr(Main main, Sentence q, Sentence a, Sentence expl, String source) {
        return main.getLemma().toSVMRankString(q, a, expl)+" "+
                main.getTs().toSVMRankString(source);
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

    public static List<String> getGroup(List<String> terms1, List<String> terms2) {
        terms1 = new ArrayList<>(terms1);
        terms2 = new ArrayList<>(terms2);
        for (String term : terms2) {
            if (!terms1.contains(term)) terms1.add(term);
        }
        return terms1;
    }

}
