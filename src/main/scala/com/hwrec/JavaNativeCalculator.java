package com.hwrec;

public class JavaNativeCalculator {
public native double distance(scala.collection.Seq<scala.Byte> x, scala.collection.Seq<scala.Byte> y);
public JavaNativeCalculator(){
    System.out.println("tylko raz");
}
}