import numpy as np
import tensorflow as tf
import datesetter as ds

def to_one_hot(matrix):
    matrix = matrix - np.min(matrix)
    num_class = int(np.max(matrix)) + 1
    result = np.zeros((matrix.size, num_class))
    for c, i in enumerate(matrix):
        result[c, int(i)] = 1
    return result

def suffle(a, b):
    assert len(a) == len(b)
    p = np.random.permutation(len(a))
    return a[p], b[p]

def suffle_dataset(dataset: ds.Dataset):
    s_td, s_tl = suffle(dataset.train_data, dataset.train_labels)
    dataset.train_data = s_td
    dataset.train_labels = s_tl

    s_td, s_tl = suffle(dataset.test_data, dataset.test_labels)
    dataset.test_data = s_td
    dataset.test_labels = s_tl

    return dataset

def calculate_accuracy(sess: tf.Session, y, feed_dict, correct):
    res = sess.run(y, feed_dict=feed_dict)
    classes = np.argmax(res, 1)

    cor = 0
    for i, c in enumerate(correct):
        if c == classes[i]:
            cor += 1
    return cor / correct.size

if __name__ == '__main__':
    print(to_one_hot(np.asarray([1,2,3,3,2,1])))
    print(suffle(np.asarray([1,2,3,3,2,1]), np.asarray([1,2,3,3,2,1])+1))