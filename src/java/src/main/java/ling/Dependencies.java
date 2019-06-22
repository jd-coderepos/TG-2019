package ling;

import markup.Sentence;
import markup.Token;

import java.util.ArrayList;
import java.util.List;

import static ling.Utils.*;

/**
 * @author jld
 */
public class Dependencies extends Features {

    int start;

    int end33;    //Q in dep relations
    int end34;    //A in dep relations
    int end35;    //Expl in dep relations
    int end36;    //Q+A+Expl in dep relations

    int end41;    //Q out dep relations
    int end42;    //A out dep relations
    int end43;    //Expl out dep relations
    int end44;    //Q+A+Expl out dep relations

    public List<String> indepRel = new ArrayList<>();
    public List<String> inlemmaDepRel = new ArrayList<>();

    public List<String> outdepRel = new ArrayList<>();
    public List<String> outlemmaDepRel = new ArrayList<>();

    @Override
    public int getFirstSize(){
        return end33;
    }

    @Override
    public int getLastSize() {
        return end44;
    }

    @Override
    public void setUniFeatures(Token token) {
        if (!token.getInDeprel().isEmpty()) {
            for (String dr : token.getInDeprel()) {
                if (!indepRel.contains(dr)) indepRel.add(dr);
            }
            for (String ldr : token.getInLemmaDeprel()) {
                if (!inlemmaDepRel.contains(ldr)) inlemmaDepRel.add(ldr);
            }
        }
        if (!token.getOutDeprel().isEmpty()) {
            for (String dr : token.getOutDeprel()) {
                if (!outdepRel.contains(dr)) outdepRel.add(dr);
            }
            for (String ldr : token.getOutLemmaDeprel()) {
                if (!outlemmaDepRel.contains(ldr)) outlemmaDepRel.add(ldr);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;
        end33 = start+indepRel.size()+1;
        end34 = end33+indepRel.size()+1;
        end35 = end34+indepRel.size()+1;
        end36 = end35+indepRel.size()+1;

        /*end37 = end36+inlemmaDepRel.size()+1;
        end38 = end37+inlemmaDepRel.size()+1;
        end39 = end38+inlemmaDepRel.size()+1;
        end40 = end39+inlemmaDepRel.size()+1;*/

        end41 = end36+outdepRel.size()+1;
        end42 = end41+outdepRel.size()+1;
        end43 = end42+outdepRel.size()+1;
        end44 = end43+outdepRel.size()+1;

        /*end45 = end44+outlemmaDepRel.size()+1;
        end46 = end45+outlemmaDepRel.size()+1;
        end47 = end46+outlemmaDepRel.size()+1;
        end48 = end47+outlemmaDepRel.size()+1;*/
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(start, indepRel, question.getInDepRels(), end33) +" "+
                getFeature(end33, indepRel, correctAns.getInDepRels(), end34) +" "+
                getFeature(end34, indepRel, expl.getInDepRels(), end35) +" "+
                getFeature(end35, indepRel, getCommon(getGroup(List.copyOf(question.getInDepRels()), List.copyOf(correctAns.getInDepRels())), List.copyOf(expl.getInDepRels())), end36) +" "+
                /*getFeature(end36, inlemmaDepRel, question.getInlemmaDepRels(), end37) +" "+
                getFeature(end37, inlemmaDepRel, correctAns.getInlemmaDepRels(), end38) +" "+
                getFeature(end38, inlemmaDepRel, expl.getInlemmaDepRels(), end39) +" "+
                getFeature(end39, inlemmaDepRel, getCommon(getGroup(List.copyOf(question.getInlemmaDepRels()), List.copyOf(correctAns.getInlemmaDepRels())), List.copyOf(expl.getInlemmaDepRels())), end40) +" "+*/
                getFeature(end36, outdepRel, question.getOutDepRels(), end41) +" "+
                getFeature(end41, outdepRel, correctAns.getOutDepRels(), end42) +" "+
                getFeature(end42, outdepRel, expl.getOutDepRels(), end43) +" "+
                getFeature(end43, outdepRel, getCommon(getGroup(List.copyOf(question.getOutDepRels()), List.copyOf(correctAns.getOutDepRels())), List.copyOf(expl.getOutDepRels())), end44);
                /*getFeature(end44, outlemmaDepRel, question.getOutlemmaDepRels(), end45) +" "+
                getFeature(end45, outlemmaDepRel, correctAns.getOutlemmaDepRels(), end46) +" "+
                getFeature(end46, outlemmaDepRel, expl.getOutlemmaDepRels(), end47) +" "+
                getFeature(end47, outlemmaDepRel, getCommon(getGroup(List.copyOf(question.getOutlemmaDepRels()), List.copyOf(correctAns.getOutlemmaDepRels())), List.copyOf(expl.getOutlemmaDepRels())), end48);*/
    }

}
