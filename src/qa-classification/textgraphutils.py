# -*- coding: utf-8 -*-

from keras.models import model_from_json
from keras.preprocessing.sequence import pad_sequences
import nltk
import numpy as np
import collections
import os

from keras.preprocessing.sequence import pad_sequences


def get_question_answer_pairs(question_file, is_test=False):
    qapairs = []
    fqa = open(question_file, "r", encoding="utf8")
    for line in fqa:
        if line.startswith("#"):
            continue
        line = line.strip()
        cols = line.split("\t")
        question = cols[1]
        qwords = nltk.word_tokenize(question)
        if not is_test:
            correct_ans = cols[2]
            answers = cols[3:]
            # training file parsing
            correct_ans_idx = ord(correct_ans) - ord('A')
            for idx, answer in enumerate(answers):
                awords = nltk.word_tokenize(answer)
                qapairs.append((qwords, awords, idx == correct_ans_idx))
        else:
            # test file parsing (no correct answer)
            answers = cols[2:]
            for answer in answers:
                awords = nltk.word_tokenize(answer)
                qapairs.append((qwords, awords, None))
    fqa.close()
    return qapairs

def build_vocab(stories, qapairs, testqa):
    wordcounts = collections.Counter()
    for story in stories:
        for sword in story:
            wordcounts[sword] += 1
    for qapair in qapairs:
        for qword in qapair[0]:
            wordcounts[qword] += 1
        for aword in qapair[1]:
            wordcounts[aword] += 1
    for tqa in testqa:
        for qword in tqa[0]:
            wordcounts[qword] += 1
        for aword in tqa[1]:
            wordcounts[aword] += 1
    words = [wordcount[0] for wordcount in wordcounts.most_common()]
    word2idx = {w: i+1 for i, w in enumerate(words)}  # 0 = mask
    return word2idx	
	
def vectorize_qapairs(qapairs, word2idx, seq_maxlen):
    Xq, Xa, Y = [], [], []
    for qapair in qapairs:
        Xq.append([word2idx[qword] for qword in qapair[0]])
        Xa.append([word2idx[aword] for aword in qapair[1]])
        Y.append(np.array([1, 0]) if qapair[2] else np.array([0, 1]))
    return (pad_sequences(Xq, maxlen=seq_maxlen), 
            pad_sequences(Xa, maxlen=seq_maxlen),
            np.array(Y))	

dictionary = {1:0.5, 0:0.2}

def generate_sample_weights(training_data): 
    sample_weights = [dictionary[np.where(one_hot_row==0)[0][0]] for one_hot_row in training_data]
    return np.asarray(sample_weights)
	
def get_model_filename(caller, model_type):
    caller = os.path.basename(caller)
    caller = caller[0:caller.rindex(".")]
    if "json" in model_type:
        return "%s%s" % (caller, model_type)
    else:
        return "%s-%s.h5" % (caller, model_type)

def save_model(model, json_filename, weights_filename):
    model.save_weights(weights_filename)
    with open(json_filename, "w") as fjson:
        fjson.write(model.to_json())

def load_model(json_filename, weights_filename):
    with open(json_filename, "r", encoding="utf8") as fjson:
        model = model_from_json(fjson.read())
    model.load_weights(filepath=weights_filename)
    return model		
