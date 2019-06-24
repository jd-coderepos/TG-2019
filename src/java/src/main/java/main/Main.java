package main;

import ling.*;
import utils.IO;
import utils.NLP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author jld
 */
public class Main {

    //Features
    Lemma lemma;
    TableStore ts;

    Affix affix;

    Dependencies dep;
    PosTags pt;
    Concepts con;
    AbstractConcepts acon;
    FocusConcepts fcon;
    ConcreteConcepts ccon;
    OpenIERel openRel;
    ConceptNet cn;

    ConceptNetSynonyms cnsyn;
    ConceptNetIsA cnisa;
    ConceptNetSimilar cnsim;
    ConceptNetRelatedTo cnrelto;
    ConceptNetRelations cnrel;
    ConceptNetRelatedWordCloud rwc;

    public Main() {
        lemma = new Lemma();
        ts = new TableStore();

        affix = new Affix();

        dep = new Dependencies();
        pt = new PosTags();
        con = new Concepts();
        acon = new AbstractConcepts();
        fcon = new FocusConcepts();
        ccon = new ConcreteConcepts();
        openRel = new OpenIERel();
        cn = new ConceptNet();

        cnsyn = new ConceptNetSynonyms();
        cnisa = new ConceptNetIsA();
        cnsim = new ConceptNetSimilar();
        cnrelto = new ConceptNetRelatedTo();
        cnrel = new ConceptNetRelations();
        rwc = new ConceptNetRelatedWordCloud();
    }

    public Lemma getLemma() {
        return lemma;
    }

    public TableStore getTs() {
        return ts;
    }

    public Affix getAffix() {
        return affix;
    }

    public Dependencies getDep() {
        return dep;
    }

    public PosTags getPosTags() {
        return pt;
    }

    public Concepts getCon() {
        return con;
    }

    public AbstractConcepts getACon() {
        return acon;
    }

    public FocusConcepts getFCon() {
        return fcon;
    }

    public ConcreteConcepts getCcon() {
        return ccon;
    }

    public OpenIERel getOpenRel() {
        return openRel;
    }

    public ConceptNet getConceptNet() {
        return cn;
    }

    public ConceptNetSynonyms getCnsyn() {
        return cnsyn;
    }

    public ConceptNetIsA getCnisa() {
        return cnisa;
    }

    public ConceptNetSimilar getCnsim() {
        return cnsim;
    }

    public ConceptNetRelatedTo getCnrelto() {
        return cnrelto;
    }

    public ConceptNetRelations getCnrel() {
        return cnrel;
    }

    public ConceptNetRelatedWordCloud getRwc() {
        return rwc;
    }

    public void initDevSetup(String data_dir, String feat_grp) throws IOException {
        Dev dev = new Dev(this);
        String qa_file = "Elem-Dev-Rel.csv";
        dev.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        String expl_file = "expl-tablestore-rel.csv";
        dev.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        String gold_data = "Elem-Dev-Expl.csv";
        dev.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "dev-"+feat_grp+".dat";
        String id_file = "dev-ids-"+feat_grp+".txt";
        //dev.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), new FileOutputStream(data_dir+"\\"+id_file));
        dev.writeOutput(new FileOutputStream(data_dir+"\\"+output_file), null);
    }

    public void initTrainSetup(String data_dir, int numNeg, String feat_grp) throws IOException {
        Train train = new Train(this);
        //String qa_file = "Elem-Train-Ling.csv";
        String qa_file = "Elem-Train-Rel.csv";
        train.processQA(IO.readCSV(data_dir+"\\"+qa_file, '\t', 0));
        //String expl_file = "expl-tablestore-ling.csv";
        String expl_file = "expl-tablestore-rel.csv";
        train.processExpl(IO.readCSV(data_dir+"\\"+expl_file, '\t', 0));
        lemma.setFeatureSizes(0);
        ts.setFeatureSizes(lemma.getLastSize());
        affix.setFeatureSizes(ts.getLastSize());
        con.setFeatureSizes(affix.getLastSize());
        //pt.setFeatureSizes(con.getLastSize());
        //dep.setFeatureSizes(con.getLastSize());
        //openRel.setFeatureSizes(con.getLastSize());
        //cn.setFeatureSizes(openRel.getLastSize());

        //cnsyn.setFeatureSizes(openRel.getLastSize());
        //cnisa.setFeatureSizes(openRel.getLastSize());
        //cnsim.setFeatureSizes(openRel.getLastSize());
        //cnrelto.setFeatureSizes(cnsim.getLastSize());
        //cnrel.setFeatureSizes(openRel.getLastSize());
        //rwc.setFeatureSizes(openRel.getLastSize());

        System.out.println("Done generating training features!");

        String gold_data = "Elem-Train-Expl.csv";
        train.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "train-"+numNeg+"-"+feat_grp+".dat";
        //String id_file = "train-ids-"+numNeg+"-"+feat_grp+".txt";
        String id_file = "train-ids.txt";
        //train.writeOutput(new FileOutputStream(data_dir+"\\"+output_file));

        train.setNegAnn(IO.readCSV(data_dir+"\\"+id_file, '\t', 0));
        train.generateNegativeQAExpl(numNeg);
        train.writePosAndNegSelectInstances(new FileOutputStream(data_dir+"\\"+output_file),
                /*new FileOutputStream(data_dir+"\\"+id_file)*/null);

        System.out.println("Done writing training data!");
        /*Select s = new Select();
        String heu_data = "Elem-main.Train-Expl-heu1.csv";
        String heu_output_file = "train_heu1.dat";
        s.init(IO.readCSV(data_dir+"\\"+heu_data, '\t', 0));
        train.writeSelectedInstances(new FileOutputStream(data_dir+"\\"+heu_output_file), s.getAnnotationsMap());*/

    }

    public static void main(String[] args) throws IOException {
        String data_dir = "C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data";

        NLP.setStopwords(IO.readFile(data_dir+"\\resources\\stopwords", StandardCharsets.UTF_8).split("\\n"));
        NLP.setConcepts(IO.readCSV(data_dir+"\\resources\\concepts.txt", '\t', 0));
        //NLP.setConceptRelations(IO.readCSV(data_dir+"\\resources\\conceptnet\\wordlemmatriples.txt", '\t', 0));
        //NLP.setConceptRelations(IO.readCSV(data_dir+"\\resources\\conceptnet\\wordtriples.txt", '\t', 0));

        Main main = new Main();
        //String feat_grp = "lemma-con-dep-rel-v2-cn";
        String feat_grp = "lemma-affix-con";
        main.initTrainSetup(data_dir, 500, feat_grp);
        main.initDevSetup(data_dir, feat_grp);
    }

}
