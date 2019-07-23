package ling;

import markup.Sentence;
import markup.Token;

/**
 * @author jld
 */
public class Embeddings extends Features {

    int start;

    int end1;

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

    @Override
    public void setFeatureSizes(int start) {

    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return null;
    }
}
