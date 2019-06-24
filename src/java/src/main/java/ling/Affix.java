package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class Affix extends Features {

    int start;

    int end11;  //Q prefixes
    int end12;  //A prefixes
    int end13;  //Expl prefixes
    int end14;  //Q + A + Expl prefixes

    int end21;  //Q suffixes
    int end22;  //A suffixes
    int end23;  //Expl suffixes
    int end24;  //Q + A + Expl sufixes

    List<String> prefix = new ArrayList<>();
    List<String> suffix = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end11;
    }

    @Override
    public int getLastSize() {
        return end24;
    }

    @Override
    public void setUniFeatures(Token token) {
        for (String pref : token.getPrefix()) {
            if (!prefix.contains(pref)) prefix.add(pref);
        }
        for (String suff : token.getSuffix()) {
            if (!suffix.contains(suff)) suffix.add(suff);
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+prefix.size();
        end12 = end11+prefix.size();
        end13 = end12+prefix.size();
        end14 = end13+prefix.size();

        end21 = end14+suffix.size();
        end22 = end21+suffix.size();
        end23 = end22+suffix.size();
        end24 = end23+suffix.size();
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, prefix, question.getPrefix(), end11) +" "+
                getFeature(end11, prefix, correctAns.getPrefix(), end12) +" "+
                getFeature(end12, prefix, expl.getPrefix(), end13) +" "+
                getFeature(end13, prefix, getCommon(getGroup(List.copyOf(question.getPrefix()), List.copyOf(correctAns.getPrefix())), List.copyOf(expl.getPrefix())), end14) +" "+
                getFeature(end14, suffix, question.getSuffix(), end21) +" "+
                getFeature(end21, suffix, correctAns.getSuffix(), end22) +" "+
                getFeature(end22, suffix, expl.getSuffix(), end23) +" "+
                getFeature(end23, suffix, getCommon(getGroup(List.copyOf(question.getSuffix()), List.copyOf(correctAns.getSuffix())), List.copyOf(expl.getSuffix())), end24);
    }
}
