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
public class ConceptNetRelatedWordCloud extends Features {

    int start;

    int end11; //Q lemma related words
    int end21; //A lemma related words
    int end31; //Expl lemma related words
    int end41; //Q+A+Expl lemma related words

    List<String> relatedWordCloud = new ArrayList<>();

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
            if (relation.equals("Synonym") || relation.equals("IsA") || relation.equals("Similar") || relation.equals("RelatedTo"))
                continue;

            List<String> terms = relatedTerms.get(relation);

            for (String term : terms) {
                if (!relatedWordCloud.contains(term)) relatedWordCloud.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+relatedWordCloud.size(); //Q lemma relations
        end21 = end11+relatedWordCloud.size(); //A lemma relations
        end31 = end21+relatedWordCloud.size(); //Expl lemma relations
        end41 = end31+relatedWordCloud.size(); //Q+A+Expl lemma relations
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, relatedWordCloud, question.getRelatedWordCloud(), end11) +" "+
                getFeature(end11, relatedWordCloud, correctAns.getRelatedWordCloud(), end21) +" "+
                getFeature(end21, relatedWordCloud, expl.getRelatedWordCloud(), end31) +" "+
                getFeature(end31, relatedWordCloud, getCommon(getGroup(List.copyOf(question.getRelatedWordCloud()), List.copyOf(correctAns.getRelatedWordCloud())), List.copyOf(expl.getRelatedWordCloud())), end41);
    }

}
