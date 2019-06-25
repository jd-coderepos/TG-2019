package ling;

import markup.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class LemmaTestArr {

    int start;

    int end1;     //non-stop word lemmas in Q
    int end2;     //non-stop word lemmas in A
    int end3;     //non-stop word lemmas in Expl

    int end4;     //non-stop word common lemmas in Q+Expl
    int end5;     //non-stop word common lemmas in A+Expl
    int end6;     //non-stop word common lemmas in Q+A+Expl

    int[] lemmaQArr;
    int[] lemmaAArr;
    int[] lemmaExplArr;

    List<String> quesUniqueLemmas = new ArrayList<>();
    List<String> ansUniqueLemmas = new ArrayList<>();
    List<String> explUniqueLemmas = new ArrayList<>();

    public void setUniFeatures(Token token, String type) {
        if (type.equals("q")) if (!quesUniqueLemmas.contains(token.getLemma())) quesUniqueLemmas.add(token.getLemma());
        if (type.equals("a")) if (!ansUniqueLemmas.contains(token.getLemma())) ansUniqueLemmas.add(token.getLemma());
        if (type.equals("e")) if (!explUniqueLemmas.contains(token.getLemma())) explUniqueLemmas.add(token.getLemma());
    }



}
