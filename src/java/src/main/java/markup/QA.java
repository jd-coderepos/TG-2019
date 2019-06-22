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

    /*public QA(String[] line, boolean train, Main main) {
        this.qaID = line[0];
        //System.out.println(line[0]);
        this.question = new Sentence();
        this.question.setTokens(line[1].split("\\s"), train, main);
        this.correctAnsOption = line[2];
        this.correctAns = new Sentence();
        this.correctAns.setTokens(line[getCorrectAns(line)].split("\\s"), train, main);
    }*/

    public QA(String[] line, boolean train, Main main) {
        this.qaID = line[0];
        this.question = new Sentence();
        this.question.setTokens(line[1].split("\\s"), train, main);
        this.question.setRelations(line.length > 3 ? line[3] : null, train, main);

        this.correctAns = new Sentence();
        this.correctAns.setTokens(line[2].split("\\s"), train, main);
        this.correctAns.setRelations(line.length > 4 ? line[4] : null, train, main);

        /*if (line.length > 3) {
            String[] quesRels = line[3].split("/");
            for (String quesRel : quesRels) {
                String[] relComponents = quesRel.split("\\|");

                //first two subj


                //next two obj

                //next two rel

                //next two triple

            }

            if (line.length == 5) {
                String[] ansRels  = line[4].split("/");
                for (String ansRel : ansRels) {
                    String[] relComponents = ansRel.split("\\|");

                    //first two subj

                    //next two obj

                    //next two rel

                    //next two triple

                }
            }
        }*/
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
