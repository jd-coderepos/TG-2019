package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jld
 */
public class SvmRankFeatures1 {

    public static void setFeaturesSizes() {
        end4 = uniqueLemmas.size()+1;
        end5 = end4+uniqueLemmas.size()+1;
        end6 = end5+uniqueLemmas.size()+1;

        end10 = end6+uniqueConcepts.size()+1;
        end11 = end10+uniqueConcepts.size()+1;
        end12 = end11+uniqueConcepts.size()+1;

        end16 = end12+uniqueAbstract.size()+1;
        end17 = end16+uniqueAbstract.size()+1;
        end18 = end17+uniqueAbstract.size()+1;

        end22 = end18+uniqueFocus.size()+1;
        end23 = end22+uniqueFocus.size()+1;
        end24 = end23+uniqueFocus.size()+1;

        end28 = end24+uniqueConcrete.size()+1;
        end29 = end28+uniqueConcrete.size()+1;
        end30 = end29+uniqueConcrete.size()+1;

        end31 = end30+tablestore.size()+1;

        end36 = end31+indepRel.size()+1;

        end40 = end36+inlemmaDepRel.size()+1;

        end44 = end40+outdepRel.size()+1;

        end48 = end44+outlemmaDepRel.size()+1;

        end52 = end48+posTags.size()+1;
        end53 = end52+posTags.size()+1;
        end54 = end53+posTags.size()+1;
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

            if (token.getConcepts() != null) {
                for (String conceptStr : token.getConcepts()) {
                    if (uniqueConcepts.contains(conceptStr)) continue;
                    uniqueConcepts.add(conceptStr);
                }
            }

            if (!token.getInDeprel().isEmpty()) {
                for (String dr : token.getInDeprel()) {
                    if (!indepRel.contains(dr)) indepRel.add(dr);
                }
                for (String ldr : token.getInLemmaDeprel()) {
                    if (!inlemmaDepRel.contains(ldr)) inlemmaDepRel.add(ldr);
                }
            }
            if (!token.getOutDeprel().isEmpty()) {
                for (String dr : token.getOutDeprel()) {
                    if (!outdepRel.contains(dr)) outdepRel.add(dr);
                }
                for (String ldr : token.getOutLemmaDeprel()) {
                    if (!outlemmaDepRel.contains(ldr)) outlemmaDepRel.add(ldr);
                }
            }
        }
    }

    public static void setTablestore(String source) {
        if (!tablestore.contains(source)) tablestore.add(source);
    }

    public static void setConcreteness(String label, String lemma) {
        switch(label) {
            case "Focus":
                if (!uniqueFocus.contains(lemma)) uniqueFocus.add(lemma);
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

    public static List<String> uniqueConcepts = new ArrayList<>();

    //Concreteness Features
    public static List<String> uniqueAbstract = new ArrayList<>();
    public static List<String> uniqueFocus = new ArrayList<>();
    public static List<String> uniqueConcrete = new ArrayList<>();

    public static List<String> tablestore = new ArrayList<>();

    public static List<String> inBetweenPatterns = new ArrayList<>();

    public static List<String> indepRel = new ArrayList<>();
    public static List<String> inlemmaDepRel = new ArrayList<>();

    public static List<String> outdepRel = new ArrayList<>();
    public static List<String> outlemmaDepRel = new ArrayList<>();

    public static List<String> posTags = new ArrayList<>();

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

    public static String toString(Sentence question, Sentence correctAns, Sentence expl, String explSource) {

        return getFeature(0, uniqueLemmas, getCommon(List.copyOf(question.getLemmas()), List.copyOf(expl.getLemmas())), end4) +" "+
                        getFeature(end4, uniqueLemmas, getCommon(List.copyOf(correctAns.getLemmas()), List.copyOf(expl.getLemmas())), end5) +" "+
                        getFeature(end5, uniqueLemmas, getCommon(getGroup(List.copyOf(question.getLemmas()), List.copyOf(correctAns.getLemmas())), List.copyOf(expl.getLemmas())), end6) +" "+
                        getFeature(end6, uniqueConcepts, getCommon(List.copyOf(question.getConcepts()), List.copyOf(expl.getConcepts())), end10) +" "+
                        getFeature(end10, uniqueConcepts, getCommon(List.copyOf(correctAns.getConcepts()), List.copyOf(expl.getConcepts())), end11) +" "+
                        getFeature(end11, uniqueConcepts, getCommon(getGroup(List.copyOf(question.getConcepts()), List.copyOf(correctAns.getConcepts())), List.copyOf(expl.getConcepts())), end12) +" "+
                        getFeature(end12, uniqueAbstract, getCommon(List.copyOf(question.getAbstractLemmas()), List.copyOf(expl.getAbstractLemmas())), end16) +" "+
                        getFeature(end16, uniqueAbstract, getCommon(List.copyOf(correctAns.getAbstractLemmas()), List.copyOf(expl.getAbstractLemmas())), end17) +" "+
                        getFeature(end17, uniqueAbstract, getCommon(getGroup(List.copyOf(question.getAbstractLemmas()), List.copyOf(correctAns.getAbstractLemmas())), List.copyOf(expl.getAbstractLemmas())), end18) +" "+
                        getFeature(end18, uniqueFocus, getCommon(List.copyOf(question.getFocusLemmas()), List.copyOf(expl.getFocusLemmas())), end22) +" "+
                        getFeature(end22, uniqueFocus, getCommon(List.copyOf(correctAns.getFocusLemmas()), List.copyOf(expl.getFocusLemmas())), end23) +" "+
                        getFeature(end23, uniqueFocus, getCommon(getGroup(List.copyOf(question.getFocusLemmas()), List.copyOf(correctAns.getFocusLemmas())), List.copyOf(expl.getFocusLemmas())), end24) +" "+
                        getFeature(end24, uniqueConcrete, getCommon(List.copyOf(question.getConcreteLemmas()), List.copyOf(expl.getConcreteLemmas())), end28) +" "+
                        getFeature(end28, uniqueConcrete, getCommon(List.copyOf(correctAns.getConcreteLemmas()), List.copyOf(expl.getConcreteLemmas())), end29) +" "+
                        getFeature(end29, uniqueConcrete, getCommon(getGroup(List.copyOf(question.getConcreteLemmas()), List.copyOf(correctAns.getConcreteLemmas())), List.copyOf(expl.getConcreteLemmas())), end30) +" "+
                        getFeature(end30, tablestore, explSource, end31) +" "+
                        getFeature(end31, indepRel, getCommon(getGroup(List.copyOf(question.getInDepRels()), List.copyOf(correctAns.getInDepRels())), List.copyOf(expl.getInDepRels())), end36) +" "+
                        getFeature(end36, inlemmaDepRel, getCommon(getGroup(List.copyOf(question.getInlemmaDepRels()), List.copyOf(correctAns.getInlemmaDepRels())), List.copyOf(expl.getInlemmaDepRels())), end40) +" "+
                        getFeature(end40, outdepRel, getCommon(getGroup(List.copyOf(question.getOutDepRels()), List.copyOf(correctAns.getOutDepRels())), List.copyOf(expl.getOutDepRels())), end44) +" "+
                        getFeature(end44, outlemmaDepRel, getCommon(getGroup(List.copyOf(question.getOutlemmaDepRels()), List.copyOf(correctAns.getOutlemmaDepRels())), List.copyOf(expl.getOutlemmaDepRels())), end48) +" "+
                        getFeature(end48, posTags, getCommon(List.copyOf(question.getPosTags()), List.copyOf(expl.getPosTags())), end52) +" "+
                        getFeature(end52, posTags, getCommon(List.copyOf(correctAns.getPosTags()), List.copyOf(expl.getPosTags())), end53) +" "+
                        getFeature(end53, posTags, getCommon(getGroup(List.copyOf(question.getPosTags()), List.copyOf(correctAns.getPosTags())), List.copyOf(expl.getPosTags())), end54);
    }

    //features
    //all are computed for non-stop words unless mentioned
    public static int end4;     //non-stop word common lemmas in Q+Expl
    public static int end5;     //non-stop word common lemmas in A+Expl
    public static int end6;     //non-stop word common lemmas in Q+A+Expl

    public static int end10;    //concepts in Q+Expl
    public static int end11;    //concepts in A+Expl
    public static int end12;    //concepts in Q+A+Expl

    public static int end16;    //common Q+Expl Abstract
    public static int end17;    //common A+Expl Abstract
    public static int end18;    //common Q+A+Expl Abstract

    public static int end22;    //common Q+Expl Focus
    public static int end23;    //common A+Expl Focus
    public static int end24;    //common Q+A+Expl Focus

    public static int end28;    //common Q+Expl Concrete
    public static int end29;    //common A+Expl Concrete
    public static int end30;    //common Q+A+Expl Concrete

    public static int end31;    //source tablestore for Expl

    public static int end36;    //Q+A+Expl in dep relations

    public static int end40;    //Q+A+Expl in lemma+dep relations

    public static int end44;    //Q+A+Expl out dep relations

    public static int end48;    //Q+A+Expl out lemma+dep relations

    public static int end52;     //pos tags in Q+Expl
    public static int end53;     //pos tags in A+Expl
    public static int end54;     //pos tags in Q+A+Expl

}
