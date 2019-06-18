package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class SvmRankFeatures {

    public void init() {

    }

    public static String getLemmaStr() {
        return "";
    }

    public static void setFeaturesSizes() {
        end1 = uniqueLemmas.size()+1;
        end2 = end1+uniqueLemmas.size()+1;
        end3 = end2+uniqueLemmas.size()+1;

        end4 = end3+uniqueLemmas.size()+1;
        end5 = end4+uniqueLemmas.size()+1;
        end6 = end5+uniqueLemmas.size()+1;

    }

    /**
     * @param sentence
     */
    public static void populateLists(Sentence sentence) {
        List<Token> tokens = sentence.getTokens();
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            String word = token.getWord();
            if (NLP.stopwords.contains(word.toLowerCase())) continue;

            if (!uniqueLemmas.contains(token.getLemma())) uniqueLemmas.add(token.getLemma());

            String label = token.getConcretenessLabel();
            setConcreteness(label, token.getLemma());

            if (!posTags.contains(token.getPos())) posTags.add(token.getPos());

            if (!token.getInDeprel().isEmpty()) {
                for (String dr : token.getInDeprel()) {
                    if (!depRel.contains(dr)) depRel.add(dr);
                }
                for (String ldr : token.getInDeprelLemma()) {
                    if (!lemmaDepRelPairs.contains(ldr)) lemmaDepRelPairs.add(ldr);
                }
            }
            if (!token.getOutDeprel().isEmpty()) {
                for (String dr : token.getOutDeprel()) {
                    if (!depRel.contains(dr)) depRel.add(dr);
                }
                for (String ldr : token.getOutDeprelLemma()) {
                    if (!lemmaDepRelPairs.contains(ldr)) lemmaDepRelPairs.add(ldr);
                }
            }
        }

        for (String pattern : sentence.getInBetweenPatterns()) {
            if (inBetweenPatterns.contains(pattern)) continue;
            inBetweenPatterns.add(pattern);
        }

    }



    public static void setConcreteness(String label, String lemma) {
        switch(label) {
            case "Focus":
                if (!unqiueFocus.contains(lemma)) unqiueFocus.add(lemma);
                break;
            case "Abstract":
                if (!uniqueAbstract.contains(lemma)) uniqueAbstract.add(lemma);
                break;
            case "Concrete":
                if (!uniqueConcrete.contains(lemma)) uniqueConcrete.add(lemma);
                break;
        }
    }

    public static List<String> uniqueLemmas = new ArrayList<>();

    //Concreteness Features
    public static List<String> unqiueFocus = new ArrayList<>();
    public static List<String> uniqueAbstract = new ArrayList<>();
    public static List<String> uniqueConcrete = new ArrayList<>();

    public static List<String> posTags = new ArrayList<>();

    public static List<String> depRel = new ArrayList<>();
    public static List<String> lemmaDepRelPairs = new ArrayList<>();

    public static List<String> inBetweenPatterns = new ArrayList<>();
    public static List<String> posTagPatterns = new ArrayList<>();

    public static List<String> uniqueConcepts = new ArrayList<>();

    //features
    //all are computed for non-stop words unless mentioned
    public static int end1;     //non-stop word lemmas in Q
    public static int end2;     //non-stop word lemmas in A
    public static int end3;     //non-stop word lemmas in Expl

    public static int end4;     //non-stop word common lemmas in Q+Expl
    public static int end5;     //non-stop word common lemmas in A+Expl
    public static int end6;     //non-stop word common lemmas in Q+A+Expl

    public static int end7;     //NN concepts in Expl
    public static int end8;     //NN concepts in Q
    public static int end9;     //NN concepts in A
    public static int end10;    //NN concepts in Q+Expl
    public static int end11;    //NN concepts in A+Expl
    public static int end12;    //NN concepts in Q+A+Expl

    public static int end13;    //MWE concepts in Expl
    public static int end14;    //MWE concepts in Q
    public static int end15;    //MWE concepts in A
    public static int end16;    //MWE concepts in Q+Expl
    public static int end17;    //MWE concepts in A+Expl
    public static int end18;    //MWE concepts in Q+A+Expl

    public static int end19;    //unique Q Abstract
    public static int end20;    //unique A Abstract
    public static int end21;    //unique Expl Abstract
    public static int end22;    //common Q+Expl Abstract
    public static int end23;    //common A+Expl Abstract
    public static int end24;    //common Q+A+Expl Abstract

    public static int end25;    //unique Q Focus
    public static int end26;    //unique A Focus
    public static int end27;    //unique Expl Focus
    public static int end28;    //common Q+Expl Focus
    public static int end29;    //common A+Expl Focus
    public static int end30;    //common Q+A+Expl Focus

    public static int end31;    //unique Q Concrete
    public static int end32;    //unique A Concrete
    public static int end33;    //unique Expl Concrete
    public static int end34;    //common Q+Expl Concrete
    public static int end35;    //common A+Expl Concrete
    public static int end36;    //common Q+A+Expl Concrete

    public static int end37;    //source tablestore for Expl

    public static int inBetPatternsExpl;    //for inbetween patterns: "is ... preposition" (not > 5 words)


}
