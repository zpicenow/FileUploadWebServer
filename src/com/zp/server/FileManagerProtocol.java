/*
 * FileManagerProtocol
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.server;

import java.io.*;
import java.lang.Exception;
import java.net.Socket;
import java.util.logging.Logger;

public class FileManagerProtocol implements SocketService {
    private Controller controller = new Controller();

    @Override
    public void service(Socket s) {
        try{
            BufferedInputStream bufferedInputStream = new BufferedInputStream(s.getInputStream(),512);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(s.getOutputStream(),512);
            DataInputStream dis = new DataInputStream(bufferedInputStream);
            DataOutputStream dos = new DataOutputStream(bufferedOutputStream);
            while (true){
                StringBuilder stringBuilder = new StringBuilder();
                char c = 0;
                while ((c = dis.readChar())!='\n'){
                    stringBuilder.append(c);
                }
                if(stringBuilder.toString().equals("UPLOAD")){
                    //Logger.getGlobal().info("正在准备上传");
                    controller.upload(dis,dos);
                }else if(stringBuilder.toString().equals("CLOSE")){
                    s.close();
                    Logger.getGlobal().info("SOCKET CLOSE");
                    break;
                }
            }
        }catch (EOFException err){
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.getGlobal().info("SOCKET CLOSE");
        }
        catch (Exception err){
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            err.printStackTrace();
        }
    }
}
