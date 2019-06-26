package markup;

import main.Main;
import utils.NLP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Sentence {
    List<Token> tokens;

    List<String> words;

    List<String> lemmas;

    List<String> posTags;
    List<String> concepts;

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

    List<String> cn_lemmarelations;

    Map<String, Integer> cn_wordrelations;

    public Sentence() {
        tokens = new ArrayList<>();
        words = new ArrayList<>();

        lemmas = new ArrayList<>();

        posTags = new ArrayList<>();
        concepts = new ArrayList<>();

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

        cn_lemmarelations = new ArrayList<>();

        cn_wordrelations = new HashMap<>();
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
                main.getCnrel().setUniFeatures(t);
                //main.getCnrelext().setUniFeatures(t);
                //main.getDep().setUniFeatures(t);
            }
        }
    }

    public void setCumulativeValues(Token t){
        if (!words.contains(t.getWord())) words.add(t.getWord());
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
        if (relatedTerms != null) {
            for (String relation : relatedTerms.keySet()) {
                List<String> terms = relatedTerms.get(relation);
                for (String term : terms) {
                    cn_wordrelations.put(relation + "-" + term, 0);
                }
            }
        }

        List<String> concepts = t.getConcepts();
        if (concepts != null) {
            for (String concept : concepts) {
                relatedTerms = NLP.conceptRelations.get(concept);
                if (relatedTerms == null) continue;
                for (String relation : relatedTerms.keySet()) {
                    List<String> terms = relatedTerms.get(relation);
                    for (String term : terms) {
                        cn_wordrelations.put(relation + "-" + term, 0);
                    }
                }
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

    public List<String> getCn_lemmarelations() {
        return cn_lemmarelations;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
    }

    public List<String> getWords() {
        return words;
    }

    public Map<String, Integer> getCn_wordrelations() {
        return cn_wordrelations;
    }

}
