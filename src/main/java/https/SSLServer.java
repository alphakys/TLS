package https;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SSLServer {
	
	private static final int PORT = 8282;
	private static final String KEYSTORE_PATH = SSLServer.class.getResource("/certs/keystore").getPath();
	private static final String PASSWORD = "passphrase";
	
	public static void main(String args[]) {
		System.setProperty("java.net.ssl.keyStore", KEYSTORE_PATH);
		System.setProperty("java.net.ssl.keyStorePassword",PASSWORD);

		
	}
	
	public void startServer() {
		try {
			ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
			SSLServerSocket socket = (SSLServerSocket)ssf.createServerSocket(PORT);
			
			System.out.println("Listening on port" + socket.getLocalPort());
			
			while(true) {
				Socket client = socket.accept();
				ClientHandler handler = new ClientHandler(client);
				handler.start();
				
			}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	
	
	
	
	
	class ClientHandler extends Thread {
		Socket client;
		BufferedInputStream input;
		BufferedOutputStream out;
		
		public ClientHandler(Socket s) {
			client = s;
			try {
				input = new BufferedInputStream(client.getInputStream());
				out = new BufferedOutputStream(client.getOutputStream());
			}catch(IOException e) {
				System.out.println("Error creating streams: "+e.getMessage());
			}
			
		}
		
		public void run() {
			try {
				
				while(true) {
					byte[] byteArr = new byte[1];
				
					int len = input.read(byteArr);
					
					
					if(len==-1) {
						break;
					}
				
				
					String msg = new String(byteArr,0, len, "UTF-8");
					if("q".equals(msg)) {
						System.out.println("connecting is over");
						return;
					}
					out.write(msg.getBytes());
					out.flush();
				}

	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	
	
	
	
}
