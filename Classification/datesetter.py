"""Functions for reading preconfigured dataset"""
import numpy as np
import csv
import random


class Dataset:
    def __init__(self, train_data, train_labels, test_data, test_labels, label_explanation):
        self.train_data = train_data
        self.train_labels = train_labels
        self.test_data = test_data
        self.test_labels = test_labels
        self.label_explanation = label_explanation
        print(self.test_data.shape, train_data.shape)


def read_dataset(filename: str, train_to_test_split=0.8)-> Dataset:
    file = open(filename, mode='r')
    contents = read_from_csv_dataset(file, train_to_test_split)
    file.close()
    return contents


def read_from_csv_dataset(file, train_to_test_split) -> Dataset:
    """
    Reads dataset out of formatted csv file... to lazy to make efficient file system size the data is very compressed
    The currently used csv format is location, type, values
    """

    class_tree = {}
    for line in csv.reader(file):
        _rec_dict_add(class_tree, line[1], line[0], line[2:])
    _print_rec_tree(class_tree)
    # Take split * len(sub-locations) random locations as test samples
    return _sample_to_dataset(class_tree, 1 - train_to_test_split)



def _rec_dict_add(dict, first_key, second_key, samples):
    """dict is a dictionary of subdictionareis which contain lists of samples in list format"""
    if first_key in dict and second_key in dict[first_key]:
        dict[first_key][second_key].append(samples)
    elif first_key in dict:
        dict[first_key][second_key] = [samples]
    else:
        dict[first_key] = {second_key: [samples]}


def _print_rec_tree(rec_tree):
    total_recordings = 0
    for rec_type, locations in rec_tree.items():
        print(rec_type, "has", len(locations), "sub-locations")
        for loc, loc_samples in locations.items():
            print("\t", loc, "number of recordings:", len(loc_samples))
            total_recordings += len(loc_samples)
    print("Total:", total_recordings)


def _sample_to_dataset(rec_tree, test_sample_ratio) -> Dataset:
    test_locations = {}
    for rec_type, locations in rec_tree.items():
        test_locations[rec_type] =  random.sample(list(locations), int(test_sample_ratio * len(locations)))
    return _create_dataset_from_rec_and_test(rec_tree, test_locations)


def _create_dataset_from_rec_and_test(rec_tree, test_locations) -> Dataset:
    class_expl = _create_class_labels(list(rec_tree))
    train_data, train_labels, test_data, test_labels = _init_numpy_arrays(rec_tree, test_locations)
    test_count = 0
    train_count = 0
    for rec_type, locations in rec_tree.items():
        for loc, loc_samples in locations.items():
            for rec in loc_samples:
                if loc in test_locations[rec_type]:
                    test_data[test_count] = np.asarray(rec, dtype=np.float32)
                    test_labels[test_count] = class_expl[rec_type]
                    test_count += 1
                else:
                    train_data[train_count] = np.asarray(rec, dtype=np.float32)
                    train_labels[train_count] = class_expl[rec_type]
                    train_count += 1
    return Dataset(train_data, train_labels, test_data, test_labels, class_expl)


def _create_class_labels(types: list):
    result = {}
    for i, rt in enumerate(types):
        result[rt] = i
    return result


def _init_numpy_arrays(rec_tree, test_locations):
    train_size, test_size = _get_number_of_train_and_test_samples(rec_tree, test_locations)
    train_labels = np.zeros(train_size)
    test_labels = np.zeros(test_size)
    sample_length = _get_and_check_sample_length(rec_tree)
    train_data = np.zeros((train_size, sample_length),dtype=np.float32)
    test_data = np.zeros((test_size, sample_length), dtype=np.float32)
    return train_data, train_labels, test_data, test_labels


def _get_and_check_sample_length(rec_tree):
    current = -1
    for rec_type, locations in rec_tree.items():
        for loc, loc_samples in locations.items():
            for rec in loc_samples:
                if current == -1:
                    current = len(rec)
                elif current != len(rec):
                    print(rec_type, loc, "has length", len(rec), "while current length is", current)
                    raise ValueError
    return current


def _get_number_of_train_and_test_samples(rec_tree, test_locations) ->(int,int):
    train_samples = 0
    test_samples = 0
    for rec_type, locations in rec_tree.items():
        for loc, loc_samples in locations.items():
            if loc in test_locations[rec_type]:
                test_samples += len(loc_samples)
            else:
                train_samples += len(loc_samples)
    return train_samples, test_samples


if __name__ == '__main__':
    read_dataset("../Resources/mfcc.csv")