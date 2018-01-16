package testLive;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Server implements Runnable {
	Socket socket = null;
	static HashMap<Integer, byte[]> hmap = new HashMap<Integer, byte[]>();
	static int id = 0;
	static int count = 1;
	private final Object lock = new Object();
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket listener = new ServerSocket(9020);
		listener.setSoTimeout(5000);
		System.out.println("Server Start");
        try {
            while (true) {
				if(count > 9){
	            	System.out.println("Start merge");
	            	merge2(9);
	            	count = 1;
            	}
				try {
					//System.out.println(id);
					Socket socket = listener.accept();
					//System.out.println("Accept Connection");
					if(socket.isConnected()){
						new Thread(new Server(socket)).start();
					}
				} catch (SocketTimeoutException e) {
					// TODO: handle exception
				}
                
            }
        }
        finally {
            listener.close();
        }

	}
	Server(Socket socket) throws IOException{
        //System.out.println("Initial Thread");
		this.socket = socket;
		//System.out.println("Open Socket");
		System.out.println("Source : "+this.socket.getRemoteSocketAddress());
	}
	
	@Override
	public void run() {
       System.out.println(Thread.currentThread().getId()+" Thread Start");

		try {
        	ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
        	Object object;
			try {
				System.out.println(Thread.currentThread().getId()+" Start download");
				object = (Data) objectInput.readObject();
				Data tmp = (Data) object;
				//JOptionPane.showMessageDialog(null, "infoMessage", "InfoBox: " + "titleBar", JOptionPane.INFORMATION_MESSAGE);
				//tmp.print();
				//value(tmp.index,tmp.pice);
				File someFile = new File("d:\\file\\tmp"+tmp.index+".t");
                FileOutputStream fos = new FileOutputStream(someFile);
                fos.write(tmp.pice);
                fos.flush();
                fos.close();
                count++;
				System.out.println("index :"+tmp.index);
				System.out.println("length : " + tmp.pice.length);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        	 
        } catch (Exception e1) {
			e1.printStackTrace();
		} finally {
            try {
				socket.close();
                //System.out.println("Server Close connection");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public void value(int v,byte[] b) {
		synchronized (lock) {
			hmap.put(v,b);
			id = v;
		}
	}
	
	static byte[] padding(byte[] bytes)
	{
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }
	    return Arrays.copyOf(bytes, i + 1);
	}
	static void merge() throws InterruptedException, IOException{
     	Thread.sleep(3000);
     	File someFile = new File("c:\\file\\new.t");
        FileOutputStream fos = new FileOutputStream(someFile,true);
   		for(Entry<Integer, byte[]> entry : hmap.entrySet()) {
   			//byt.write(entry.getValue(), 0, entry.getValue().length);
   			System.out.println(entry.getKey());
   			fos.write(padding(entry.getValue()));
            fos.flush();
   		}
        fos.close();
	    id = 0;
	}
	static void merge2(int num){
		int n = 1;
		try {
			ByteArrayOutputStream byt = new ByteArrayOutputStream();
			while(n <= num){
				System.out.println("n :"+n);
				System.out.println("num:"+num);
				File f = new File("d:\\file\\tmp"+n+".t");
				int tmp = 0;
				int sizeOfFiles = (int) f.length();
		        byte[] buffer = new byte[sizeOfFiles];
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
				while ((tmp = bis.read(buffer)) > 0) {
	            	byt.write(buffer, 0, tmp);
	            	byt.flush();
				}
				bis.close();
				n++;
        	}
			byte[] bytes = byt.toByteArray();
			byt = new ByteArrayOutputStream();
			System.gc();
            File File = new File("d:\\file\\newMerge.t");
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

}
