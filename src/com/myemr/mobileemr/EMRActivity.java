package com.myemr.mobileemr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLSocket;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EMRActivity extends ActionBarActivity {
	
	private static final String SUCCESS= "successful login";
	private static final String NOUSER = "Username does not exists";
	private static final String PASSWORDERROR="password is not correct";

	private SSLSocket client_sslSocket;    
	
	private Button btn;
	private EditText etxtUsername;
	private EditText etxtPassword;
	private String username;
	private String password;
	private RelativeLayout relative;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emr);
		btn=(Button)findViewById(R.id.btnLogin);
		relative=(RelativeLayout)findViewById(R.id.relative);
		etxtUsername = (EditText)findViewById(R.id.editAccount);
        etxtPassword = (EditText)findViewById(R.id.editPwd);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = etxtUsername.getText().toString();
				password = etxtPassword.getText().toString();
				if(username.equals(""))
					Toast.makeText(getApplicationContext(), "Please input username or email", Toast.LENGTH_SHORT).show();
				else if(password.equals(""))
					Toast.makeText(getApplicationContext(), "Please input password", Toast.LENGTH_SHORT).show();
				else{
					Thread thread= new Thread(new ChildThread());
					thread.start();
				}

			}
		});
		
		relative.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.requestFocus();
				return true;
			}
		});
	}
	
	class ChildThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			init();
			sendSocketMessageToServer(username,hashMD5(password));
			getSocketMessageFromServer();
		//	closeServerSocket();
		}
		
	}
	
	private void init(){
		SSLClient c=new SSLClient(getApplicationContext());
		client_sslSocket=c.getSocket();
	}
	  
    // Generate hash value of password using MD5
	public String hashMD5(String plainText){ 
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");   
			md.update(plainText.getBytes());   
			byte b[] = md.digest();   
			int i;   
			StringBuffer buf = new StringBuffer("");   
			for (int offset = 0; offset < b.length; offset++) {    
				i = b[offset];    
				if (i < 0)     
					i += 256;    
				if (i < 16)     
					buf.append("0");    
				buf.append(Integer.toHexString(i));   
			}   
			return buf.toString();   
            //System.out.println("result: " + buf.toString());   //32bit hash value  
			//System.out.println("result: " + buf.toString().substring(8, 24));// 16bit hash value
		}catch (NoSuchAlgorithmException e) {   
				// TODO Auto-generated catch block   
			e.printStackTrace();  
			return "";
		}
	}
	
	public void getSocketMessageFromServer(){
		try{
    	 
    	    BufferedReader in = new BufferedReader(new InputStreamReader(client_sslSocket.getInputStream()));
    	    String line;
    	    line = in.readLine();
    	    if(line.equals(NOUSER))
    	    	myhandler.sendMessage(Message.obtain(myhandler, 0, NOUSER));
    	    else if(line.equals(PASSWORDERROR))
    	    	myhandler.sendMessage(Message.obtain(myhandler, 0, PASSWORDERROR));
    	    else{
    	    	myhandler.sendMessage(Message.obtain(myhandler, 1, SUCCESS));
    	    }
         
    	}
    	catch (IOException e) {
    				// TODO Auto-generated catch block
    			e.printStackTrace();
    	}

	}
	
	
    public void sendSocketMessageToServer(String uName, String pWord){
    	try{
			PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(client_sslSocket.getOutputStream())),true);
		    out.println("1"+uName+","+pWord);		
			}catch(Exception e) {
				Log.e("TCP", "S: Error", e);
		}
	}
    
    Handler myhandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch(msg.what){
    		case 0:
    			Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
    			break;
    		case 1:
    			Intent mainIntent = new Intent(getApplicationContext(), DisplayActivity.class);
    			mainIntent.putExtra("user", username);
                startActivity(mainIntent);  
                finish();  
    		}
    	}
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emr, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
