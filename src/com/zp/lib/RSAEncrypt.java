package com.zp.lib;

public interface RSAEncrypt {
    byte[] publicKeyEncrypt(byte[] data);
    byte[] privateKeyEncrypt(byte[] data);
}
//package cn.liuchaorun.lib;
//
//import javax.crypto.CipherOutputStream;
//import java.io.OutputStream;
//
//public interface RSAEncrypt {
//    CipherOutputStream publicKeyEncrypt(OutputStream outputStream);
//    CipherOutputStream privateKeyEncrypt(OutputStream outputStream);
//}
