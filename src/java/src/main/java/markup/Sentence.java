package markup;

import ling.Lemma;
import main.Main;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Sentence {
    List<Token> tokens;

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

    //set only non-stopword tokens
    public void setTokens(String[] line, boolean train, Main main) {
        for (String token : line) {
            String[] elements = token.split("/");
            if (NLP.stopwords.contains(elements[0].toLowerCase())) continue;

            List<String> concepts = NLP.concepts.get(elements[0].toLowerCase());
            Token t = new Token(elements[0], elements[1], elements[2], elements[4], elements[5].equals("null") ? null : Double.parseDouble(elements[5]), concepts);
            if (elements.length == 7) {
                t.setDepRel(elements[6].split("\\|"), false);
            }
            if (elements.length == 8) {
                t.setDepRel(elements[7].split("\\|"), true);
            }
            tokens.add(t);
            setCumulativeValues(t);

            if (train) {
                main.getLemma().setUniFeatures(t);
                //main.getDep().setUniFeatures(t);
                //main.getPosTags().setUniFeatures(t);
                //main.getCon().setUniFeatures(t);
                //main.getACon().setUniFeatures(t);
                main.getFCon().setUniFeatures(t);
                main.getCcon().setUniFeatures(t);
            }
        }
    }

    public void setCumulativeValues(Token t){
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
