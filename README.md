# Digit Recognizer

Digit recognizer written in Scala/Akka HTTP using Knn algorithm. 

The purpose of the project is to test the scalability and the framework being used to implement the service. Knn 
algorithm is used because it's simple and inefficient. Here's how it works. The system receives an input from the 
user which is a set of 1024 bytes of binary data (i.e. 101010101). The system has a preset of reference digits. For 
each digit, the service figures the distance between input and the reference. Once all distances are calculated, they 
are sorted in assending order. Only the first K entries are taken and counted. The digit whose count is the biggest 
is the recognized digit. 

This algorithm can be implemented in many ways. To be really fast, one can implement it with lots of parallelism. But
then such a system will need to survive and be able to continue to be responsive when there's whole lot of load on it.

## How to run
```sh
sbt -J-Xmx2g run
```

## How to check if it runs correctly
Open a browser to [http://localhost:4000/index.html](http://localhost:4000/index.html). You should see the screen with
a white drawing area on the left and the recognition area on the right. Click `New` button at the top left. Try drawing
digit 0 (zero) as perfectly as possible and as filling up the entire drawing space as possible. You should see the
recognized digit on the right. The recognition accuracy isn't important although it should be able to recognize
some of your drawing. If you want to know how to write, check out files in `src/main/digit/tests` as examples.

## How to run load test
Once you verified that the service works, you can run load test against it. JMeter loadtest file is provided 
(`loadtest.jmx`) with load test data (`testdata.txt`). Once you load up the test file, it should be as simple as
kicking it off to run it.
