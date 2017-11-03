from keras import backend as K
from keras.models import load_model
from tensorflow.python.training import saver

SOURCE_FILE = "../Resources/mfcc_model.hdf5"
TARGET_FILE = "../Resources/tensor_model"


if __name__ == '__main__':
    model = load_model(SOURCE_FILE)
    sess = K.get_session()
    tf_saver = saver.Saver()
    tf_saver.save(sess, TARGET_FILE)

