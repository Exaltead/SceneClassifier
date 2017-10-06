# May work with
cmake_minimum_required(VERSION 3.6)

add_library(mfcc-lib SHARED src/main/cpp/mfcc.c)
include_directories(src/main/cpp)