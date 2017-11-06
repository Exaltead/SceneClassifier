import tensorflow as tf
import numpy as np
from datesetter import read_dataset
from util import *
NUMBER_OF_CLASSES = 15
INPUT_SAMPLE_LENGHT = 30
INPUT_TENSOR_NAME = 'dadaa'
OUTPUT_TENSOR_NAME = 'tuutuut'
LABEL_TENSOR_NAME = 'meepmeep'
FEATURE_FILENAME = "../Resources/mfcc.csv"
BATCH_SIZE = 100

def create_computation_graph():
    x = tf.placeholder(tf.float32,
                       shape=[None, INPUT_SAMPLE_LENGHT],
                       name=INPUT_TENSOR_NAME)
    y_ = tf.placeholder(tf.float32,
                       shape=[None, NUMBER_OF_CLASSES],
                       name=LABEL_TENSOR_NAME)
    W = tf.Variable(tf.zeros(
        [INPUT_SAMPLE_LENGHT, NUMBER_OF_CLASSES]))
    b = tf.Variable(tf.zeros([NUMBER_OF_CLASSES]))
    y = tf.matmul(x, W) + b

    output = tf.nn.softmax(y, name=OUTPUT_TENSOR_NAME)

    cross_entropy = tf.reduce_mean(
        tf.nn.softmax_cross_entropy_with_logits(labels=y_,
                                                logits=y))
    train_step = tf.train.GradientDescentOptimizer(0.5)\
        .minimize(cross_entropy)

    return x, y_, train_step, output

if __name__ == '__main__':
    x, y_, train_step, output = create_computation_graph()
    datset = suffle_dataset(read_dataset(FEATURE_FILENAME))
    datset.train_labels = to_one_hot(datset.train_labels)
    sess = tf.Session()
    sess.run(tf.global_variables_initializer())

    for e in range(100):
        for i in range(int(datset.train_data.shape[0] / BATCH_SIZE) -1):
            batch_start = i * BATCH_SIZE
            batch = datset.train_data[batch_start: batch_start+BATCH_SIZE]
            labels = datset.train_labels[batch_start: batch_start+BATCH_SIZE]
            train_step.run(session=sess, feed_dict={x: batch, y_: labels})
        print("Iteration", e, "complete")

    # Test some predicitons

    #for i in range(10):
    #    print("Guessed", sess.run(output, feed_dict={x:np.reshape(datset.test_data[i,:], (1, 30))}))
    #    print("Correct", datset.test_labels[i])


    print("Accuracy:",calculate_accuracy(sess, output, {x: datset.test_data}, datset.test_labels))