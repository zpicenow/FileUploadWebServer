/*
 * ServerThread
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.server;

import java.net.Socket;

public class ServerThread extends Thread {
    private Socket s = null;
    private SocketService fmp = null;

    public ServerThread(SocketService fmp){
        this.fmp = fmp;
    }

    public synchronized void setS(Socket s) {
        this.s = s;
        notify();
    }

    public boolean isIdle(){
        return s == null;
    }

    @Override
    public synchronized void run() {
        while (true){
            try {
                wait();
                fmp.service(s);
                this.s = null;
            }catch (Exception err){
                err.printStackTrace();
            }
        }
    }
}
