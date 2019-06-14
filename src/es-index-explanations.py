# -*- coding: utf-8 -*-
#adapted from https://github.com/sujitpal/dl-models-for-qa/blob/master/src/es-load-flashcards.py

from __future__ import division, print_function
import elasticsearch
import nltk
import os

DATA_DIR = "../data"
EXPL_FILE = "expl-tablestore.csv"
EXPL_INDEX = "expl-idx"

es = elasticsearch.Elasticsearch(hosts=[{
    "host": "localhost",
    "port": "9200"
}])

if es.indices.exists(EXPL_INDEX):
    print("deleting index: %s" % (EXPL_INDEX))
    resp = es.indices.delete(index=EXPL_INDEX)
    print(resp)

body = {
    "settings": {
        "number_of_shards": 5,
        "number_of_replicas": 0
    }
}
print("creating index: %s" % (EXPL_INDEX))
resp = es.indices.create(index=EXPL_INDEX, body=body)
print(resp)

fexpl = open(os.path.join(DATA_DIR, EXPL_FILE), "r", encoding="utf8")
lno = 1
for line in fexpl:
    if lno % 1000 == 0:
        print("# explanation sentences read: %d" % (lno))
    line = line.strip()
    fcid, sent = line.split("\t")
    expl = " ".join(nltk.word_tokenize(sent))
    doc = { "story": expl }
    resp = es.index(index=EXPL_INDEX, doc_type="explanations", id=lno, body=doc)
#    print(resp["created"])
    lno += 1
print("# explanation sentences read and indexed: %d" % (lno))
fexpl.close()
es.indices.refresh(index=EXPL_INDEX)

query = """ { "query": { "match_all": {} } }"""
resp = es.search(index=EXPL_INDEX, doc_type="explanations", body=query)
print("# of records in index: %d" % (resp["hits"]["total"]))