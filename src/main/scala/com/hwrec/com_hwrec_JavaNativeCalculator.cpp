/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_hwrec_JavaNativeCalculator */

#ifndef _Included_com_hwrec_JavaNativeCalculator
#define _Included_com_hwrec_JavaNativeCalculator
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_hwrec_JavaNativeCalculator
 * Method:    distance
 * Signature: (Lscala/collection/Seq;Lscala/collection/Seq;)D
 */

 int abs(int x){
     if(x < 0) return -x;
     return x;
 }

JNIEXPORT jdouble JNICALL Java_com_hwrec_JavaNativeCalculator_distance
  (JNIEnv * env, jobject ob, jbyteArray data, jbyteArray input){
  jdouble res;
  jbyte * data_arr = env->GetByteArrayElements(input, 0);
  jbyte * input_arr = env->GetByteArrayElements(data, 0);
  jsize data_len = env->GetArrayLength(data);
  jsize input_len = env->GetArrayLength(input);

  for(int i = 0;i< data_len;i++){
  res += abs(data_arr[i] - input_arr[i]);
  }
  env->ReleaseByteArrayElements(input, data_arr, 0);
  env->ReleaseByteArrayElements(data, input_arr, 0);

  return res;
}

#ifdef __cplusplus
}
#endif
#endif
