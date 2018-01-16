/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import Module.*;
import java.io.IOException;
import static java.lang.System.out;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NattapatN
 */
public class Client {

    public static void main(String[] args) throws IOException {
//        try {
            sendMetaFile sMF = new sendMetaFile("127.0.0.1", 9000, "test.mp4");
            int port = sMF.send();
            
            Upload upload = new Upload("127.0.0.1",port);
            upload.send("media/test.mp4");

//            //get available nic
//            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//            for (NetworkInterface netint : Collections.list(nets)) {
//                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
//                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
//                    if (!inetAddress.toString().contains(":")) {
//                        out.printf("InetAddress: %s\n", inetAddress);
//                    }
//                }
//            }
//
//        } catch (SocketException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    }

}
