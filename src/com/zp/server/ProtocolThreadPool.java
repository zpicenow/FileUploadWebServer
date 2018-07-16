/*
 * ProtocolThreadPool
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ProtocolThreadPool implements SocketService {
    private ArrayList<ServerThread> threads = new ArrayList<>();
    private final int initThreadNumber = 50;
    private final int Max = 150;
    private SocketService protocol = null;

    public ProtocolThreadPool(SocketService ss){
        this.protocol = ss;
        for (int i = 0; i < initThreadNumber; i++){
            ServerThread one = new ServerThread(ss);
            one.start();
            threads.add(one);
        }
        try{
            Thread.sleep(300);
        }catch (InterruptedException err){
            err.printStackTrace();
        }
    }

    @Override
    public void service(Socket s) {
        int flag = 0;
        ServerThread st = null;
        for (int i = 0; i < threads.size(); i++){
            st = threads.get(i);
            if(st.isIdle()){
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            st = new ServerThread(this.protocol);
            st.start();
            try{
                Thread.sleep(300);
            }catch (InterruptedException err){
                err.printStackTrace();
            }
            threads.add(st);
        }
        if(flag == 0){
            if(threads.size() < Max){
                st = new ServerThread(this.protocol);
                st.start();
                try{
                    Thread.sleep(300);
                }catch (InterruptedException err){
                    err.printStackTrace();
                }
                threads.add(st);
            }
            else {
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        st.setS(s);
    }
}
