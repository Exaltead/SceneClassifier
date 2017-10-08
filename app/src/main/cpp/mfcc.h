/*
 * Copyright (C) 2012 Jamie Bullock
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

#ifndef SCENECLASSIFIER_MFCC_H
#define SCENECLASSIFIER_MFCC_H
#ifdef __cplusplus
extern "C" {
#endif
#define XTRACT_VERY_SMALL_NUMBER 2e-42
#define XTRACT_LOG_LIMIT XTRACT_VERY_SMALL_NUMBER

enum xtract_return_codes_ {
    XTRACT_SUCCESS,
    XTRACT_MALLOC_FAILED,
    XTRACT_BAD_ARGV,
    XTRACT_BAD_VECTOR_SIZE,
    XTRACT_BAD_STATE,
    XTRACT_DENORMAL_FOUND,
    XTRACT_NO_RESULT, /* This usually occurs when the correct calculation cannot take place because required data is missing or would result in a NaN or infinity/-infinity. Under these curcumstances 0.f is usually given by *result */
    XTRACT_FEATURE_NOT_IMPLEMENTED,
    XTRACT_ARGUMENT_ERROR
};

enum xtract_mfcc_types_ {
    XTRACT_EQUAL_GAIN,
    XTRACT_EQUAL_AREA
};

typedef struct xtract_mel_filter_ {
    int n_filters;
    double **filters;
} mel_filter;

int mfcc(const double *data, const int N, const mel_filter *filter, double *result);

int xtract_init_mfcc(int N, double nyquist, int style, double freq_min, double freq_max, int freq_bands, double **fft_tables);
#ifdef __cplusplus
}
#endif
#endif //SCENECLASSIFIER_MFCC_H
