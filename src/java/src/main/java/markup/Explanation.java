package markup;

import java.util.List;

/**
 * @author jld
 */
public class Explanation {

    String id;
    Sentence sentence;
    String source;

    public Explanation(String id, String source) {
        this.id = id;
        this.source = source;
    }

    public void setTokens(String[] line) {
        this.sentence = new Sentence();
        this.sentence.setTokens(line);
        this.sentence.setInBetweenPatterns();
        this.sentence.setAggregateFeatures();
    }

    public String getId() {
        return id;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public String getSource() {
        return source;
    }

}
