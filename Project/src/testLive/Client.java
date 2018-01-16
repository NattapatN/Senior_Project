package testLive;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;

public class Client implements Runnable {
	Socket s;
	String nic;
	byte[] send;
	int index;
	public static void main(String[] args) throws IOException{
		File f = new File("media/test.mp4");
		splitFile(f);
	}
	
	Client(String nic,byte[] send,int index){
		this.nic = nic;
		this.send = send;
		this.index = index;
		try {
			NetworkInterface ni = NetworkInterface.getByName(nic);
			Enumeration<InetAddress> nifAddresses = ni.getInetAddresses();
			//System.out.println("Socket Open With "+nic);
			s = new Socket();
			s.bind(new InetSocketAddress(nifAddresses.nextElement(), 0));
			s.connect(new InetSocketAddress("127.0.0.1", 9020));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println(index);
		try {
			ObjectOutputStream outToServer = new ObjectOutputStream(s.getOutputStream());
	        outToServer.writeObject(new Data(send,index));
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void splitFile(File f) throws IOException {
        int sizeOfFiles = 1024 * (1024*50);// 500MB
        byte[] buffer = new byte[sizeOfFiles];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {//try-with-resources to ensure closing stream
        	//ByteArrayOutputStream byt = new ByteArrayOutputStream();
            int tmp = 0;
            int count = 1;
            while ((tmp = bis.read(buffer)) > 0) {
            	ByteArrayOutputStream byt = new ByteArrayOutputStream();
            	byt.write(buffer, 0, tmp);
            	byt.flush();
            	byte[] bytes = byt.toByteArray();
            	String nicName = getNetworkInterface();
            	System.out.println(nicName);
        		//new Thread(new Client(nicName,bytes,count++)).start();
            	System.out.println(count++);
            	
            	/*
                File someFile = new File("c:\\file\\new"+count+++".t");
                FileOutputStream fos = new FileOutputStream(someFile);
                fos.write(bytes);
                fos.flush();
                fos.close();
        		System.out.println(tmp);
            	*/
        	}
            //merge(count-1);
            /**
            byte[] bytes = byt.toByteArray();
            File someFile = new File("c:\\file\\new.t");
            FileOutputStream fos = new FileOutputStream(someFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
            **/
        }
    }
	public static void merge(int num){
		int n = 0;
		try {
			ByteArrayOutputStream byt = new ByteArrayOutputStream();
			while(n++ < num){
				System.out.println("n :"+n);
				System.out.println("num:"+num);
				File f = new File("c:\\file\\new"+n+".t");
				int tmp = 0;
				int sizeOfFiles = (int) f.length();
		        byte[] buffer = new byte[sizeOfFiles];
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
				while ((tmp = bis.read(buffer)) > 0) {
	            	byt.write(buffer, 0, tmp);
	            	byt.flush();
				}
				bis.close();
        	}
			byte[] bytes = byt.toByteArray();
            File File = new File("c:\\file\\newMerge.t");
            FileOutputStream fos = new FileOutputStream(File);
            fos.write(bytes);
            fos.flush();
            fos.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getNetworkInterface(){
		Enumeration<NetworkInterface> nets = null;
		String[] interfaceName = new String[99];
		int i = 0;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
        for (NetworkInterface netIf : Collections.list(nets)) {
        	Enumeration<InetAddress> addresses = netIf.getInetAddresses();
            try {
				if(netIf.isUp()&&!netIf.isLoopback() && !netIf.isVirtual() && !netIf.isPointToPoint()){
					//System.out.println("Display name: " + netIf.getDisplayName());
				    //System.out.println("Name: " + netIf.getName());
				    interfaceName[i++] = netIf.getName();
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        Random gen = new Random();
        return interfaceName[gen.nextInt(i)];
	}
}
