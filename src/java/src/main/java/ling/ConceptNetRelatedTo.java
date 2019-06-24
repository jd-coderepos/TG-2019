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
public class ConceptNetRelatedTo extends Features {

    int start;

    int end11; //Q lemma relatedTo
    int end21; //A lemma relatedTo
    int end31; //Expl lemma relatedTo
    int end41; //Q+A+Expl lemma relatedTo

    List<String> relatedTo = new ArrayList<>();

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
        if (relatedTerms.containsKey("Similar")) {
            List<String> terms = relatedTerms.get("Similar");
            for (String term : terms) {
                if (!relatedTo.contains(term)) relatedTo.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+relatedTo.size(); //Q lemma relatedTo
        end21 = end11+relatedTo.size(); //A lemma relatedTo
        end31 = end21+relatedTo.size(); //Expl lemma relatedTo
        end41 = end31+relatedTo.size(); //Q+A+Expl lemma relatedTo
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, relatedTo, question.getRelatedTo(), end11) +" "+
                getFeature(end11, relatedTo, correctAns.getRelatedTo(), end21) +" "+
                getFeature(end21, relatedTo, expl.getRelatedTo(), end31) +" "+
                getFeature(end31, relatedTo, getCommon(getGroup(List.copyOf(question.getRelatedTo()), List.copyOf(correctAns.getRelatedTo())), List.copyOf(expl.getRelatedTo())), end41);
    }

}
