package com.hwrec;

public class JavaNativeCalculator {
public native byte[][] distance(byte [][] x, byte [] y);
public JavaNativeCalculator(){
    System.out.println("tylko raz");
}
}