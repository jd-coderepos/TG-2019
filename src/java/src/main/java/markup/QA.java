package markup;

/**
 * @author jld
 */
public class QA {

    String id;
    Sentence question;
    String correctAns;
    Sentence optionA;
    Sentence optionB;
    Sentence optionC;
    Sentence optionD;

    public QA(String[] line) {
        this.id = line[0];
        this.question = new Sentence();
        this.question.setTokens(line[1].split("\\s"));
        this.question.setAggregateFeatures();
        this.correctAns = line[2];
        this.optionA = new Sentence();
        this.optionA.setTokens(line[3].split("\\s"));
        this.optionA.setAggregateFeatures();
        this.optionB = new Sentence();
        this.optionB.setTokens(line[4].split("\\s"));
        this.optionB.setAggregateFeatures();
        if (line.length >= 6) {
            this.optionC = new Sentence();
            this.optionC.setTokens(line[5].split("\\s"));
            this.optionC.setAggregateFeatures();
        }
        if (line.length >= 7) {
            this.optionD = new Sentence();
            this.optionD.setTokens(line[6].split("\\s"));
            this.optionD.setAggregateFeatures();
        }
    }

    public Sentence getCorrectAns() {
        if (correctAns.equals("A")) return optionA;
        else if (correctAns.equals("B")) return optionB;
        else if (correctAns.equals("C")) return optionC;
        else return optionD;
    }

    public String getId() {
        return id;
    }

    public Sentence getQuestion() {
        return question;
    }

    public Sentence getOptionA() {
        return optionA;
    }

    public Sentence getOptionB() {
        return optionB;
    }

    public Sentence getOptionC() {
        return optionC;
    }

    public Sentence getOptionD() {
        return optionD;
    }

}
