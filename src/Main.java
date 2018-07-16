/*
 * Main
 *
 * @author zhaopeng
 * @date 18-7-15
 */

import com.zp.client.Client;
import com.zp.server.Server;



public class Main {
    public static void main(String[] args){
        if(args[0].equals("client")){
            Client client = new Client();
            client.start();
        }else {
            Server app = new Server();
            app.start();
        }

    }
}