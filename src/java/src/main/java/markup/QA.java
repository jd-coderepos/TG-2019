package markup;

import main.Main;

/**
 * @author jld
 */
public class QA {

    String qaID;
    Sentence question;
    String correctAnsOption;
    Sentence correctAns;

    public QA(String[] line, boolean train, Main main) {
        this.qaID = line[0];
        //System.out.println(line[0]);
        this.question = new Sentence();
        this.question.setTokens(line[1].split("\\s"), train, main);
        this.correctAnsOption = line[2];
        this.correctAns = new Sentence();
        this.correctAns.setTokens(line[getCorrectAns(line)].split("\\s"), train, main);
    }

    public int getCorrectAns(String[] tokens) {
        if (correctAnsOption.equals("A")) return 3;
        else if (correctAnsOption.equals("B")) return 4;
        else if (correctAnsOption.equals("C")) return 5;
        else return 6;
    }

    public String getQaID() {
        return qaID;
    }

    public Sentence getQuestion() {
        return question;
    }

    public String getCorrectAnsOption() {
        return correctAnsOption;
    }

    public Sentence getCorrectAns() {
        return correctAns;
    }

}
