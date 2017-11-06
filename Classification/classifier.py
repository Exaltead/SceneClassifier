from keras.models import Sequential
from keras.layers import Dense, Activation
import keras.backend as K
import keras
import datesetter
FEATURE_FILE_LOCATION = "../Resources/mfcc.csv"
MODEL_FILE_LOCATION = "../Resources/mfcc_model.hdf5"

def train_classifier(dataset, target_file):
    model = Sequential()
    model.add(Dense(32, input_dim=dataset.train_data.shape[1]))
    model.add(Activation('relu'))
    model.add(Dense(len(dataset.label_explanation), activation='softmax'))
    model.compile(optimizer='rmsprop',
                  loss='categorical_crossentropy',
                  metrics=['accuracy'])
    model.fit(dataset.train_data, keras.utils.to_categorical(dataset.train_labels, num_classes=15), epochs=100)
    score = model.evaluate(dataset.test_data, keras.utils.to_categorical(dataset.test_labels))
    print()
    for i, name in enumerate(model.metrics_names):
        print(name, score[i])
    model.save(target_file)

def load_model(model_file):
    return load_model(model_file)


if __name__ == '__main__':
    train_classifier(datesetter.read_dataset(FEATURE_FILE_LOCATION), MODEL_FILE_LOCATION)

