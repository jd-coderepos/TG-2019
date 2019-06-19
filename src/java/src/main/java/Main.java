import ling.SvmRankFeatures1;
import markup.Explanation;
import markup.QA;
import utils.IO;
import utils.NLP;
import utils.Select;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
public class Main {

    public void initDevSetup(String data_dir) throws IOException {
        Dev dev = new Dev();
        String qa_file = "Elem-Dev-Ling.csv";
        dev.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-final.csv";
        dev.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        String gold_data = "Elem-Dev-Expl.csv";
        dev.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "dev-group.dat";
        String id_log_file = "ids.dat";
        dev.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), new FileOutputStream(data_dir+"\\"+id_log_file));

        /*Select s = new Select();
        String heu_data = "Elem-Dev-Expl-heu1.csv";
        String heu_output_file = "dev_heu1.dat";
        s.init(IO.readCSV(data_dir+"\\"+heu_data, '\t', 0));
        dev.writeSelectedInstances(new FileOutputStream(data_dir+"\\"+heu_output_file), s.getAnnotationsMap());*/

    }

    public void initTrainSetup(String data_dir) throws IOException {
        Train train = new Train();
        String qa_file = "Elem-Train-Ling.csv";
        train.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-final.csv";
        train.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        SvmRankFeatures1.setFeaturesSizes();
        String gold_data = "Elem-Train-Expl.csv";
        train.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "train-group.dat";
        train.writeOutput(new FileOutputStream(data_dir+"\\"+output_file));

        /*Select s = new Select();
        String heu_data = "Elem-Train-Expl-heu1.csv";
        String heu_output_file = "train_heu1.dat";
        s.init(IO.readCSV(data_dir+"\\"+heu_data, '\t', 0));
        train.writeSelectedInstances(new FileOutputStream(data_dir+"\\"+heu_output_file), s.getAnnotationsMap());*/

    }

    public static void main(String[] args) throws IOException {
        String data_dir = "C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data";

        NLP.setStopwords(IO.readFile(data_dir+"\\resources\\stopwords", StandardCharsets.UTF_8).split("\\n"));
        NLP.setConcepts(IO.readCSV(data_dir+"\\resources\\concepts.txt", '\t', 0));

        Main main = new Main();
        main.initTrainSetup(data_dir);
        main.initDevSetup(data_dir);
    }

}
