g++ -dynamiclib -O3 \
    -I/usr/include \
    -I$JAVA_HOME/include \
    -I$JAVA_HOME/include/darwin \
    com_hwrec_JavaNativeCalculator.cpp -o libJavaNativeCalculator.dylib
