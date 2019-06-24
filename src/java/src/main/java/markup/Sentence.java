package markup;

import ling.Lemma;
import main.Main;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    List<String> prefix;
    List<String> suffix;

    List<String> subj_words;
    List<String> subj_lemmas;
    List<String> obj_words;
    List<String> obj_lemmas;
    List<String> rel_words;
    List<String> rel_lemmas;

    List<String> synonyms;
    List<String> isA;
    List<String> similar;
    List<String> relatedTo;

    List<String> relations;
    List<String> relatedWordCloud;

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

        prefix = new ArrayList<>();
        suffix = new ArrayList<>();

        subj_words = new ArrayList<>();
        subj_lemmas = new ArrayList<>();
        obj_words = new ArrayList<>();
        obj_lemmas = new ArrayList<>();
        rel_words = new ArrayList<>();
        rel_lemmas = new ArrayList<>();

        synonyms = new ArrayList<>();
        isA = new ArrayList<>();
        similar = new ArrayList<>();
        relatedTo = new ArrayList<>();

        relations = new ArrayList<>();
        relatedWordCloud = new ArrayList<>();
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
                main.getAffix().setUniFeatures(t);
                main.getCon().setUniFeatures(t);
                //main.getPosTags().setUniFeatures(t);
                //main.getDep().setUniFeatures(t);
                //main.getConceptNet().setUniFeatures(t);
                //main.getCnsyn().setUniFeatures(t);
                //main.getCnisa().setUniFeatures(t);
                //main.getCnsim().setUniFeatures(t);
                //main.getCnrelto().setUniFeatures(t);
                //main.getCnrel().setUniFeatures(t);
                //main.getRwc().setUniFeatures(t);
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

        for (String pref : t.getPrefix()) {
            if (!prefix.contains(pref)) prefix.add(pref);
        }
        for (String suff : t.getSuffix()) {
            if (!suffix.contains(suff)) suffix.add(suff);
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

        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(t.word.toLowerCase());
        //Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(t.lemma.toLowerCase());
        if (relatedTerms == null) return;
        //Synonym
        if (relatedTerms.containsKey("Synonym")) {
            List<String> terms = relatedTerms.get("Synonym");
            for (String term : terms) {
                if (!synonyms.contains(term)) synonyms.add(term);
            }
        }
        //IsA
        if (relatedTerms.containsKey("IsA")) {
            List<String> terms = relatedTerms.get("IsA");
            for (String term : terms) {
                if (!isA.contains(term)) isA.add(term);
            }
        }
        //Similar
        if (relatedTerms.containsKey("Similar")) {
            List<String> terms = relatedTerms.get("Similar");
            for (String term : terms) {
                if (similar.contains(term)) similar.add(term);
            }
        }
        //RelatedTo
        if (relatedTerms.containsKey("RelatedTo")) {
            List<String> terms = relatedTerms.get("RelatedTo");
            for (String term : terms) {
                if (relatedTo.contains(term)) relatedTo.add(term);
            }
        }

        for (String relation : relatedTerms.keySet()) {
            if (relation.equals("Synonym") || relation.equals("IsA") || relation.equals("Similar") || relation.equals("RelatedTo")) continue;

            if (!relations.contains(relation)) relations.add(relation);

            List<String> terms = relatedTerms.get(relation);
            for (String term : terms) {
                if (!relatedWordCloud.contains(term)) relatedWordCloud.add(term);
            }
        }
    }

    public void setRelations(String stringRel, boolean train, Main main) {
        if (stringRel == null) return;
        String[] relations = stringRel.split("/");

        for (String relation : relations) {
            String[] relComponents = relation.split("\\|");
            if (relComponents.length == 1) continue;

            //first two subj
            //if (!subj_words.contains(relComponents[0])) subj_words.add(relComponents[0]);
            if (!subj_lemmas.contains(relComponents[1])) subj_lemmas.add(relComponents[1]);

            //next two obj
            //if (!obj_words.contains(relComponents[2])) obj_words.add(relComponents[2]);
            if (!obj_lemmas.contains(relComponents[3])) obj_lemmas.add(relComponents[3]);

            //next two rel
            //if (!rel_words.contains(relComponents[4])) rel_words.add(relComponents[4]);
            if (!rel_lemmas.contains(relComponents[5])) rel_lemmas.add(relComponents[5]);

        }
        if (train) {
            main.getOpenRel().setUniFeatures(this);
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

    public List<String> getSubj_words() {
        return subj_words;
    }

    public List<String> getSubj_lemmas() {
        return subj_lemmas;
    }

    public List<String> getObj_words() {
        return obj_words;
    }

    public List<String> getObj_lemmas() {
        return obj_lemmas;
    }

    public List<String> getRel_words() {
        return rel_words;
    }

    public List<String> getRel_lemmas() {
        return rel_lemmas;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getIsA() {
        return isA;
    }

    public List<String> getSimilar() {
        return similar;
    }

    public List<String> getRelatedTo() {
        return relatedTo;
    }

    public List<String> getRelations() {
        return relations;
    }

    public List<String> getRelatedWordCloud() {
        return relatedWordCloud;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
    }

}
