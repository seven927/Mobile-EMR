package com.myemr.mobileemr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.SSLSocket;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class BasicFragment extends Fragment{
	
	private SSLSocket client_sslSocket;
	private EditText efname;
	private EditText elname;
	private EditText egender;
	private EditText ebirth;
	private EditText ephone;
	private EditText eaddress;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Thread thread=new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				init();
				sendSocketMessageToServer("2basicinfo"+","+getActivity().getIntent().getStringExtra("user"));
				getSocketMessageFromServer();
			}	
		});
		thread.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.basic, container, false);
		efname=(EditText)view.findViewById(R.id.editFirstName);
		elname=(EditText)view.findViewById(R.id.editLastName);
		egender=(EditText)view.findViewById(R.id.editGender);
		ebirth=(EditText)view.findViewById(R.id.editBirth);
		ephone=(EditText)view.findViewById(R.id.editPhone);
		eaddress=(EditText)view.findViewById(R.id.editAddress);
		return view;
	}
	
	private void init(){
		SSLClient c=new SSLClient(getActivity());
		client_sslSocket=c.getSocket();	
	}
	
	private void sendSocketMessageToServer(String s){
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client_sslSocket.getOutputStream())),true);
			out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getSocketMessageFromServer(){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client_sslSocket.getInputStream()));
			String line = in.readLine();
			handler.sendMessage(Message.obtain(handler, 0, line));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			String[] result= msg.obj.toString().split(",");
			efname.setText(result[0]);
//			efname.setEnabled(false);
			elname.setText(result[1]);
//			elname.setEnabled(false);
			egender.setText(result[2]);
//			egender.setEnabled(false);
			ebirth.setText(result[3]);
//			ebirth.setEnabled(false);
			ephone.setText(result[4]);
//			ephone.setEnabled(false);
			eaddress.setText(result[5]);
//			eaddress.setEnabled(false);
		}
	};
	
	
}
