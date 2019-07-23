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
    Concepts con;
    OpenIERel openRel;

    ConceptNetRelations cnrel;

    WikiCategories wikicat;

    WikiTitles wikit;

    FrameNet1_7 frameNet1_7;

    WordNet wn;

    TAG tag;

    public Main() {
        lemma = new Lemma();

        ts = new TableStore();

        affix = new Affix();

        con = new Concepts();
        openRel = new OpenIERel();

        dep = new Dependencies();

        cnrel = new ConceptNetRelations();

        wikicat = new WikiCategories();

        wikit = new WikiTitles();

        frameNet1_7 = new FrameNet1_7();

        wn = new WordNet();

        tag = new TAG();
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

    public Concepts getCon() {
        return con;
    }

    public OpenIERel getOpenRel() {
        return openRel;
    }

    public ConceptNetRelations getCnrel() {
        return cnrel;
    }

    public WikiCategories getWikicat() {
        return wikicat;
    }

    public WikiTitles getWikit() {
        return wikit;
    }

    public FrameNet1_7 getFrameNet1_7() {
        return frameNet1_7;
    }

    public WordNet getWordNet() {
        return wn;
    }

    public TAG getTag() {
        return tag;
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
        openRel.setFeatureSizes(con.getLastSize());
        cnrel.setFeatureSizes(openRel.getLastSize());
        wikicat.setFeatureSizes(cnrel.getLastSize());
        wikit.setFeatureSizes(wikicat.getLastSize());
        frameNet1_7.setFeatureSizes(wikit.getLastSize());
        //tag.setFeatureSizes(frameNet1_7.getLastSize());

        System.out.println("Done generating training features!");

        String gold_data = "Elem-Train-Expl.csv";
        train.processPositiveQAExpl(IO.readCSV(data_dir+"\\"+gold_data, '\t', 0));
        String output_file = "train-"+numNeg+"-"+feat_grp+".dat";
        //String id_file = "train-ids-"+numNeg+"-"+feat_grp+".txt";
        String id_file = "train-ids.txt";
        //train.writeOutput(new FileOutputStream(data_dir+"\\"+output_file));
        //train.generateNegativeQAExpl(numNeg);

        train.setNegAnn(IO.readCSV(data_dir+"\\"+id_file, '\t', 0));
        train.writePosAndNegIDs(new FileOutputStream(data_dir+"\\training_data.txt"));
        train.writePosAndNegSelectInstances(new FileOutputStream(data_dir+"\\"+output_file),
                /*new FileOutputStream(data_dir+"\\"+id_file)*/null);

        System.out.println("Done writing training data!");
    }

    public static void main(String[] args) throws IOException {
        String data_dir = "C:\\Users\\DSouzaJ\\Desktop\\Code\\TG-2019\\data";

        NLP.setStopwords(IO.readFile(data_dir+"\\resources\\stopwords", StandardCharsets.UTF_8).split("\\n"));
        NLP.setConcepts(IO.readCSV(data_dir+"\\resources\\concepts.txt", '\t', 0));
        NLP.setConceptRelations(IO.readCSV(data_dir+"\\resources\\wordtriples.txt", '\t', 0));
        NLP.setWikiCategories(IO.readCSV(data_dir+"\\resources\\wiki-concept-categories.txt", '\t', 0));
        NLP.setWikiTitles(IO.readCSV(data_dir+"\\resources\\wiki-concept-search-titles.txt", '\t', 0));

        NLP.setFrames1_7(IO.readFile(data_dir+"\\resources\\framenet\\predicted-args-train.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_7(IO.readFile(data_dir+"\\resources\\framenet\\predicted-args-dev.txt", StandardCharsets.UTF_8).split("\\n"));
        NLP.setFrames1_7(IO.readFile(data_dir+"\\resources\\framenet\\predicted-args-expl.txt", StandardCharsets.UTF_8).split("\\n"));

        Main main = new Main();
        //NLP.setTAGFeat(IO.readFile(data_dir+"\\resources\\qae_TAG.txt", StandardCharsets.UTF_8).split("\\n"), main.getTag());

        String feat_grp = "framenet";
        main.initTrainSetup(data_dir, 500, feat_grp);
        main.initDevSetup(data_dir, feat_grp);
    }

}
