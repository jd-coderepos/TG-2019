# -*- coding: utf-8 -*-
#adapted from https://github.com/sujitpal/dl-models-for-qa/blob/master/src/add-story.py

from __future__ import division, print_function
import elasticsearch
import os
import re
import spacy

DATA_DIR = "../data"
QA_TRAIN_INPUT = "8thGr-NDMC-Train.csv"

ES_HOST = "localhost"
ES_PORT = 9200
ES_INDEXNAME = "flashcards-idx"
ES_DOCTYPE = "stories"

SQA_TRAIN_OUTPUT = "SQA-Train.csv"

class StoryFinder(object):
    
    def __init__(self, host, port, index, doc_type):
        self.esconn = elasticsearch.Elasticsearch(hosts = [{
            "host": host, "port": str(port)    
        }])
        self.nlp = spacy.load('en')
        self.posbag = {"NOUN", "PROPN", "VERB"}
        self.index = index
        self.doc_type = doc_type

    def find_stories_for_question(self, question, num_stories=10):
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
                                  body=query)
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

fqa = open(os.path.join(DATA_DIR, QA_TRAIN_INPUT), "r", encoding="utf8")
fsqa = open(os.path.join(DATA_DIR, SQA_TRAIN_OUTPUT), "w", encoding="utf8")

nbr_lines = 1
for line in fqa:
    if line.startswith('#'):
        continue
    if nbr_lines % 100 == 0:
        print("Processed %d lines of input..." % (nbr_lines))
    line = line.strip()
    qid, question, correct_ans, ans_a, ans_b, ans_c, ans_d = \
        line.split("\t")
    story = " ".join(storyfinder.find_stories_for_question(question))
    correct_ans_idx = ord(correct_ans) - ord('A')
    answers = [ans_a, ans_b, ans_c, ans_d]
    for idx, answer in enumerate(answers):
        fsqa.write("%s\t%s\t%s\t%d\n" % (story, question, answer, 
                                         1 if idx == correct_ans_idx else 0))
    nbr_lines += 1
    
print("Processed %d lines of input...complete" % (nbr_lines))
fsqa.close()
fqa.close()
