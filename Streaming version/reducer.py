#!/usr/bin/env python

from operator import itemgetter
import sys

current_word = None
current_count = 1
word = None

triangles = []
# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    
    # parse the input we got from mapper.py
    word, tri = line.split('\t', 1)

    # this IF-switch only works because Hadoop sorts map output
    # by key (here: word) before it is passed to the reducer
    if current_word == word:
        current_count += 1
        triangles.append(tri)
    else:
        if current_word:
            if current_count==3:
                # write result to STDOUT
                for j in range(0,3):
                    temp = triangles[j].replace(',', ' ')
                    print '%s' % temp
        current_count = 1
        current_word = word
        del triangles[:]
        triangles.append(tri)

