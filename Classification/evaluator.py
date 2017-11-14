import tensorflow as tf
from dataset import read_dataset, Dataset
from consts import *
from util import print_graph, print_acc

def load_and_test_graph():
    dataset = read_dataset(FEATURE_FILENAME)
    #persisted_sess.graph.as_default()
    load_graph()
    print_graph(tf.get_default_graph())
    with tf.Session() as sess:
        #sess.run(tf.global_variables_initializer())
        output_tensor = sess.graph.get_tensor_by_name("import/" + OUTPUT_TENSOR_NAME + ":0")
        input_tensor = sess.graph.get_tensor_by_name("import/" + INPUT_TENSOR_NAME + ":0")

        print_acc(sess, output_tensor, input_tensor, dataset)


def load_graph() -> tf.GraphDef:
    with tf.gfile.GFile(TARGET_MODEL_NAME, 'rb') as f:
        graph_def = tf.GraphDef()
        graph_def.ParseFromString(f.read())
    graph = tf.Graph().as_default()
    tf.import_graph_def(graph_def)
    return graph


if __name__ == '__main__':
    load_and_test_graph()