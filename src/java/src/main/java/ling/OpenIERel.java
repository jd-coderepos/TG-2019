package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class OpenIERel extends Features {

    int start;

    //only consider non-stop terms

    //int end11; //Q word-wise relations
    int end21; //Q lemma-wise relations
    //int end31; //A word-wise relations
    int end41; //A lemma-wise relations
    //int end51; //Expl word-wise relations
    int end61; //Expl lemma-wise relations

    //int end12; //Q subject words
    int end22; //Q subject lemmas
    //int end32; //A subject words
    int end42; //A subject lemmas
    //int end52; //Expl subject words
    int end62; //Expl subject lemmas
    //int end72; //Q + A + Expl subject words
    int end82; //Q + A + Expl subject lemmas

    //int end13; //Q object words
    int end23; //Q object lemmas
    //int end33; //A object words
    int end43; //A object lemmas
    //int end53; //Expl object words
    int end63; //Expl object lemmas
    //int end73; //Q + A + Expl object words
    int end83; //Q + A + Expl object lemmas

    //List<String> relation_words = new ArrayList<>();
    List<String> relation_lemma = new ArrayList<>();
    //List<String> subject_words = new ArrayList<>();
    List<String> subject_lemma = new ArrayList<>();
    //List<String> object_words = new ArrayList<>();
    List<String> object_lemma = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end21;
    }

    @Override
    public int getLastSize() {
        return end83;
    }

    @Override
    public void setUniFeatures(Token token) {

    }

    public void setUniFeatures(Sentence sentence) {
        /*if (!sentence.getRel_words().isEmpty())  {
            for (String rel_word : sentence.getRel_words()) {
                if (!relation_words.contains(rel_word)) relation_words.add(rel_word);
            }
        }*/
        if (!sentence.getRel_lemmas().isEmpty()) {
            for (String rel_lemma : sentence.getRel_lemmas()) {
                if (!relation_lemma.contains(rel_lemma)) relation_lemma.add(rel_lemma);
            }
        }
        /*if (!sentence.getSubj_words().isEmpty()) {
            for (String subj_word : sentence.getSubj_words()) {
                if (!subject_words.contains(subj_word)) subject_words.add(subj_word);
            }
        }*/
        if (!sentence.getSubj_lemmas().isEmpty()) {
            for (String subj_lemma : sentence.getSubj_lemmas()) {
                if (!subject_lemma.contains(subj_lemma)) subject_lemma.add(subj_lemma);
            }
        }
        /*if (!sentence.getObj_words().isEmpty()) {
            for (String obj_word : sentence.getObj_words()) {
                if (!object_words.contains(obj_word)) object_words.add(obj_word);
            }
        }*/
        if (!sentence.getObj_lemmas().isEmpty()) {
            for (String obj_lemma : sentence.getObj_lemmas()) {
                if (!object_lemma.contains(obj_lemma)) object_lemma.add(obj_lemma);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        //end11 = start+relation_words.size()+1;
        end21 = start+relation_lemma.size()+1;
        //end31 = end21+relation_words.size()+1;
        end41 = end21+relation_lemma.size()+1;
        //end51 = end41+relation_words.size()+1;
        end61 = end41+relation_lemma.size()+1;

        //end12 = end61+subject_words.size()+1;
        end22 = end61+subject_lemma.size()+1;
        //end32 = end22+subject_words.size()+1;
        end42 = end22+subject_lemma.size()+1;
        //end52 = end42+subject_words.size()+1;
        end62 = end42+subject_lemma.size()+1;
        //end72 = end62+subject_words.size()+1;
        end82 = end62+subject_lemma.size()+1;

        //end13 = end82+object_words.size()+1;
        end23 = end82+object_lemma.size()+1;
        //end33 = end23+object_words.size()+1;
        end43 = end23+object_lemma.size()+1;
        //end53 = end43+object_words.size()+1;
        end63 = end43+object_lemma.size()+1;
        //end73 = end63+object_words.size()+1;
        end83 = end63+object_lemma.size()+1;
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return //getFeature(start, relation_words, question.getRel_words(), end11) +" "+
                getFeature(this.start, relation_lemma, question.getRel_lemmas(), end21) +" "+
                //getFeature(end21, relation_words, correctAns.getRel_words(), end31) +" "+
                getFeature(end21, relation_lemma, correctAns.getRel_lemmas(), end41) +" "+
                //getFeature(end41, relation_words, expl.getRel_words(), end51) +" "+
                getFeature(end41, relation_lemma, expl.getRel_lemmas(), end61) +" "+

                //getFeature(end61, subject_words, question.getSubj_words(), end12) +" "+
                getFeature(end61, subject_lemma, question.getSubj_lemmas(), end22) +" "+
                //getFeature(end22, subject_words, correctAns.getSubj_words(), end32) +" "+
                getFeature(end22, subject_lemma, correctAns.getSubj_lemmas(), end42) +" "+
                //getFeature(end42, subject_words, expl.getSubj_words(), end52) +" "+
                getFeature(end42, subject_lemma, expl.getSubj_lemmas(), end62) +" "+
                //getFeature(end62, subject_words, getCommon(getGroup(List.copyOf(question.getSubj_words()), List.copyOf(correctAns.getSubj_words())), List.copyOf(expl.getSubj_words())), end72) +" "+
                getFeature(end62, subject_lemma, getCommon(getGroup(List.copyOf(question.getSubj_lemmas()), List.copyOf(correctAns.getSubj_lemmas())), List.copyOf(expl.getSubj_lemmas())), end82) +" "+

                //getFeature(end82, object_words, question.getObj_words(), end13) +" "+
                getFeature(end82, object_lemma, question.getObj_lemmas(), end23) +" "+
                //getFeature(end23, object_words, correctAns.getObj_words(), end33) +" "+
                getFeature(end23, object_lemma, correctAns.getObj_lemmas(), end43) +" "+
                //getFeature(end43, object_words, expl.getObj_words(), end53) +" "+
                getFeature(end43, object_lemma, expl.getObj_lemmas(), end63) +" "+
                //getFeature(end63, object_words, getCommon(getGroup(List.copyOf(question.getObj_words()), List.copyOf(correctAns.getObj_words())), List.copyOf(expl.getObj_words())), end73) +" "+
                getFeature(end63, object_lemma, getCommon(getGroup(List.copyOf(question.getObj_lemmas()), List.copyOf(correctAns.getObj_lemmas())), List.copyOf(expl.getObj_lemmas())), end83);
    }
}
