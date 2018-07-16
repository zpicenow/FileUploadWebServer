/*
 * UploadManager
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client.upload;

import com.zp.client.FindFile.FileInfo;
import com.zp.lib.RSA;
import com.zp.lib.RSAEncrypt;

import java.io.File;
import java.util.LinkedList;


public class UploadManager extends Thread implements UploadService {
    private LinkedList treads = new LinkedList();
    private final int MAX_THREAD;
    private RSAEncrypt encrypt;
    private LinkedList<FileInfo> uploadFiles;

    public UploadManager(int number, LinkedList<FileInfo> uploadFiles){

        this.MAX_THREAD = number;
        this.uploadFiles = uploadFiles;
        this.encrypt = new RSA();
        for (int i = 0; i < MAX_THREAD; i++){
            UploadThread one = new UploadThread(encrypt);
            one.start();
            treads.add(one);
        }
        try{
            Thread.sleep(300);
        }catch (InterruptedException err){
            err.printStackTrace();
        }
    }

    @Override
    public synchronized void service(String absolutePath, String filePath) throws Exception {
        int flag = -1;
        while (flag == -1){
            for (int i = 0; i < MAX_THREAD; i++){
                UploadThread one = ((UploadThread)treads.get(i));
                if(one.isIdle()){
                    flag = i;
                    Uploader uploader = new Uploader(filePath);
                    one.setAbsolutePath(absolutePath);
                    one.setFilePath(filePath);
                    one.setFile(new File(absolutePath+filePath).isFile());
                    one.setUploader(uploader);
                    break;
                }
            }
            if(flag == -1){
                Thread.sleep(100);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true){
                if(uploadFiles.size() != 0) {
                    FileInfo fileInfo = uploadFiles.getFirst();
                    service(fileInfo.getName(), fileInfo.getPath());
                    uploadFiles.removeFirst();
                }else {
                    Thread.sleep(1000);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
