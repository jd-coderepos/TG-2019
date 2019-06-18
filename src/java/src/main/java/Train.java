import ling.SvmRankFeatures;
import markup.Data;
import markup.Explanation;
import markup.QA;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Train {

    Map<String, QA> idQa;
    Map<String, Explanation> idExpl;
    Map<String, List<String>> posAnn;

    public Train() {
        idQa = new HashMap<>();
        idExpl = new HashMap<>();
        posAnn = new HashMap<>();
    }

    public void processPositiveQAExpl(List<String[]> lines) {
        for (String[] line : lines) {
            List<String> annotations = posAnn.get(line[0]);
            if (annotations == null) posAnn.put(line[0], annotations = new ArrayList<>());
            annotations.add(line[1]);
            System.out.println(line[1]);
            System.exit(-1);
        }
    }

    public void processExpl(List<String[]> lines) {
        for (String[] line : lines) {
            Explanation obj = new Explanation(line[0], line[2]);
            obj.setTokens(line[1].split("\\s"));
            idExpl.put(line[0], obj);
            SvmRankFeatures.populateLists(obj.getSentence());
        }
    }

     public void processQA(List<String[]> lines) {
         for (String[] line : lines) {
             QA obj = new QA(line);
             idQa.put(line[0], obj);
             SvmRankFeatures.populateLists(obj.getQuestion());
             SvmRankFeatures.populateLists(obj.getOptionA());
             SvmRankFeatures.populateLists(obj.getOptionB());
             if (line.length >= 6) SvmRankFeatures.populateLists(obj.getOptionC());
             if (line.length >= 7) SvmRankFeatures.populateLists(obj.getOptionD());
        }
    }

    public void generateTrainingInstances() {

    }

    public void writeTrainingFile(FileOutputStream output) {


    }
}

