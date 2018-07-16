/*
 * Server
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.server;

import com.zp.lib.RSA;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private Properties config;
    private SocketService threadsPool = new ProtocolThreadPool(new FileManagerProtocol());

    public Server() {
        this.config = new Properties();
        try {
            String configPath = RSA.class.getResource("../config.properties").toString();
            FileReader fr = new FileReader(configPath.substring(5,configPath.length()));
            this.config.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(config.getProperty("port")));
            //System.out.println("Server start in port " + config.getProperty("port"));
            while (true) {
                Socket s = ss.accept();
                s.setSoTimeout(5*1000);
                threadsPool.service(s);
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
