
package com.zp;
/**
 * Test
 *
 * @author zhaopeng
 * @date 18-7-15
 * 用于测试调试，本片代码与功能无关
 */
import java.io.*;
import java.security.*;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;

public class Test {
    private static final int KEYSIZE = 1024;
    public LinkedList<Integer> a = new LinkedList<>();

    public static void main(String[] args) throws IOException, GeneralSecurityException, ClassCastException {
//        KeyPairGenerator pairGenerator = KeyPairGenerator.getInstance("RSA");
//        SecureRandom random = new SecureRandom();
//        pairGenerator.initialize(KEYSIZE,random);
//        KeyPair keyPair = pairGenerator.generateKeyPair();
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("public.key"))){
//            out.writeObject(keyPair.getPublic());
//        }
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("private.key"))){
//            out.writeObject(keyPair.getPrivate());
//        }
        Test t = new Test();
        try {
            t.a.addLast(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(t.a.toArray()));
        new testTread(t.a).start();
        System.out.println(1);
        while (true) {
            try {
                System.out.println("文件管理系统：");
                System.out.println("请输入指令");
                t.a.addLast(2);
                System.out.println(Arrays.toString(t.a.toArray()));
                Thread.sleep(1000);
            } catch (Exception err) {
                err.printStackTrace();
                break;
            }
        }
    }
}

class testTread extends Thread{
    private LinkedList<Integer> a;

    public testTread(LinkedList<Integer> a){
        this.a = a;
    }

    @Override
    public void run() {
        while(true){
            System.out.println(Arrays.toString(a.toArray()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class AgeChangeEvent extends EventObject {
    private int oage, nage;


    public int getOage() {
        return oage;
    }

    public int getNage() {
        return nage;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AgeChangeEvent(Object source, int oage, int nage) {

        super(source);
        this.nage = nage;
        this.oage = oage;


    }



}