package com.myemr.mobileemr;


import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import android.content.Context;
import android.util.Log;

public class SSLClient {
	private static final int SERVER_PORT = 50030;  
	private static final String SERVER_IP = "192.168.1.100";  
	private static final String CLIENT_KET_PASSWORD = "123456";  // password for file keyclient.keystore which stores the private key of the client
	private static final String CLIENT_TRUST_PASSWORD = "123456";  // Password for file server_trust.keystore which stores the public certificate of the server
	private static final String CLIENT_AGREEMENT = "TLS"; 
	private static final String CLIENT_KEY_MANAGER = "X509";   
	private static final String CLIENT_TRUST_MANAGER = "X509";  
	private static final String CLIENT_KEY_KEYSTORE = "BKS";    
	private static final String CLIENT_TRUST_KEYSTORE = "BKS";
	
	private SSLSocket client_sslSocket;    
	
	public SSLClient(Context context){
		init(context);
		
	}
	//Initial setup
    private void init(Context context) {  
    	try{
    		SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);  
  	      	KeyManagerFactory keyManager = KeyManagerFactory.getInstance(CLIENT_KEY_MANAGER);  
  	      	TrustManagerFactory trustManager = TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);  
  	      	KeyStore kks= KeyStore.getInstance(CLIENT_KEY_KEYSTORE);  
  	      	KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE); 
  	      	kks.load(context.getResources().openRawResource(R.raw.keyclient),CLIENT_KET_PASSWORD.toCharArray());  
  	      	tks.load(context.getResources().openRawResource(R.raw.server_trust),CLIENT_TRUST_PASSWORD.toCharArray());   
  	      	keyManager.init(kks,CLIENT_KET_PASSWORD.toCharArray());  
  	      	trustManager.init(tks);  
  	      	sslContext.init(keyManager.getKeyManagers(),trustManager.getTrustManagers(),null);  
  	      	client_sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(SERVER_IP,SERVER_PORT);
  	      }catch (Exception e) {  
  	    	  Log.e("MySSLSocket",e.getMessage());
      	  } 
    }
    public SSLSocket getSocket(){
    	return client_sslSocket;
    }
}
