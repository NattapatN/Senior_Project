/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NattapatN
 */
public class sendMetaFile {

    String filename;
    String serverAdd;
    int oldPort;
    int buffersize = 1;

    public sendMetaFile(String server,int port,String file) {
        filename = file;
        serverAdd = server;
        oldPort=port;
    }
    
    public int send(){
        int newPort=0;
        try {
            File file = new File("media/"+filename);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverAdd, oldPort));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(buffersize);
            out.writeLong(file.length());
            byte[] data = file.getName().getBytes("UTF-8");
            out.writeInt(data.length);
            out.write(data);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            newPort = in.readInt();
            System.out.println(newPort);

        } catch (IOException ex) { 
            Logger.getLogger(sendMetaFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newPort;
    }

}
