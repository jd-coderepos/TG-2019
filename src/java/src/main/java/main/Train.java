package main;

import ling.Utils;
import markup.Explanation;
import markup.QA;
import markup.Sentence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jld
 */
public class Train {

    Map<String, QA> idQa;
    Map<String, Explanation> idExpl;
    Map<String, List<String>> posAnn;
    Map<String, List<String>> negAnn;
    Main main;

    public Train(Main m) {
        main = m;
        idQa = new HashMap<>();
        idExpl = new HashMap<>();
        posAnn = new HashMap<>();
        negAnn = new HashMap<>();
    }

    public void processExpl(List<String[]> lines) {
        for (String[] line : lines) {
            Explanation obj = new Explanation(line[0], line[2]);
            obj.setTokens(line[1].split("\\s"), true, main);
            idExpl.put(line[0], obj);
        }
    }

    public void processQA(List<String[]> lines) {
         for (String[] line : lines) {
             QA obj = new QA(line, true, main);
             idQa.put(line[0], obj);
        }
    }

    public void processPositiveQAExpl(List<String[]> lines) {
        for (String[] line : lines) {
            String quesID = line[0];
            String explID = line[1];
            List<String> annotations = posAnn.get(quesID);
            if (annotations == null) posAnn.put(quesID, annotations = new ArrayList<>());
            annotations.add(explID);
        }
    }

    public void generateNegativeQAExpl(int num) {
        for (String qa : posAnn.keySet()) {
            List<String> posExplIDs = posAnn.get(qa);

            List<String> explIDs = new ArrayList<>(idExpl.keySet());

            explIDs.removeAll(posExplIDs);

            List<Integer> range = IntStream.range(0, explIDs.size()).boxed().collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(range);

            List<String> negExplIDs = negAnn.get(qa);
            if (negExplIDs == null) negAnn.put(qa, negExplIDs = new ArrayList<>());

            for (int i = 0; i < num; i++) {
                negExplIDs.add(explIDs.get(range.get(i)));
            }
        }
    }

    public int iterateExplanations(Integer qid, String qaID, List<String> explIDs, Sentence q, Sentence a, int rank, FileOutputStream output, FileOutputStream idLog) throws IOException {
        for (String explID : explIDs) {
            Explanation expl = idExpl.get(explID);
            Sentence explSent = expl.getSentence();

            String featureStr = Utils.getFeatureStr(main, q, a, explSent, expl.getSource()).replaceAll("\\s+", " ");

            String outputStr = rank+" qid:"+qid+" "+featureStr;
            output.write((outputStr+"\n").getBytes());
            if (idLog != null) idLog.write((qaID+"\t"+explID+"\n").getBytes());

            if(rank != 1) rank--;
        }
        return rank;
    }

    public void writePosAndNegSelectInstances(FileOutputStream output, FileOutputStream idLog) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size()+1;
            rank = iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, idLog);

            if (rank != 1) {
                System.out.println(qaID);
                System.out.println(rank);
                System.out.println("~strange [Train line 108]");
                System.exit(-1);
            }
            explIDs = negAnn.get(qaID);
            iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, idLog);

            qid++;
        }
    }

    public void writeSelectedInstances(FileOutputStream output, Map<String, Map<String, Integer>> annotations) throws IOException {
        int qid = 1;
        for (String qaID : annotations.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            Map<String, Integer> subannotations = annotations.get(qaID);

            for (String explID : subannotations.keySet()) {
                Explanation expl = idExpl.get(explID);
                int rank = subannotations.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource());
                String outputStr = rank + " qid:" + qid + " " + featureStr;
                output.write((outputStr + "\n").getBytes());
            }

            qid++;
        }
    }

    public void writeOutput(FileOutputStream output) throws IOException {
        int qid = 1;
        for (String qaID : posAnn.keySet()) {
            QA qa = idQa.get(qaID);
            Sentence quesSent = qa.getQuestion();
            Sentence ansSent = qa.getCorrectAns();

            List<String> explIDs = posAnn.get(qaID);
            int rank = explIDs.size()+1;
            iterateExplanations(qid, qaID, explIDs, quesSent, ansSent, rank, output, null);

            if (rank != 1) {
                System.out.println("~strange");
                System.exit(-1);
            }
            for (String explID : idExpl.keySet()) {
                if (explIDs.contains(explID)) continue;

                Explanation expl = idExpl.get(explID);
                Sentence explSent = expl.getSentence();

                String featureStr = Utils.getFeatureStr(main, quesSent, ansSent, explSent, expl.getSource());

                String outputStr = rank+" qid:"+qid+" "+featureStr;
                output.write((outputStr+"\n").getBytes());
            }

            qid++;
        }
    }

}
