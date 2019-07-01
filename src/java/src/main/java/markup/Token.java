package markup;

import utils.NLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Token {

    String word;
    String lemma;
    String pos;
    String concretenessLabel;
    Double concretenessScore;
    List<String> prefix;
    List<String> suffix;
    List<String> concepts;
    List<String> outDeprel;
    List<String> outLemmaDeprel;
    List<String> inDeprel;
    List<String> inLemmaDeprel;

    List<String> cn_wordrelations;

    List<String> wikicategories;

    List<String> wikititles;

    public Token(String w, String l, String p, String cl, Double cs, List<String> concepts) {
        this.word = w;
        this.lemma = l;
        this.pos = p;
        this.concretenessLabel = cl;
        this.concretenessScore = cs;

        this.prefix = new ArrayList<>();
        this.suffix = new ArrayList<>();
        setAffix(w);

        this.concepts = concepts;
        this.outDeprel = new ArrayList<>();
        this.outLemmaDeprel = new ArrayList<>();
        this.inDeprel = new ArrayList<>();
        this.inLemmaDeprel = new ArrayList<>();

        cn_wordrelations = new ArrayList<>();
        wikicategories = new ArrayList<>();
        wikititles = new ArrayList<>();
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

    public void setCn_wordrelations(String word) {
        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(word);
        if (relatedTerms != null) {
            for (String relation : relatedTerms.keySet()) {
                List<String> terms = relatedTerms.get(relation);
                for (String term : terms) {
                    if (!cn_wordrelations.contains(relation + "-" + term)) cn_wordrelations.add(relation + "-" + term);
                }
            }
        }
    }

    public void setWikicategories(String word) {
        List<String> categories = NLP.wikiCategories.get(word);
        if (categories != null) {
            for (String category : categories) {
                if (!wikicategories.contains(category)) wikicategories.add(category);
            }
        }
    }

    public void setWikititles(String word) {
        List<String> titles = NLP.wikiTitles.get(word);
        if (titles != null) {
            for (String title : titles) {
                if (!wikititles.contains(title)) wikititles.add(title);
            }
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

    public void setAffix(String word) {
        String w = word.toLowerCase();
        int length = w.length() >= 5 ? 5 : w.length() >= 4 ? 4 : w.length() >= 3 ? 3 : 0;
        setAffix(length, w);
    }

    public void setAffix(int length, String str) {
        switch(length) {
            case 5:
                if (!prefix.contains(str.substring(0,5))) prefix.add(str.substring(0, 5));
                if (!suffix.contains(str.substring(str.length()-5))) suffix.add(str.substring(str.length()-5));
            case 4:
                if (!prefix.contains(str.substring(0,4))) prefix.add(str.substring(0, 4));
                if (!suffix.contains(str.substring(str.length()-4))) suffix.add(str.substring(str.length()-4));
            case 3:
                if (!prefix.contains(str.substring(0,3))) prefix.add(str.substring(0, 3));
                if (!suffix.contains(str.substring(str.length()-3))) suffix.add(str.substring(str.length()-3));
        }
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
    }

    public List<String> getCn_wordrelations() {
        return cn_wordrelations;
    }

    public List<String> getWikicategories() {
        return wikicategories;
    }

    public List<String> getWikititles() {
        return wikititles;
    }

}

