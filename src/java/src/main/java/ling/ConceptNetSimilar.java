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
public class ConceptNetSimilar extends Features {

    int start;

    int end11; //Q lemma similar
    int end21; //A lemma similar
    int end31; //Expl lemma similar
    int end41; //Q+A+Expl lemma similar

    List<String> similar = new ArrayList<>();

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
                if (!similar.contains(term)) similar.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+similar.size(); //Q lemma similar
        end21 = end11+similar.size(); //A lemma similar
        end31 = end21+similar.size(); //Expl lemma similar
        end41 = end31+similar.size(); //Q+A+Expl lemma similar
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, similar, question.getSimilar(), end11) +" "+
                getFeature(end11, similar, correctAns.getSimilar(), end21) +" "+
                getFeature(end21, similar, expl.getSimilar(), end31) +" "+
                getFeature(end31, similar, getCommon(getGroup(List.copyOf(question.getSimilar()), List.copyOf(correctAns.getSimilar())), List.copyOf(expl.getSimilar())), end41);
    }

}
