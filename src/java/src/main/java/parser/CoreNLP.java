package parser;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * @author jld
 */
public class CoreNLP {

    StanfordCoreNLP pipeline;

    public CoreNLP() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        props.put("ner.model", "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
        props.put("ner.applyNumericClassifiers", "true");
        pipeline = new StanfordCoreNLP(props);
    }

}
