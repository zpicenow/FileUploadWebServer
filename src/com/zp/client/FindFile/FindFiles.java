/*
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client.FindFile;

import java.io.File;
import java.util.LinkedList;

public class FindFiles {
    private void getAllFiles(LinkedList<FileInfo> list,File f,String rootPath){
        if(f.isFile()){
            String absolute = (new File(f.getParent())).getPath();
            list.add(new FileInfo(f.getName(),f.length(),absolute.substring(rootPath.length(),absolute.length())+'/'));
        }
        else{
            String[] files = f.list();
            if (files != null) {
                if(files.length == 0){
                    String absolute = (new File(f.getParent())).getPath();
                    list.add(new FileInfo(f.getName(),-1,absolute.substring(rootPath.length(),absolute.length())+'/'));
                }
                else {
                    for (String i:files){
                        getAllFiles(list,new File(f.getPath()+'/'+i),rootPath);
                    }
                }
            }
        }
    }

    public LinkedList<FileInfo> getFiles(String filePath){
        File f = new File(filePath);
        LinkedList<FileInfo> list = new LinkedList<>();
        FindFiles findFiles = new FindFiles();
        findFiles.getAllFiles(list,f,filePath);
        return list;
    }
}
