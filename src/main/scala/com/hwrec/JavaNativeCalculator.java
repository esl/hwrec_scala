package com.hwrec;

public class JavaNativeCalculator {
public native double distance(byte [] x, byte [] y);
public JavaNativeCalculator(){
    System.out.println("tylko raz");
}
}