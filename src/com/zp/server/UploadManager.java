/*
 * UploadManager
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.server;

import com.zp.lib.AES;
import com.zp.lib.ObjectAndBytes;
import com.zp.lib.RSADecrypt;

import javax.crypto.CipherInputStream;
import java.io.*;
import java.security.Key;
import java.util.logging.Logger;

/**
 * 接受上传文件数据
 */
public class UploadManager {
    private long fileLength;
    private String name;
    private String path;
    private long currentLength = 0;

    public UploadManager(String pathName, long fileLength){
        String[] s = pathName.split("/");
        this.name = s[s.length -1];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length - 1; i++){
            stringBuilder.append(s[i]);
            stringBuilder.append("/");
        }
        this.path = stringBuilder.toString();
        this.fileLength = fileLength;
    }

    /**
     * 文件夹上传函数
     * @param dis
     * @param dos
     */
    public void uploadDir(DataInputStream dis, DataOutputStream dos){
        try{
            if(fileLength == -1){
                File f = new File(path+name+'\n');
                if(f.exists()){
                    dos.writeChars("OK\n");
                }
                else {
                    if(f.mkdirs()){
                        dos.writeChars("OK\n");
                    }else {
                        dos.writeChars("FAILED\n");
                    }
                }
                Logger.getGlobal().info(path+name+"上传完成");
            }
        }catch (Exception err){
            err.printStackTrace();
        }
    }

    /**
     * 查看是否该文件之前有过传输记录
     * @return boolean
     */
    public boolean readSchedule(){
        try{
            File f = new File(path+name+".lcrbak");
            if(f.exists()){
                RandomAccessFile raf = new RandomAccessFile(f,"r");
                String n = raf.readLine();
                String p = raf.readLine();
                long c = Long.parseLong(raf.readLine());
                long l = Long.parseLong(raf.readLine());
                if(n.equals(this.name)&&p.equals(this.path)&&l==this.fileLength){
                    this.currentLength = c;
                }
                //Logger.getGlobal().info("读取记录完成");
                return true;
            }else {
                return false;
            }
        }catch (Exception err){
            err.printStackTrace();
        }
        return false;
    }

    /**
     * 文件上传处理函数
     * @param dis
     * @param decrypt
     * @param dos
     */
    public void uploadFile(DataInputStream dis, DataOutputStream dos, RSADecrypt decrypt,boolean flag){
        File f = new File(path+name);
        Logger.getGlobal().info(path);
        if(!f.exists()){
            if(new File(path).mkdirs()){
                try {
                    if(f.createNewFile()){
                        //Logger.getGlobal().info(path+name+"文件创建成功！");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(f,flag);
            BufferedOutputStream fileBufferedOutputStream = new BufferedOutputStream(fos);

            dos.writeLong(currentLength);
            dos.flush();
            //Logger.getGlobal().info(""+currentLength);

            byte[] b = new byte[128];
            dis.readFully(b);
            byte[] iv = decrypt.privateKeyDecrypt(b);

            int objectArrayLength = dis.readInt();
            byte[] o = new byte[objectArrayLength];
            int c = 0;
            while (c < objectArrayLength){
                dis.readFully(b);
                byte[] temp = decrypt.privateKeyDecrypt(b);
                System.arraycopy(temp,0,o,c,temp.length);
                c+=temp.length;
            }
            Key key = (Key) ObjectAndBytes.toObject(o);

            int length = 16;
            AES aes = new AES(key,iv);
            CipherInputStream cipherInputStream = aes.decrypt(dis);
            Logger.getGlobal().info(path+name+"开始上传");
            long start = System.currentTimeMillis()/1000;
            while (currentLength < fileLength){
                byte[] data = new byte[length];
                length = cipherInputStream.read(data,0,(int)(fileLength-currentLength>=16?16:fileLength - currentLength));
                fileBufferedOutputStream.write(data,0,(int)(fileLength-currentLength>=16?16:fileLength - currentLength));
                fileBufferedOutputStream.flush();
                currentLength += data.length;
            }
            //Logger.getGlobal().info("currentLength = " + currentLength + ", "+ fileLength);
            long end = System.currentTimeMillis()/1000;
            Logger.getGlobal().info(path+name+"上传完成,平均速度为"+(fileLength/(1000*(end==start?1:end-start)))+"KB/S");
            File backupFile = new File(path+name+".lcrbak");
            if(backupFile.exists()){
                //Logger.getGlobal().info("进度文件删除 "+backupFile.delete());
            }
            fileBufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getGlobal().info("文件上传中断，记录此次信息！");
            try{
                FileWriter fw = new FileWriter(path+name+".lcrbak");
                fw.write(this.name+'\n');
                fw.write(this.path+'\n');
                fw.write(Long.toString(currentLength)+'\n');
                fw.write(Long.toString(this.fileLength)+'\n');
                fw.close();
            }catch (Exception err){
                err.printStackTrace();
            }
        }
    }
}
