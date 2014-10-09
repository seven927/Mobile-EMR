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

public class MedicFragment extends Fragment{
	
	private SSLSocket client_sslSocket;
	private EditText etype;
	private EditText eheight;
	private EditText eweight;
	private EditText eexam;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				init();
				sendSocketMessageToServer("2medicinfo"+","+getActivity().getIntent().getStringExtra("user"));
				getSocketMessageFromServer();
			}
			
		});
		thread.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.medic, container, false);
		etype=(EditText)view.findViewById(R.id.editBlood);
		eheight=(EditText)view.findViewById(R.id.editHeight);
		eweight=(EditText)view.findViewById(R.id.editWeight);
		eexam=(EditText)view.findViewById(R.id.editExamination);
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
			String[] result=msg.obj.toString().split(",");
			etype.setText(result[0]);
			eheight.setText(result[1]);
			eweight.setText(result[2]);
			eexam.setText(result[3]);
		}
	};
}
