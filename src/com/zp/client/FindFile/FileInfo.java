/*
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client.FindFile;

public class FileInfo {
    private String name = "";
    private long size = 0;
    private String path = "";

    public FileInfo(String name,long size,String path){
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public FileInfo(){
        this("",0,"");
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.name+", "+this.path + ", "+this.size;
    }
}
