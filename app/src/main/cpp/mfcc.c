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
#include "mfcc_extractor.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdio.h>

int xtract_dct(const double *data, const int N, double *result)
{

    int n;
    int m;
    double *temp = (double*)calloc(N, sizeof(double));

    for (n = 0; n < N; ++n)
    {
        for(m = 1; m <= N; ++m) {
            temp[n] += data[m - 1] * cos(M_PI * (n / (double)N) * (m - 0.5));
        }
    }

    memcpy(result, temp, N * sizeof(double));
    free(temp);

    return XTRACT_SUCCESS;
}

int xtract_mfcc(const double *data, const int N, const mel_filter *f, double *result)
{
    int n, filter;


    for(filter = 0; filter < f->n_filters; filter++)
    {
        result[filter] = 0.0;
        for(n = 0; n < N; n++)
        {
            result[filter] += data[n] * f->filters[filter][n];
        }
        result[filter] = log(result[filter] < XTRACT_LOG_LIMIT ? XTRACT_LOG_LIMIT : result[filter]);
    }

    xtract_dct(result, f->n_filters, result);

    return XTRACT_SUCCESS;
}

int xtract_init_mfcc(int N, double nyquist, int style, double freq_min, double freq_max, int freq_bands, double **fft_tables)
{

    int n, i, k, *fft_peak, M, next_peak;
    double norm, mel_freq_max, mel_freq_min, norm_fact, height, inc, val,
          freq_bw_mel, *mel_peak, *height_norm, *lin_peak;

    mel_peak = height_norm = lin_peak = NULL;
    fft_peak = NULL;
    norm = 1;

    if (freq_bands <= 1)
    {
        return XTRACT_ARGUMENT_ERROR;
    }

    mel_freq_max = 1127 * log(1 + freq_max / 700);
    mel_freq_min = 1127 * log(1 + freq_min / 700);
    freq_bw_mel = (mel_freq_max - mel_freq_min) / freq_bands;

    mel_peak = (double *)malloc((freq_bands + 2) * sizeof(double));
    /* +2 for zeros at start and end */

    if (mel_peak == NULL)
    {
        perror("error");
        return XTRACT_MALLOC_FAILED;
    }

    lin_peak = (double *)malloc((freq_bands + 2) * sizeof(double));

    if (lin_peak == NULL)
    {
        perror("error");
        free(mel_peak);
        return XTRACT_MALLOC_FAILED;
    }

    fft_peak = (int *)malloc((freq_bands + 2) * sizeof(int));

    if (fft_peak == NULL)
    {
        perror("error");
        free(mel_peak);
        free(lin_peak);
        return XTRACT_MALLOC_FAILED;
    }

    height_norm = (double *)malloc(freq_bands * sizeof(double));

    if (height_norm == NULL)
    {
        perror("error");
        free(mel_peak);
        free(lin_peak);
        free(fft_peak);
        return XTRACT_MALLOC_FAILED;
    }

    M = N >> 1;

    mel_peak[0] = mel_freq_min;
    lin_peak[0] = freq_min; // === 700 * (exp(mel_peak[0] / 1127) - 1);
    fft_peak[0] = lin_peak[0] / nyquist * M;


    for (n = 1; n < (freq_bands + 2); ++n)
    {
        //roll out peak locations - mel, linear and linear on fft window scale
        mel_peak[n] = mel_peak[n - 1] + freq_bw_mel;
        lin_peak[n] = 700 * (exp(mel_peak[n] / 1127) -1);
        fft_peak[n] = lin_peak[n] / nyquist * M;
    }

    for (n = 0; n < freq_bands; n++)
    {
        //roll out normalised gain of each peak
        if (style == XTRACT_EQUAL_GAIN)
        {
            height = 1;
            norm_fact = norm;
        }
        else
        {
            height = 2 / (lin_peak[n + 2] - lin_peak[n]);
            norm_fact = norm / (2 / (lin_peak[2] - lin_peak[0]));
        }
        height_norm[n] = height * norm_fact;
    }

    i = 0;

    for(n = 0; n < freq_bands; n++)
    {

        // calculate the rise increment
        if(n==0)
            inc = height_norm[n] / fft_peak[n];
        else
            inc = height_norm[n] / (fft_peak[n] - fft_peak[n - 1]);
        val = 0;

        // zero the start of the array
        for(k = 0; k < i; k++)
            fft_tables[n][k] = 0.0;

        // fill in the rise
        for(; i <= fft_peak[n]; i++)
        {
            fft_tables[n][i] = val;
            val += inc;
        }

        // calculate the fall increment
        inc = height_norm[n] / (fft_peak[n + 1] - fft_peak[n]);

        val = 0;
        next_peak = fft_peak[n + 1];

        // reverse fill the 'fall'
        for(i = next_peak; i > fft_peak[n]; i--)
        {
            fft_tables[n][i] = val;
            val += inc;
        }

        // zero the rest of the array
        for(k = next_peak + 1; k < N; k++)
            fft_tables[n][k] = 0.0;
    }


    /* Initialise the fft_plan for the DCT */
    /*
     * Ooura doesn't support non power-of-two DCT
    xtract_init_fft(freq_bands, XTRACT_MFCC);
    */

    free(mel_peak);
    free(lin_peak);
    free(height_norm);
    free(fft_peak);

    return XTRACT_SUCCESS;

}