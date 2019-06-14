# -*- coding: utf-8 -*-
from __future__ import division, print_function

import sys
import numpy as np
seed = int(sys.argv[1])
np.random.seed(seed) # for reproducibility

from gensim.models import KeyedVectors
from keras.callbacks import ModelCheckpoint, EarlyStopping, ReduceLROnPlateau
from keras.layers import Dense, Merge, Dropout, Reshape, Flatten
from keras.layers.embeddings import Embedding
from keras.layers.recurrent import LSTM
from keras.layers.wrappers import Bidirectional
from keras.models import Sequential
from sklearn.model_selection import train_test_split
from keras.preprocessing.sequence import pad_sequences
import os
import nltk

import textgraphutils as tgu

DATA_DIR = "../data"
RES_DIR = "../data/resources"
MODEL_DIR = "../data/models"
OUTPUT_DIR = "../output"
WORD2VEC_BIN = "GoogleNews-vectors-negative300.bin.gz"
WORD2VEC_EMBED_SIZE = 300

QA_TRAIN_FILE = "Elem-Train.csv"
QA_DEV_FILE = "Elem-Dev.csv"

QA_EMBED_SIZE = 64
BATCH_SIZE = 32
NBR_EPOCHS = 20

## extract data
print("Loading and formatting data...")
qapairs = tgu.get_question_answer_pairs(
    os.path.join(DATA_DIR, QA_TRAIN_FILE))
ques_maxlen = max([len(qapair[0]) for qapair in qapairs])
ans_maxlen = max([len(qapair[1]) for qapair in qapairs])

# Even though we don't use the test set for classification, we still need
# to consider any additional vocabulary words from it for when we use the
# model for prediction (against the test set).
dqapairs = tgu.get_question_answer_pairs(
    os.path.join(DATA_DIR, QA_DEV_FILE))    
dques_maxlen = max([len(qapair[0]) for qapair in dqapairs])
dans_maxlen = max([len(qapair[1]) for qapair in dqapairs])

seq_maxlen = max([ques_maxlen, ans_maxlen, dques_maxlen, dans_maxlen])

# build word indexes
word2idx = tgu.build_vocab([], qapairs, dqapairs)
vocab_size = len(word2idx) + 1 # include mask character 0

Xq, Xa, Y = tgu.vectorize_qapairs(qapairs, word2idx, seq_maxlen)

# get embeddings from word2vec
print("Loading Word2Vec model and generating embedding matrix...")
word2vec = KeyedVectors.load_word2vec_format(
    os.path.join(RES_DIR, WORD2VEC_BIN), binary=True)
embedding_weights = np.zeros((vocab_size, WORD2VEC_EMBED_SIZE))
for word, index in word2idx.items():
    try:
        embedding_weights[index, :] = word2vec[word.lower()]
    except KeyError:
        pass  # keep as zero (not ideal, but what else can we do?)
        
print("Building model...")
qenc = Sequential()
qenc.add(Embedding(output_dim=WORD2VEC_EMBED_SIZE, input_dim=vocab_size,
                   input_length=seq_maxlen,
                   weights=[embedding_weights]))
qenc.add(Bidirectional(LSTM(QA_EMBED_SIZE, return_sequences=True), 
                       merge_mode="sum"))
qenc.add(Dropout(0.3))

aenc = Sequential()
aenc.add(Embedding(output_dim=WORD2VEC_EMBED_SIZE, input_dim=vocab_size,
                   input_length=seq_maxlen,
                   weights=[embedding_weights]))
aenc.add(Bidirectional(LSTM(QA_EMBED_SIZE, return_sequences=True),
                       merge_mode="sum"))
aenc.add(Dropout(0.3))

# attention model
attn = Sequential()
attn.add(Merge([qenc, aenc], mode="dot", dot_axes=[1, 1]))
attn.add(Flatten())
attn.add(Dense((seq_maxlen * QA_EMBED_SIZE)))
attn.add(Reshape((seq_maxlen, QA_EMBED_SIZE)))

model = Sequential()
model.add(Merge([qenc, attn], mode="sum"))
model.add(Flatten())
model.add(Dense(2, activation="softmax"))

model.compile(optimizer="adam", loss="categorical_crossentropy",
              metrics=["accuracy"])

basefilename = os.path.basename(sys.argv[0])
basefilename = basefilename[0:basefilename.rindex(".")]+str(seed)

print("Training...")
best_model_filename = os.path.join(MODEL_DIR, basefilename+"-best.hdf5")
checkpoint = ModelCheckpoint(filepath=best_model_filename,
                             verbose=1, save_best_only=True)
early_stop = EarlyStopping(monitor='val_loss', min_delta=0, patience=5, mode='auto')
reduce_lr = ReduceLROnPlateau(monitor='val_loss', factor=0.2, patience=5, mode='auto', min_lr=0.001)
model.fit([Xq, Xa], [Y], batch_size=BATCH_SIZE,
          nb_epoch=NBR_EPOCHS, validation_split=0.1,
          callbacks=[reduce_lr,early_stop,checkpoint],
	  sample_weight = tgu.generate_sample_weights(Y))

final_model_filename = os.path.join(MODEL_DIR, tgu.get_model_filename(sys.argv[0], str(seed)+"-final"))
json_model_filename = os.path.join(MODEL_DIR, tgu.get_model_filename(sys.argv[0], str(seed)+".json"))
tgu.save_model(model, json_model_filename, final_model_filename)

print("Making predictions...")
ftest = open(os.path.join(DATA_DIR, QA_DEV_FILE), "r", encoding="utf8")
total_answers = 0
total_questions = 0
fsub = open(os.path.join(OUTPUT_DIR, "ACTUAL-PREDICT-"+str(seed)), "w")
line_nbr = 0
for line in ftest:
    if line.startswith("#"):
        continue
    line = line.strip()
    if line_nbr % 10 == 0:
        print("Processed %d questions..." % (line_nbr))
    cols = line.split("\t")
    qid = cols[0]
    question = cols[1]
    answers = cols[3:]
    # create batch of question
    qword_ids = [word2idx[qword] for qword in nltk.word_tokenize(question)]
    Xq, Xa = [], []
    for answer in answers:
        Xq.append(qword_ids)
        Xa.append([word2idx[aword] for aword in nltk.word_tokenize(answer)])
    Xq = pad_sequences(Xq, maxlen=seq_maxlen)
    Xa = pad_sequences(Xa, maxlen=seq_maxlen)
    Y = model.predict([Xq, Xa])
    correct_ans = cols[2]

    result = np.where(Y[:,0] == np.amax(Y[:, 0]))
    predicted_ans = result[0] == 0 and "A" or \
                    (result[0] == 1 and "B" or \
                    (result[0] == 2 and "C" or "D"))
    if (correct_ans == predicted_ans): total_answers = total_answers+1
    total_questions = total_questions+1	  

    if (len(Y) >= 1): fsub.write(correct_ans+'\t'+str(Y[0,:])+'\t'+str(Xq[0,:])+'\n')
    if (len(Y) >= 2): fsub.write(correct_ans+'\t'+str(Y[1,:])+'\t'+str(Xq[1,:])+'\n')
    if (len(Y) >= 3): fsub.write(correct_ans+'\t'+str(Y[2,:])+'\t'+str(Xq[2,:])+'\n')
    if (len(Y) >= 4): fsub.write(correct_ans+'\t'+str(Y[3,:])+'\t'+str(Xq[3,:])+'\n')

    line_nbr += 1
print("Processed %d questions..." % (line_nbr))

print("Accuracy:")
print((total_answers/total_questions)*100.0)

fsub.close()
ftest.close()

