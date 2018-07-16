/*
 * AES
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.lib;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AES implements AESDecrypt,AESEncrypt {
    private Key key;
    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;

    public AES(Key key,byte[] iv){
        try{
            this.key = key;
            cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            this.ivParameterSpec = new IvParameterSpec(iv);
        }catch (Exception err){
            err.printStackTrace();
        }
    }

    public static byte[] generateIvParamterApec(){
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    public static Key generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(secureRandom);
        return keyGenerator.generateKey();
    }

    @Override
    public CipherInputStream decrypt(InputStream is) {
        try {
            cipher.init(Cipher.DECRYPT_MODE,key,ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CipherInputStream(is,cipher);
    }

    @Override
    public CipherOutputStream encrypt(OutputStream os) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE,key,ivParameterSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CipherOutputStream(os,cipher);
    }
}
