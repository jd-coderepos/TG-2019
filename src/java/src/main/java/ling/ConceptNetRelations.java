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
public class ConceptNetRelations extends Features {

    int start;

    int end11; //Q lemma relations
    int end21; //A lemma relations
    int end31; //Expl lemma relations
    int end41; //Q+A+Expl lemma relations

    List<String> relations = new ArrayList<>();

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

            if (!relations.contains(relation)) relations.add(relation);
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+relations.size(); //Q lemma relations
        end21 = end11+relations.size(); //A lemma relations
        end31 = end21+relations.size(); //Expl lemma relations
        end41 = end31+relations.size(); //Q+A+Expl lemma relations
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, relations, question.getRelations(), end11) +" "+
                getFeature(end11, relations, correctAns.getRelations(), end21) +" "+
                getFeature(end21, relations, expl.getRelations(), end31) +" "+
                getFeature(end31, relations, getCommon(getGroup(List.copyOf(question.getRelations()), List.copyOf(correctAns.getRelations())), List.copyOf(expl.getRelations())), end41);
    }

}
