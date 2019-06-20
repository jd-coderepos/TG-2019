package ling;

import markup.Sentence;
import markup.Token;

/**
 * @author jld
 */
abstract class Features {

    abstract int getFirstSize();

    abstract int getLastSize();

    abstract void setUniFeatures(Token token);

    abstract void setFeatureSizes(int start);

    abstract String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl);

}
