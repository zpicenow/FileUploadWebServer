/*
 * RSADecrypt
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.lib;

public interface RSADecrypt {
    byte[] publicKeyDecrypt(byte[] data);
    byte[] privateKeyDecrypt(byte[] data);
}

//package cn.liuchaorun.lib;
//
//import java.io.InputStream;
//import javax.crypto.CipherInputStream;
//
//public interface RSADecrypt {
//    CipherInputStream publicKeyDecrypt(InputStream inputStream);
//    CipherInputStream privateKeyDecrypt(InputStream inputStream);
//}
