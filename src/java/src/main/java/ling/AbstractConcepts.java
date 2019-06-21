package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class AbstractConcepts extends Features {

    int start;

    int end13;    //unique Q Abstract
    int end14;    //unique A Abstract
    int end15;    //unique Expl Abstract
    int end16;    //common Q+Expl Abstract
    int end17;    //common A+Expl Abstract
    int end18;    //common Q+A+Expl Abstract

    List<String> uniqueAbstract = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end13;
    }

    @Override
    public int getLastSize() {
        return end18;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (!token.getConcretenessLabel().equals("Abstract")) return;
        if (!uniqueAbstract.contains(token.getLemma())) uniqueAbstract.add(token.getLemma());
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end13 = start+uniqueAbstract.size()+1;
        end14 = end13+uniqueAbstract.size()+1;
        end15 = end14+uniqueAbstract.size()+1;
        end16 = end15+uniqueAbstract.size()+1;
        end17 = end16+uniqueAbstract.size()+1;
        end18 = end17+uniqueAbstract.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, uniqueAbstract, question.getAbstractLemmas(), end13) +" "+
                getFeature(end13, uniqueAbstract, correctAns.getAbstractLemmas(), end14) +" "+
                getFeature(end14, uniqueAbstract, expl.getAbstractLemmas(), end15) +" "+
                getFeature(end15, uniqueAbstract, getCommon(List.copyOf(question.getAbstractLemmas()), List.copyOf(expl.getAbstractLemmas())), end16) +" "+
                getFeature(end16, uniqueAbstract, getCommon(List.copyOf(correctAns.getAbstractLemmas()), List.copyOf(expl.getAbstractLemmas())), end17) +" "+
                getFeature(end17, uniqueAbstract, getCommon(getGroup(List.copyOf(question.getAbstractLemmas()), List.copyOf(correctAns.getAbstractLemmas())), List.copyOf(expl.getAbstractLemmas())), end18);
    }
}
