package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class ConceptNetSynonyms extends Features {

    int start;

    int end11; //Q lemma synonyms
    int end21; //A lemma synonyms
    int end31; //Expl lemma synonyms
    int end41; //Q+A+Expl lemma synonyms

    List<String> synonyms = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end11;
    }

    @Override
    public int getLastSize() {
        return end41;
    }

    @Override
    public void setUniFeatures(Token token) {
        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(token.getLemma().toLowerCase());
        if (relatedTerms == null) return;
        //Synonym
        if (relatedTerms.containsKey("Synonym")) {
            List<String> terms = relatedTerms.get("Synonym");
            for (String term : terms) {
                if (!synonyms.contains(term)) synonyms.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+synonyms.size(); //Q lemma synonyms
        end21 = end11+synonyms.size(); //A lemma synonyms
        end31 = end21+synonyms.size(); //Expl lemma synonyms
        end41 = end31+synonyms.size(); //Q+A+Expl lemma synonyms
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, synonyms, question.getSynonyms(), end11) +" "+
                getFeature(end11, synonyms, correctAns.getSynonyms(), end21) +" "+
                getFeature(end21, synonyms, expl.getSynonyms(), end31) +" "+
                getFeature(end31, synonyms, getCommon(getGroup(List.copyOf(question.getSynonyms()), List.copyOf(correctAns.getSynonyms())), List.copyOf(expl.getSynonyms())), end41);
    }
}
