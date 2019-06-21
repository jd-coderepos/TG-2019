package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class Concepts extends Features {

    int start;

    int end7;     //concepts in Q
    int end8;     //concepts in A
    int end9;     //concepts in Expl

    int end10;    //concepts in Q+Expl
    int end11;    //concepts in A+Expl
    int end12;    //concepts in Q+A+Expl

    List<String> uniqueConcepts = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end7;
    }

    @Override
    public int getLastSize() {
        return end12;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (token.getConcepts() != null) {
            for (String conceptStr : token.getConcepts()) {
                if (uniqueConcepts.contains(conceptStr)) continue;
                uniqueConcepts.add(conceptStr);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end7 = start+uniqueConcepts.size()+1;
        end8 = end7+uniqueConcepts.size()+1;
        end9 = end8+uniqueConcepts.size()+1;

        end10 = end9+uniqueConcepts.size()+1;
        end11 = end10+uniqueConcepts.size()+1;
        end12 = end11+uniqueConcepts.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, uniqueConcepts, question.getConcepts(), end7) +" "+
                getFeature(end7, uniqueConcepts, correctAns.getConcepts(), end8) +" "+
                getFeature(end8, uniqueConcepts, expl.getConcepts(), end9) +" "+
                getFeature(end9, uniqueConcepts, getCommon(List.copyOf(question.getConcepts()), List.copyOf(expl.getConcepts())), end10) +" "+
                getFeature(end10, uniqueConcepts, getCommon(List.copyOf(correctAns.getConcepts()), List.copyOf(expl.getConcepts())), end11) +" "+
                getFeature(end11, uniqueConcepts, getCommon(getGroup(List.copyOf(question.getConcepts()), List.copyOf(correctAns.getConcepts())), List.copyOf(expl.getConcepts())), end12);
    }
}
