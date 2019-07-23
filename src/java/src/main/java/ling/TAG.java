package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ling.Utils.getFeature;

/**
 * @author jld
 */
public class TAG extends Features {

    int start;

    int end1;

    Map<String, Integer> tagTypes = new HashMap<>();

    @Override
    public int getFirstSize() {
        return end1;
    }

    @Override
    public int getLastSize() {
        return end1;
    }

    @Override
    public void setUniFeatures(Token token) {

    }

    public void setUniFeatures(String string) {
        if (tagTypes.isEmpty()) tagTypes.put(string, 0);
        else if (!tagTypes.containsKey(string)) tagTypes.put(string, tagTypes.size());
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end1 = start + tagTypes.size() + 1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return null;
    }

    public String toSVMRankString(Map<String, String> tagFeatures) {
        return getFeature(start, tagTypes, tagFeatures, end1);
    }
}
