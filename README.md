# Hadoop-Learning
Streaming version &amp; Jar version

solve three's company

1>. delete folder:

bin/hadoop dfs -rm -r /hadoop/friends-output

2>. make dir：

bin/hadoop dfs -mkdir /hadoop

3>. check to content of a folder:

bin/hadoop dfs -ls /hadoop

4.> upload data to local HDFS

bin/hadoop dfs -copyFromLocal ~/friends1000/* /hadoop

5.> run

5.1>. Streaming Version (with python)

5.1.1>. without hadoop

cat friendsimple/* |./mapper.py |sort -k1,1| ./reducer.py 

5.1.2>. with hadoop

bin/hadoop jar ./share/hadoop/tools/lib/hadoop-streaming-2.2.0.jar -mapper ~/mapper.py -reducer ~/reducer.py -input /hadoop/friends-simple/* -output /hadoop/friends-output

5.2>. Jar version (with Java)

hadoop jar CountingTriangles.jar hw3.CountingTriangles /hadoop/friends-simple /hadoop/output
(hw3.CountingTriangle: hw3 is the package and CountingTriangles is the main class of Jar)

6>. check result
bin/hadoop fs -cat /hadoop/friends-output/part-00000
