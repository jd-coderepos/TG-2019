package markup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jld
 */
public class Token {

    String word;
    String lemma;
    String pos;
    String concretenessLabel;
    Double concretenessScore;
    List<String> outDeprel;
    List<String> outDeprelLemma;
    List<String> inDeprel;
    List<String> inDeprelLemma;

    public Token(String w, String l, String p, String cl, Double cs) {
        this.word = w;
        this.lemma = l;
        this.pos = p;
        this.concretenessLabel = cl;
        this.concretenessScore = cs;
        this.outDeprel = new ArrayList<>();
        this.outDeprelLemma = new ArrayList<>();
        this.inDeprel = new ArrayList<>();
        this.inDeprelLemma = new ArrayList<>();
    }

    public void setDepRel(String[] tokens, boolean out) {
        if (tokens.length == 1) return;
        for (int i = 0; i < tokens.length; i = i+2) {
            String l = tokens[i];
            String rel = tokens[i+1];
            if (out) outDeprel.add(rel);
            else inDeprel.add(rel);
            if (out) outDeprelLemma.add(l+"-"+rel);
            else inDeprelLemma.add(l+"-"+rel);
        }
    }

    public String getWord() {
        return word;
    }

    public String getLemma() {
        return lemma;
    }

    public String getPos() {
        return pos;
    }

    public String getConcretenessLabel() {
        return concretenessLabel;
    }

    public Double getConcretenessScore() {
        return concretenessScore;
    }

    public List<String> getOutDeprel() {
        return outDeprel;
    }

    public List<String> getOutDeprelLemma() {
        return outDeprelLemma;
    }

    public List<String> getInDeprel() {
        return inDeprel;
    }

    public List<String> getInDeprelLemma() {
        return inDeprelLemma;
    }

}
