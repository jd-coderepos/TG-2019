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
public class ConceptNetIsA extends Features {

    int start;

    int end11; //Q lemma isa
    int end21; //A lemma isa
    int end31; //Expl lemma isa
    int end41; //Q+A+Expl lemma isa

    List<String> isA = new ArrayList<>();

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
        if (relatedTerms.containsKey("IsA")) {
            List<String> terms = relatedTerms.get("IsA");
            for (String term : terms) {
                if (!isA.contains(term)) isA.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+isA.size(); //Q lemma isA
        end21 = end11+isA.size(); //A lemma isA
        end31 = end21+isA.size(); //Expl lemma isA
        end41 = end31+isA.size(); //Q+A+Expl lemma isA
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, isA, question.getIsA(), end11) +" "+
                getFeature(end11, isA, correctAns.getIsA(), end21) +" "+
                getFeature(end21, isA, expl.getIsA(), end31) +" "+
                getFeature(end31, isA, getCommon(getGroup(List.copyOf(question.getIsA()), List.copyOf(correctAns.getIsA())), List.copyOf(expl.getIsA())), end41);
    }

}
