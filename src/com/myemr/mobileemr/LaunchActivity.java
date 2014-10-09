package com.myemr.mobileemr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity extends Activity{
	
	private static final int SPLASH_DISPLAY_LENGHT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent mainIntent = new Intent(getApplicationContext(), EMRActivity.class);  
                startActivity(mainIntent);  
                finish();  
            }  
        }, SPLASH_DISPLAY_LENGHT);  
		
	}
	
}
