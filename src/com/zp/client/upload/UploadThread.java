/*
 * UploadThread
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client.upload;

import com.zp.client.controller.Controller;
import com.zp.lib.RSAEncrypt;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class UploadThread extends Thread {
    private Uploader uploader;
    private boolean isFile;
    private String filePath;
    private RSAEncrypt encrypt;
    private String absolutePath;
    private Properties config;

    public UploadThread(RSAEncrypt encrypt){
        this.config = new Properties();
        try{
            String configPath = Controller.class.getResource("../../config.properties").toString();
            FileReader fr = new FileReader(configPath.substring(5, configPath.length()));
            this.config.load(fr);
        }catch (Exception err){
            err.printStackTrace();
        }
        this.uploader = null;
        this.isFile = true;
        this.filePath = "";
        this.encrypt = encrypt;
        this.absolutePath = "";
    }

    public synchronized void setUploader(Uploader uploader){
        this.uploader = uploader;
        notify();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public boolean isIdle(){
        return this.uploader == null;
    }

    @Override
    public synchronized void run() {
        try{
            while (true){
                wait();
                Socket s = new Socket(this.config.getProperty("host"), Integer.parseInt(this.config.getProperty("port")));
                BufferedInputStream bufferedInputStream = new BufferedInputStream(s.getInputStream(),512);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream(),512);
                DataInputStream dis = new DataInputStream(bufferedInputStream);
                DataOutputStream dos = new DataOutputStream(bufferedOutputStream);
                dos.writeChars("UPLOAD\n");
                dos.flush();
                if(isFile){
                    File f = new File(absolutePath+filePath);
                    dos.writeLong(f.length());
                    dos.flush();
                    FileInputStream fis = new FileInputStream(f);
                    BufferedInputStream fileBufferedInputStream = new BufferedInputStream(fis);
                    uploader.uploadFile(fileBufferedInputStream,dis,dos,encrypt,f.length());
                    fis.close();
                }else {
                    uploader.uploadDir(dis,dos);
                }
                dis.close();
                dos.close();
                s.close();
                this.uploader = null;
            }
        }catch (Exception err){
            err.printStackTrace();
        }
    }
}
