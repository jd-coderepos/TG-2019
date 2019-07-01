package markup;

import main.Main;
import utils.NLP;

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

    public Explanation(String[] line, boolean train, Main main) {
        this.id = line[0];
        this.source = line[2];
        this.sentence = new Sentence();
        if (NLP.frames.containsKey(this.id)) {
            String frameStr = NLP.frames.get(this.id);
            this.sentence.setFrames(frameStr, train, main);
        }
        this.sentence.setTokens(line[1].split("\\s"), train, main);
        this.sentence.setRelations(line.length > 3 ? line[3] : null, train, main);

        if (train) {
            main.getTs().setTablestore(source);
        }
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
