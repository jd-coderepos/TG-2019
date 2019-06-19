package markup;

import utils.NLP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Sentence {

    List<Token> tokens;
    List<String> inBetweenPatterns;

    List<String> lemmas;
    List<String> posTags;
    List<String> concepts;
    List<String> abstractLemmas;
    List<String> focusLemmas;
    List<String> concreteLemmas;
    List<String> inDepRels;
    List<String> inlemmaDepRels;
    List<String> outDepRels;
    List<String> outlemmaDepRels;

    public Sentence() {
        tokens = new ArrayList<>();
        inBetweenPatterns = new ArrayList<>();
        lemmas = new ArrayList<>();
        posTags = new ArrayList<>();
        concepts = new ArrayList<>();
        abstractLemmas = new ArrayList<>();
        focusLemmas = new ArrayList<>();
        concreteLemmas = new ArrayList<>();
        inDepRels = new ArrayList<>();
        inlemmaDepRels = new ArrayList<>();
        outDepRels = new ArrayList<>();
        outlemmaDepRels = new ArrayList<>();
    }

    public void setTokens(String[] line) {
        for (String token : line) {
            //System.out.println(token);
            String[] elements = token.split("/");
            //System.out.println(elements.length);
            //System.out.println(elements[0]+" "+elements[1]+" "+elements[2]+" "+elements[4]+" "+elements[5]);
            List<String> concepts = NLP.concepts.get(elements[0].toLowerCase());
            Token t = new Token(elements[0], elements[1], elements[2], elements[4], elements[5].equals("null") ? null : Double.parseDouble(elements[5]), concepts);
            if (elements.length == 7) {
                t.setDepRel(elements[6].split("\\|"), false);
            }
            if (elements.length == 8) {
                t.setDepRel(elements[7].split("\\|"), true);
            }
            tokens.add(t);
        }
    }

    public void setInBetweenPatterns() {
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.getLemma().equals("be")) setInBetweenPattern(i);
        }
    }

    public void setInBetweenPattern(int i) {
        String inBetweenPattern = "";
        Token t = tokens.get(i);
        int index = i;
        while (!t.getPos().equals("IN") && index < i+4 && index < tokens.size()) {
            t = tokens.get(index);
            inBetweenPattern += t.getLemma()+" ";
            index++;
        }
        inBetweenPattern = inBetweenPattern.trim();
        if (!inBetweenPatterns.contains(inBetweenPattern)) inBetweenPatterns.add(inBetweenPattern);
    }

    public void setAggregateFeatures(){
        for (Token t : tokens) {
            if (NLP.stopwords.contains(t.getWord().toLowerCase())) continue;

            if (!lemmas.contains(t.getLemma())) lemmas.add(t.getLemma());
            if (!posTags.contains(t.getPos())) posTags.add(t.getPos());
            if (t.getConcepts() != null) {
                for (String concept : t.getConcepts()) {
                    if (!concepts.contains(concept)) concepts.add(concept);
                }
            }
            if (t.getConcretenessLabel().equals("Abstract")) {
                if (!abstractLemmas.contains(t.getLemma())) abstractLemmas.add(t.getLemma());
            }
            else if (t.getConcretenessLabel().equals("Focus")) {
                if (!focusLemmas.contains(t.getLemma())) focusLemmas.add(t.getLemma());
            }
            else if (t.getConcretenessLabel().equals("Concrete")) {
                if (!concreteLemmas.contains(t.getLemma())) concreteLemmas.add(t.getLemma());
            }

            if (!t.getInDeprel().isEmpty()) {
                for (String inDepRel : t.getInDeprel()) {
                    if (!inDepRels.contains(inDepRel)) inDepRels.add(inDepRel);
                }
                for (String inlemmaDepRel : t.getInLemmaDeprel()) {
                    if (!inlemmaDepRels.contains(inlemmaDepRel)) inlemmaDepRels.add(inlemmaDepRel);
                }
            }

            if (!t.getOutDeprel().isEmpty()) {
                for (String outDepRel : t.getOutDeprel()) {
                    if (!outDepRels.contains(outDepRel)) outDepRels.add(outDepRel);
                }
                for (String outlemmaDepRel : t.getOutLemmaDeprel()) {
                    if (!outlemmaDepRels.contains(outlemmaDepRel)) outlemmaDepRels.add(outlemmaDepRel);
                }
            }
        }
    }

    public List<String> getInBetweenPatterns() {
        return inBetweenPatterns;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<String> getLemmas() {
        return lemmas;
    }

    public List<String> getPosTags() {
        return posTags;
    }

    public List<String> getConcepts() {
        return concepts;
    }

    public List<String> getAbstractLemmas() {
        return abstractLemmas;
    }

    public List<String> getFocusLemmas() {
        return focusLemmas;
    }

    public List<String> getConcreteLemmas() {
        return concreteLemmas;
    }

    public List<String> getInDepRels() {
        return inDepRels;
    }

    public List<String> getInlemmaDepRels() {
        return inlemmaDepRels;
    }

    public List<String> getOutDepRels() {
        return outDepRels;
    }

    public List<String> getOutlemmaDepRels() {
        return outlemmaDepRels;
    }

}
