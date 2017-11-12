# SceneClassifier

## Prerequirites:

This application requres Andrdoid 7.0 to function. Used dataset was DCASE2017 downloadble from http://www.cs.tut.fi/sgn/arg/dcase2017/challenge/download.


## Setup:

Install python 3.X and tensorflow package to python. Python version used with development was 3.6.
```
python -m pip install tensorflow
```
Load a release for https://github.com/JorenSix/TarsosDSP  and copy it to both 
*SceneClassifier/app/libs* and *DataExtraction/libs*. At time of writing the jar is incuded within the repository but it should be removed and manually loaded.

Followingly load prebuilt binaries from https://ci.tensorflow.org/view/Nightly/job/nightly-android/ for tensorflow inference. Used version for this project #318 (12.11.2017 0:19:33) with revision d4ad9c73969c45d1a224ebfc43eb645b9860216b. Then transfer the prebuilt binaries to *SceneClassifier/app/libs* so that the library contains the following: 

```
libs
|--arm64-v8a
|  |--libtensorflow_inference.so
|--armeabi-v7a
|  |--libtensorflow_inference.so
|--x86
|  |--libtensorflow_inference.so
|-arm64-v8a
|  |--libtensorflow_inference.so
|--libandroid_tensorflow_inference_java.jar
|--TarsosDSP-Android-latest.jar

``` 
Load http://www.labbookpages.co.uk/audio/javaWavFiles.html for Wav-file processing, the file is currently included within the repository, but may be removed.

## Structure

Currently the structure of the program is as following, extract MFCC features from the dataset and save this to a file. Then the file is loaded to classifier which uses tensorflow to create a shallow neural network model. The model is frozen (there mght be a bug with the freezing...)  and should be moved under the *SceneClassifier/app/src/main/assets*.

## Todos

Alot... incuding but not limited to
* Have labes for classes
* Manage to check or fix freeezing bug
* Refactoring audio recording to be more realtime
* Creating actual user interface
* Removing jars and binaries form the repository