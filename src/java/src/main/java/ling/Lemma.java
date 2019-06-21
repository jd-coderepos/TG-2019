package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Lemma extends Features {

    int start;

    int end1;     //non-stop word lemmas in Q
    int end2;     //non-stop word lemmas in A
    int end3;     //non-stop word lemmas in Expl

    int end4;     //non-stop word common lemmas in Q+Expl
    int end5;     //non-stop word common lemmas in A+Expl
    int end6;     //non-stop word common lemmas in Q+A+Expl

    public int getFirstSize() {
        return end1;
    }

    public int getLastSize() {
        return end6;
    }

    public List<String> uniqueLemmas = new ArrayList<>();

    @Override
    public void setUniFeatures(Token token) {
        if (!uniqueLemmas.contains(token.getLemma())) uniqueLemmas.add(token.getLemma());
    }

    @Override
    public void setFeatureSizes(int s) {
        start = s;

        end1 = start+uniqueLemmas.size() + 1;   //lemmas in Q
        end2 = end1 + uniqueLemmas.size() + 1;  //lemmas in A
        end3 = end2 + uniqueLemmas.size() + 1;  //lemmas in Expl

        end4 = end3 + uniqueLemmas.size() + 1;
        end5 = end4 + uniqueLemmas.size() + 1;
        end6 = end5 + uniqueLemmas.size() + 1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return Utils.getFeature(start, uniqueLemmas, question.getLemmas(), end1)+" "+
                Utils.getFeature(end1, uniqueLemmas, correctAns.getLemmas(), end2) +" "+
                Utils.getFeature(end2, uniqueLemmas, expl.getLemmas(), end3) +" "+
                Utils.getFeature(end3, uniqueLemmas, Utils.getCommon(List.copyOf(question.getLemmas()), List.copyOf(expl.getLemmas())), end4) + " " +
                Utils.getFeature(end4, uniqueLemmas, Utils.getCommon(List.copyOf(correctAns.getLemmas()), List.copyOf(expl.getLemmas())), end5) + " " +
                Utils.getFeature(end5, uniqueLemmas, Utils.getCommon(Utils.getGroup(List.copyOf(question.getLemmas()), List.copyOf(correctAns.getLemmas())), List.copyOf(expl.getLemmas())), end6);
    }

}
