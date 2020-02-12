g++ -shared -O3 \
    -I/usr/include \
    -I$JAVA_HOME/include \
    -I$JAVA_HOME/include/linux \
    com_hwrec_JavaNativeCalculator.cpp -o libJavaNativeCalculator.dylib
cp libJavaNativeCalculator.dylib ../../../../../
