package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class PosTags extends Features {

    int start;

    int end49;     //pos tags in Q
    int end50;     //pos tags in A
    int end51;     //pos tags in Expl

    int end52;     //pos tags in Q+Expl
    int end53;     //pos tags in A+Expl
    int end54;     //pos tags in Q+A+Expl

    List<String> posTags = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end49;
    }

    @Override
    public int getLastSize() {
        return end54;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (!posTags.contains(token.getPos())) posTags.add(token.getPos());
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end49 = start+posTags.size()+1;
        end50 = end49+posTags.size()+1;
        end51 = end50+posTags.size()+1;

        end52 = end51+posTags.size()+1;
        end53 = end52+posTags.size()+1;
        end54 = end53+posTags.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, posTags, question.getPosTags(), end49) +" "+
                getFeature(end49, posTags, correctAns.getPosTags(), end50) +" "+
                getFeature(end50, posTags, expl.getPosTags(), end51) +" "+
                getFeature(end51, posTags, getCommon(List.copyOf(question.getPosTags()), List.copyOf(expl.getPosTags())), end52) +" "+
                getFeature(end52, posTags, getCommon(List.copyOf(correctAns.getPosTags()), List.copyOf(expl.getPosTags())), end53) +" "+
                getFeature(end53, posTags, getCommon(getGroup(List.copyOf(question.getPosTags()), List.copyOf(correctAns.getPosTags())), List.copyOf(expl.getPosTags())), end54);
    }
}
