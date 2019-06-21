package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class FocusConcepts extends Features {

    int start;

    int end19;    //unique Q Focus
    int end20;    //unique A Focus
    int end21;    //unique Expl Focus
    int end22;    //common Q+Expl Focus
    int end23;    //common A+Expl Focus
    int end24;    //common Q+A+Expl Focus

    List<String> uniqueFocus = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end19;
    }

    @Override
    public int getLastSize() {
        return end24;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (!token.getConcretenessLabel().equals("Focus")) return;
        if (!uniqueFocus.contains(token.getLemma())) uniqueFocus.add(token.getLemma());
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end19 = start+uniqueFocus.size()+1;
        end20 = end19+uniqueFocus.size()+1;
        end21 = end20+uniqueFocus.size()+1;
        end22 = end21+uniqueFocus.size()+1;
        end23 = end22+uniqueFocus.size()+1;
        end24 = end23+uniqueFocus.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, uniqueFocus, question.getFocusLemmas(), end19) +" "+
                getFeature(end19, uniqueFocus, correctAns.getFocusLemmas(), end20) +" "+
                getFeature(end20, uniqueFocus, expl.getFocusLemmas(), end21) +" "+
                getFeature(end21, uniqueFocus, getCommon(List.copyOf(question.getFocusLemmas()), List.copyOf(expl.getFocusLemmas())), end22) +" "+
                getFeature(end22, uniqueFocus, getCommon(List.copyOf(correctAns.getFocusLemmas()), List.copyOf(expl.getFocusLemmas())), end23) +" "+
                getFeature(end23, uniqueFocus, getCommon(getGroup(List.copyOf(question.getFocusLemmas()), List.copyOf(correctAns.getFocusLemmas())), List.copyOf(expl.getFocusLemmas())), end24);
    }
}
