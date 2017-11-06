import tensorflow as tf
from tensorflow.python.framework.graph_util import convert_variables_to_constants
from tensorflow.python.platform import gfile
from datesetter import read_dataset
from util import to_one_hot, calculate_accuracy, suffle_dataset
NUMBER_OF_CLASSES = 15
INPUT_SAMPLE_LENGHT = 30
INPUT_TENSOR_NAME = 'dadaa'
OUTPUT_TENSOR_NAME = 'tuutuut'
LABEL_TENSOR_NAME = 'meepmeep'
FEATURE_FILENAME = "../Resources/mfcc.csv"
BATCH_SIZE = 100
HIDDEN_LAYER1_SIZE = 100
HIDDEN_LAYER2_SIZE = 100


def create_computation_graph(x):
    weights = {
        'h1': tf.Variable(tf.random_normal([INPUT_SAMPLE_LENGHT, HIDDEN_LAYER1_SIZE])),
        'h2': tf.Variable(tf.random_normal([HIDDEN_LAYER1_SIZE, HIDDEN_LAYER2_SIZE])),
        'out': tf.Variable(tf.random_normal([HIDDEN_LAYER2_SIZE, NUMBER_OF_CLASSES]))
    }
    biases = {
        'b1': tf.Variable(tf.random_normal([HIDDEN_LAYER1_SIZE])),
        'b2': tf.Variable(tf.random_normal([HIDDEN_LAYER2_SIZE])),
        'out': tf.Variable(tf.random_normal([NUMBER_OF_CLASSES]))
    }
    layer_1 = tf.add(tf.matmul(x, weights['h1']), biases['b1'])
    layer_2 = tf.add(tf.matmul(layer_1, weights['h2']), biases['b2'])
    return tf.matmul(layer_2, weights['out']) + biases['out']

def save_model(sess: tf.Session):
    minimal_graph = convert_variables_to_constants(sess, sess.graph_def, [OUTPUT_TENSOR_NAME])

    tf.train.write_graph(minimal_graph, '.', 'minimal_graph.proto', as_text=False)
    tf.train.write_graph(minimal_graph, '.', 'minimal_graph.txt', as_text=True)


def add_predictor(logits):
    return tf.nn.softmax(logits, name=OUTPUT_TENSOR_NAME)


def make_traingin_step(logits, Y, learning_rate=0.001):
    loss_op = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(
        logits=logits, labels=Y))
    optimizer = tf.train.AdamOptimizer(learning_rate=learning_rate)
    train_op = optimizer.minimize(loss_op)
    return train_op, loss_op


def train_model(sess: tf.Session, x, y,train_op, dataset, loss_op=None):
    for e in range(100):
        avg_cost = 0.
        total_batch = int(dataset.train_data.shape[0] / BATCH_SIZE)
        for i in range(int(total_batch) -1):
            batch_start = i * BATCH_SIZE
            batch = dataset.train_data[batch_start: batch_start+BATCH_SIZE]
            labels = dataset.train_labels[batch_start: batch_start+BATCH_SIZE]
            _, c = sess.run([train_op, loss_op], feed_dict={x: batch, y: labels})
            avg_cost += c / total_batch
        print("Epoch:", '%04d' % (e+1), "cost={:.9f}".format(avg_cost))

def train_and_save():
    x = tf.placeholder(tf.float32,
                       shape=[None, INPUT_SAMPLE_LENGHT],
                       name=INPUT_TENSOR_NAME)
    y_ = tf.placeholder(tf.float32,
                        shape=[None, NUMBER_OF_CLASSES],
                        name=LABEL_TENSOR_NAME)
    logits = create_computation_graph(x)
    datset = suffle_dataset(read_dataset(FEATURE_FILENAME))
    datset.train_labels = to_one_hot(datset.train_labels)
    train_op, loss_op = make_traingin_step(logits, y_)

    init = tf.global_variables_initializer()
    with tf.Session() as sess:
        sess.run(init)

        train_model(sess, x, y_, train_op, datset, loss_op)

        output = add_predictor(logits)

        print("Accuracy", calculate_accuracy(sess, output, {x: datset.test_data}, datset.test_labels))
        save_model(sess)


def load_graph(sess):
    with open("minimal_graph.proto", 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())


def load_and_test_graph():
    dataset = read_dataset(FEATURE_FILENAME)
    #persisted_sess.graph.as_default()
    with tf.Session() as sess:
        load_graph(sess)
        output_tensor = sess.graph.get_tensor_by_name(OUTPUT_TENSOR_NAME+":0")
        input_tensor = sess.graph.get_tensor_by_name(INPUT_TENSOR_NAME+":0")
        print("Accuracy", calculate_accuracy(sess, output_tensor, {input_tensor: dataset.test_data}, dataset.test_labels))

def main():
    #train_and_save()
    load_and_test_graph()


if __name__ == '__main__':
    main()

    # Test some predicitons

    #for i in range(10):
    #    print("Guessed", sess.run(output, feed_dict={x:np.reshape(datset.test_data[i,:], (1, 30))}))
    #    print("Correct", datset.test_labels[i])


