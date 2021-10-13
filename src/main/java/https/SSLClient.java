package https;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.nio.ByteBuffer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {
    private static final String REMOTE_HOST = "localhost";
    private static final int REMOTE_PORT = 8282;
    private static final String TRUST_PATH = SSLClient.class.getResource("/certs/truststore").getPath();
    private static final String PASSWORD = "passphrase";

    public static void main(String[] args){
        System.setProperty("javax.net.ssl.trustStore", TRUST_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", PASSWORD);

        SSLSocketFactory ssf = null;

        try{
            SSLSocket socket = null;

            if(socket ==null){
                return;
            }

            System.out.println("Negotiated Session: "+socket.getSession().getProtocol());
            System.out.println("Cipher Suite: "+socket.getSession().getCipherSuite());

            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

            out.write(new byte[]{2,3,4,5,6,7});
            out.flush();

            byte[] response = new byte[256];
            int len = input.read(response, 0, 64);

            if(len ==4){
                ByteBuffer result = ByteBuffer.allocate(4).put(response,0,4);
                System.out.println("\nServer result: "+result.getInt());
            }
            input.close();
            out.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
