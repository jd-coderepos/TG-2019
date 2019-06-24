package ling;

import markup.Sentence;
import markup.Token;
import utils.NLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ling.Utils.*;

/**
 * @author jld
 */
public class ConceptNet extends Features {

    int start;

    int end11; //Q lemma synonyms
    int end21; //A lemma synonyms
    int end31; //Expl lemma synonyms
    int end41; //Q+A+Expl lemma synonyms

    int end12; //Q lemma isA
    int end22; //A lemma isA
    int end32; //Expl lemma isA
    int end42; //Q+A+Expl lemma isA

    int end13; //Q lemma similar
    int end23; //A lemma similar
    int end33; //Expl lemma similar
    int end43; //Q+A+Expl lemma similar

    int end14; //Q lemma related to
    int end24; //A lemma related to
    int end34; //Expl lemma related to
    int end44; //Q+A+Expl lemma related to

    int end15; //Q lemma relations (all remaining relations)
    int end25; //A lemma relations
    int end35; //Expl lemma relations
    int end45; //Q+A+Expl lemma relations

    int end16; //Q all other related words
    int end26; //A all other related words
    int end36; //Expl all other related words
    int end46; //Q+A+Expl all other related words

    List<String> synonyms = new ArrayList<>();
    List<String> isA = new ArrayList<>();
    List<String> similar = new ArrayList<>();
    List<String> relatedTo = new ArrayList<>();

    List<String> relations = new ArrayList<>();
    List<String> relatedWordCloud = new ArrayList<>();

    @Override
    public int getFirstSize() {
        return end11;
    }

    @Override
    public int getLastSize() {
        return end46;
    }

    @Override
    public void setUniFeatures(Token token) {
        Map<String, List<String>> relatedTerms = NLP.conceptRelations.get(token.getWord().toLowerCase());
        if (relatedTerms == null) return;
        //Synonym
        if (relatedTerms.containsKey("Synonym")) {
            List<String> terms = relatedTerms.get("Synonym");
            for (String term : terms) {
                if (!synonyms.contains(term)) synonyms.add(term);
            }
        }
        //IsA
        if (relatedTerms.containsKey("IsA")) {
            List<String> terms = relatedTerms.get("IsA");
            for (String term : terms) {
                if (!isA.contains(term)) isA.add(term);
            }
        }
        //Similar
        if (relatedTerms.containsKey("Similar")) {
            List<String> terms = relatedTerms.get("Similar");
            for (String term : terms) {
                if (similar.contains(term)) similar.add(term);
            }
        }
        //RelatedTo
        if (relatedTerms.containsKey("RelatedTo")) {
            List<String> terms = relatedTerms.get("RelatedTo");
            for (String term : terms) {
                if (relatedTo.contains(term)) relatedTo.add(term);
            }
        }

        for (String relation : relatedTerms.keySet()) {
            if (relation.equals("Synonym") || relation.equals("IsA") || relation.equals("Similar") || relation.equals("RelatedTo")) continue;

            if (!relations.contains(relation)) relations.add(relation);

            List<String> terms = relatedTerms.get(relation);
            for (String term : terms) {
                if (!relatedWordCloud.contains(term)) relatedWordCloud.add(term);
            }
        }
    }

    @Override
    public void setFeatureSizes(int start) {
        this.start = start;

        end11 = start+synonyms.size(); //Q lemma synonyms
        end21 = end11+synonyms.size(); //A lemma synonyms
        end31 = end21+synonyms.size(); //Expl lemma synonyms
        end41 = end31+synonyms.size(); //Q+A+Expl lemma synonyms

        end12 = end41+isA.size(); //Q lemma isA
        end22 = end12+isA.size(); //A lemma isA
        end32 = end22+isA.size(); //Expl lemma isA
        end42 = end32+isA.size(); //Q+A+Expl lemma isA

        end13 = end42+similar.size(); //Q lemma similar
        end23 = end13+similar.size(); //A lemma similar
        end33 = end23+similar.size(); //Expl lemma similar
        end43 = end33+similar.size(); //Q+A+Expl lemma similar

        end14 = end43+relatedTo.size(); //Q lemma related to
        end24 = end14+relatedTo.size(); //A lemma related to
        end34 = end24+relatedTo.size(); //Expl lemma related to
        end44 = end34+relatedTo.size(); //Q+A+Expl lemma related to

        end15 = end44+relations.size(); //Q lemma relations (all remaining relations)
        end25 = end15+relations.size(); //A lemma relations
        end35 = end25+relations.size(); //Expl lemma relations
        end45 = end35+relations.size(); //Q+A+Expl lemma relations

        end16 = end45+relatedWordCloud.size(); //Q all other related words
        end26 = end16+relatedWordCloud.size(); //A all other related words
        end36 = end26+relatedWordCloud.size(); //Expl all other related words
        end46 = end36+relatedWordCloud.size(); //Q+A+Expl all other related words
    }

    @Override
    public String toSVMRankString(Sentence question, Sentence correctAns, Sentence expl) {
        return getFeature(this.start, synonyms, question.getSynonyms(), end11) +" "+
                getFeature(end11, synonyms, correctAns.getSynonyms(), end21) +" "+
                getFeature(end21, synonyms, expl.getSynonyms(), end31) +" "+
                getFeature(end31, synonyms, getCommon(getGroup(List.copyOf(question.getSynonyms()), List.copyOf(correctAns.getSynonyms())), List.copyOf(expl.getSynonyms())), end41) +" "+

                getFeature(end41, isA, question.getIsA(), end12) +" "+
                getFeature(end12, isA, correctAns.getIsA(), end22) +" "+
                getFeature(end22, isA, expl.getIsA(), end32) +" "+
                getFeature(end32, isA, getCommon(getGroup(List.copyOf(question.getIsA()), List.copyOf(correctAns.getIsA())), List.copyOf(expl.getIsA())), end42) +" "+

                getFeature(end42, similar, question.getSimilar(), end13) +" "+
                getFeature(end13, similar, correctAns.getSimilar(), end23) +" "+
                getFeature(end23, similar, expl.getSimilar(), end33) +" "+
                getFeature(end33, similar, getCommon(getGroup(List.copyOf(question.getSimilar()), List.copyOf(correctAns.getSimilar())), List.copyOf(expl.getSimilar())), end43) +" "+

                getFeature(end43, relatedTo, question.getRelatedTo(), end14) +" "+
                getFeature(end14, relatedTo, correctAns.getRelatedTo(), end24) +" "+
                getFeature(end24, relatedTo, expl.getRelatedTo(), end34) +" "+
                getFeature(end34, relatedTo, getCommon(getGroup(List.copyOf(question.getRelatedTo()), List.copyOf(correctAns.getRelatedTo())), List.copyOf(expl.getRelatedTo())), end44) +" "+

                getFeature(end44, relations, question.getRelations(), end15) +" "+
                getFeature(end15, relations, correctAns.getRelations(), end25) +" "+
                getFeature(end25, relations, expl.getRelations(), end35) +" "+
                getFeature(end35, relations, getCommon(getGroup(List.copyOf(question.getRelations()), List.copyOf(correctAns.getRelations())), List.copyOf(expl.getRelations())), end45) +" "+

                getFeature(end45, relatedWordCloud, question.getRelatedWordCloud(), end15) +" "+
                getFeature(end16, relatedWordCloud, correctAns.getRelatedWordCloud(), end26) +" "+
                getFeature(end26, relatedWordCloud, expl.getRelatedWordCloud(), end36) +" "+
                getFeature(end36, relatedWordCloud, getCommon(getGroup(List.copyOf(question.getRelatedWordCloud()), List.copyOf(correctAns.getRelatedWordCloud())), List.copyOf(expl.getRelatedWordCloud())), end46);
    }
}
