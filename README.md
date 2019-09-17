# Digit Recognizer

Digit recognizer written in Scala/Akka HTTP using Knn algorithm.

## How to run
```sh
sbt -J-Xmx2g run
```

## How to check if it runs correct
Open a browser to [http://localhost:4000/index.html](http://localhost:4000/index.html). You should see the screen with
a white drawing area on the left and the recognition area on the right. Click `New` button at the top left. Try drawing
digit 0 (zero) as perfectly as possible and as filling up the entire drawing space as possible. You should see the
recognized digit on the right. The recognition accuracy isn't important although it should be able to recognize
some of your drawing. If you want to know how to write, check out files in `src/main/digit/tests` as examples.

## How to run load test
Once you verified that the service works, you can run load test against it. JMeter loadtest file is provided 
(`loadtest.jmx`) with load test data (`testdata.txt`). Once you load up the test file, it should be as simple as
kicking it off to run it.
