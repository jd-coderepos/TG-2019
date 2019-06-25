package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class ConceptNetRelations extends Features {

    int start;

    int end11; //Q lemma related words
    int end21; //A lemma related words
    int end31; //Expl lemma related words
    int end41; //Q+A+Expl lemma related words

    Map<String, Integer> relationLemmaIndexed = new HashMap<>();

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
        for (String relation : relatedTerms.keySet()) {

            List<String> terms = relatedTerms.get(relation);

            for (String term : terms) {

                if (relationLemmaIndexed.isEmpty()) relationLemmaIndexed.put(relation+"-"+term, 0);
                else if (!relationLemmaIndexed.containsKey(relation+"-"+term)) relationLemmaIndexed.put(relation+"-"+term, relationLemmaIndexed.size());

            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end11 = start+relationLemmaIndexed.size()+1;
        end21 = end11+relationLemmaIndexed.size()+1;
        end31 = end21+relationLemmaIndexed.size()+1;
        end41 = end31+relationLemmaIndexed.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, relationLemmaIndexed, question.getCn_lemmarelations(), end11) +" "+
                getFeature(end11, relationLemmaIndexed, correctAns.getCn_lemmarelations(), end21) +" "+
                getFeature(end21, relationLemmaIndexed, expl.getCn_lemmarelations(), end31) +" "+
                getFeature(end31, relationLemmaIndexed, getCommon(getGroup(List.copyOf(question.getCn_lemmarelations()), List.copyOf(correctAns.getCn_lemmarelations())), List.copyOf(expl.getCn_lemmarelations())), end41);
    }
}
