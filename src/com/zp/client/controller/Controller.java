/*
 * Controller
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client.controller;

import com.zp.client.FindFile.FileInfo;
import com.zp.client.FindFile.FindFiles;
import com.zp.client.upload.UploadManager;

import java.io.File;
import java.lang.Exception;
import java.util.LinkedList;

public class Controller {
    private UploadManager uploadManager;
    private LinkedList<FileInfo> uploadFiles = new LinkedList<>();

    public Controller() {
        try {
            this.uploadManager = new UploadManager(5,this.uploadFiles);
            this.uploadManager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(String filePath){
        try{
            File f = new File(filePath);
            if(f.exists()){
                if(f.isFile()){
                    String[] strings = filePath.split("/");
                    String name = strings[strings.length -1];
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < strings.length - 1; i++){
                        stringBuilder.append(strings[i]);
                        stringBuilder.append("/");
                    }
                    uploadFiles.addLast(new FileInfo(stringBuilder.toString(),0,name));
                }
                if (f.isDirectory()){
                    String[] strings = filePath.split("/");
                    String name = strings[strings.length -1];
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < strings.length - 1; i++){
                        stringBuilder.append(strings[i]);
                        stringBuilder.append("/");
                    }
                    FindFiles findFiles = new FindFiles();
                    LinkedList<FileInfo> list = findFiles.getFiles(filePath);
                    for (int i = 0; i < list.size(); i++){
                        uploadFiles.addLast(new FileInfo(stringBuilder.toString(),1,name+list.get(i).getPath()+list.get(i).getName()));
                    }
                }
            }else {
                System.out.println("文件不存在！");
            }
        }
        catch (Exception err){
            err.printStackTrace();
        }
    }
}
