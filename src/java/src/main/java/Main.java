import markup.Explanation;
import markup.QA;
import utils.IO;
import utils.NLP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Main {

    public static void main(String[] args) throws IOException {
        NLP.setStopwords(IO.readFile("C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data\\resources\\stopwords", StandardCharsets.UTF_8).split("\\n"));

        String data_dir = "C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data";
        String qa_file = "Elem-Train-Ling.csv";
        String expl_file = "expl-tablestore-final.csv";
        String gold_data = "Elem-Train-Expl.csv";

        Train train = new Train();
        train.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        train.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        train.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
    }

}
