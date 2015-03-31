#!/usr/bin/env python

import sys

# input comes from STDIN (standard input)
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    # split the line into words
    words = line.split()
    # increase counters
    #print len(words)
    i0 = 0
    for i1 in range(i0+1,len(words)-1):
        for i2 in range(i1+1,len(words)):
            # write the results to STDOUT (standard output);
            # what we output here will be the input for the
            # Reduce step, i.e. the input for reducer.py
            #
            # tab-delimited; the trivial word count is 1

            #get possible triangle
            t0 = int(words[i0])
            t1 = int(words[i1])
            t2 = int(words[i2])

            #sort for key
            n1 = 0;n1 = 0;n2 = 0
            if t0<t1:
                if t0<t2:
                    n0 = t0
                    if t1<t2:
                        n1 = t1;n2 = t2
                    else:
                        n1 = t2;n2 = t1
                else:
                    n0 = t2;n1 = t0;n2 = t1
            else:
                if t1<t2:
                    n0 = t1
                    if t0<t2:
                        n1 = t0;n2 = t2
                    else:
                        n1 = t2;n2 = t0
                else:
                    n0 = t2;n1 = t1;n2 = t0

            #maper output, value as triangle
            if t1<t2:
                tri = words[i0]+','+words[i1]+','+words[i2]
                print '%s%s%s\t%s' % (n0, n1, n2, tri)
            else:
                tri = words[i0]+','+words[i2]+','+words[i1]
                print '%s%s%s\t%s' % (n0, n1, n2, tri)
