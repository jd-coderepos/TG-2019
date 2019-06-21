package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class ConcreteConcepts extends Features {

    int start;

    int end25;    //unique Q Concrete
    int end26;    //unique A Concrete
    int end27;    //unique Expl Concrete
    int end28;    //common Q+Expl Concrete
    int end29;    //common A+Expl Concrete
    int end30;    //common Q+A+Expl Concrete

    List<String> uniqueConcrete = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end25;
    }

    @Override
    public int getLastSize() {
        return end30;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (token.getConcretenessLabel().equals("Concrete")) return;
        if (!uniqueConcrete.contains(token.getLemma())) uniqueConcrete.add(token.getLemma());
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end25 = start+uniqueConcrete.size()+1;
        end26 = end25+uniqueConcrete.size()+1;
        end27 = end26+uniqueConcrete.size()+1;
        end28 = end27+uniqueConcrete.size()+1;
        end29 = end28+uniqueConcrete.size()+1;
        end30 = end29+uniqueConcrete.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, uniqueConcrete, question.getConcreteLemmas(), end25) +" "+
                getFeature(end25, uniqueConcrete, correctAns.getConcreteLemmas(), end26) +" "+
                getFeature(end26, uniqueConcrete, expl.getConcreteLemmas(), end27) +" "+
                getFeature(end27, uniqueConcrete, getCommon(List.copyOf(question.getConcreteLemmas()), List.copyOf(expl.getConcreteLemmas())), end28) +" "+
                getFeature(end28, uniqueConcrete, getCommon(List.copyOf(correctAns.getConcreteLemmas()), List.copyOf(expl.getConcreteLemmas())), end29) +" "+
                getFeature(end29, uniqueConcrete, getCommon(getGroup(List.copyOf(question.getConcreteLemmas()), List.copyOf(correctAns.getConcreteLemmas())), List.copyOf(expl.getConcreteLemmas())), end30);
    }
}
