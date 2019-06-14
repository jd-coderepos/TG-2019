# -*- coding: utf-8 -*-
#adapted from https://github.com/sujitpal/dl-models-for-qa/blob/master/src/add-story.py

from __future__ import division, print_function
import elasticsearch
import os
import re
import spacy

DATA_DIR = "../data"
OUTPUT_DIR = "../output"
QA_QUERY_INPUT = "Elem-Dev.csv"
EXPL_INPUT = "expl-tablestore.csv"

ES_HOST = "localhost"
ES_PORT = 9200
ES_INDEXNAME = "expl-idx"
ES_DOCTYPE = "explanations"

OUTPUT = "qa-es-explanations-3000.txt"

class StoryFinder(object):
    
    def __init__(self, host, port, index, doc_type):
        self.esconn = elasticsearch.Elasticsearch(hosts = [{
            "host": host, "port": str(port)    
        }])
        self.nlp = spacy.load('en')
        self.posbag = {"NOUN", "PROPN", "VERB"}
        self.index = index
        self.doc_type = doc_type

    def find_stories_for_question(self, question, num_stories=3000):
        # extract tokens from question to search with (NOUN, VERB, PROPN)
        question = re.sub(r"[^A-Za-z0-9 ]", "", question)
        tokens = self.nlp(question)
        qwords = []
        for token in tokens:
            if token.pos_ in self.posbag:
                qwords.append(token.string)
        # compose an OR query with all words and get num_stories results
        query_header = """
{
    "query": {
        "bool": {
            "should": [
    """
        qbody = []
        for qword in qwords:
            qbody.append("""
            {
                "term": {
                    "story": "%s"
                }
            }""" % (qword.strip().lower()))
        query_footer = """
            ]
        }
    }
}
        """
        query = query_header + ",".join(qbody) + query_footer
        resp = self.esconn.search(index=self.index, doc_type=self.doc_type, 
                                  body=query, size=num_stories)
        hits = resp["hits"]["hits"]
        stories = []
        for hit in hits:
            stories.append(hit["_source"]["story"].encode("ascii", "ignore"))	        
		
        stories2 = []
        for story in stories:
            stories2.append(story.decode('utf-8','ignore'))
                			
        return stories2    
        


###### main ####

storyfinder = StoryFinder(ES_HOST, ES_PORT, ES_INDEXNAME, ES_DOCTYPE)

explanations_d = {}
with open(os.path.join(DATA_DIR, EXPL_INPUT), "r", encoding="utf8") as f:
    for line in f:
        (key, val) = line.split("\t")
        val = val.replace(" ", "").strip()
        if "SÃ£oTomÃ©andPrÃ­ncipe" in val:
            val = "SoTomandPrncipeislocatedintheequatorialregion"
        if "thesurfaceofthesunisextremelyhotintemperature" in val:
            val = "thesurfaceofthesunisextremelyhotintemperaturewithvaluesashighas20000000C"
        if "YucatÃ¡nisastate" in val:			
            val = "YucatnisastatelocatedinMexico"	
        if "MichoacÃ¡nisastate" in val:
            val = "MichoacnisastatelocatedinMexico"	
        if "NuevoLeÃ³nisastate" in val:			
            val = "NuevoLenisastatelocatedinMexico"
        if "MÃ©xicoisastate" in val:			
            val = "MxicoisastatelocatedinMexico"
        if "QuerÃ©taroisastate" in val:
            val = "QuertaroisastatelocatedinMexico"
        if "SanLuisPotosÃ­isastate" in val:
            val = "SanLuisPotosisastatelocatedinMexico"		
        if "CuraÃ§aoislocatedin" in val:
            val = "Curaaoislocatedinthenorthernhemisphere"
        if "SaintBarthÃ©lemy(France)islocated" in val:
            val = "SaintBarthlemy(France)islocatedinthenorthernhemisphere"
        if "RÃ©union(France)islocatedin" in val:
            val = "Runion(France)islocatedinthesouthernhemisphere"
        explanations_d[val] = key

fqa = open(os.path.join(DATA_DIR, QA_QUERY_INPUT), "r", encoding="utf8")
fsqa = open(os.path.join(OUTPUT_DIR, OUTPUT), "w", encoding="utf8")

nbr_lines = 1
for line in fqa:
    if line.startswith('#'):
        continue
    if nbr_lines % 100 == 0:
        print("Processed %d lines of input..." % (nbr_lines))
    line = line.strip()
    if (len(line.split("\t")) == 7):
        qid, question, correct_ans, ans_a, ans_b, ans_c, ans_d = line.split("\t")
    else:		
        qid, question, correct_ans, ans_a, ans_b, ans_c = line.split("\t")
	
    answer = correct_ans == "A" and ans_a or (correct_ans == "B" and ans_b or (correct_ans == "C" and ans_c or ans_d))
    query = " ".join([question, answer])
	
    explanations = storyfinder.find_stories_for_question(query)
	
    for idx, explanation in enumerate(explanations):
        explanation_str = explanation.replace(" ", "")
        fsqa.write("%s\t%s\n" % (qid, explanations_d[explanation_str]))
    nbr_lines += 1
    
print("Processed %d lines of input...complete" % (nbr_lines))
fsqa.close()
fqa.close()
