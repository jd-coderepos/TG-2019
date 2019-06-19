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
    List<String> concepts;
    List<String> outDeprel;
    List<String> outLemmaDeprel;
    List<String> inDeprel;
    List<String> inLemmaDeprel;

    public Token(String w, String l, String p, String cl, Double cs, List<String> concepts) {
        this.word = w;
        this.lemma = l;
        this.pos = p;
        this.concretenessLabel = cl;
        this.concretenessScore = cs;
        this.concepts = concepts;
        this.outDeprel = new ArrayList<>();
        this.outLemmaDeprel = new ArrayList<>();
        this.inDeprel = new ArrayList<>();
        this.inLemmaDeprel = new ArrayList<>();
    }

    public void setDepRel(String[] tokens, boolean out) {
        if (tokens.length == 1) return;
        for (int i = 0; i < tokens.length; i = i+2) {
            String l = tokens[i];
            String rel = tokens[i+1];
            if (out) outDeprel.add(rel);
            else inDeprel.add(rel);
            if (out) outLemmaDeprel.add(l+"-"+rel);
            else inLemmaDeprel.add(l+"-"+rel);
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

    public List<String> getConcepts() {
        return concepts;
    }

    public List<String> getOutDeprel() {
        return outDeprel;
    }

    public List<String> getOutLemmaDeprel() {
        return outLemmaDeprel;
    }

    public List<String> getInDeprel() {
        return inDeprel;
    }

    public List<String> getInLemmaDeprel() {
        return inLemmaDeprel;
    }

}
