package com.house.smart.remote;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

public class IpSettingsActivity extends Activity {
	private Context context;
	private SharedPreferences sharedPrefIp, sharedPrefPort;
	private EditText etIp, etPort;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_settings);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		etIp = (EditText) findViewById(R.id.ip_address);
		etPort = (EditText) findViewById(R.id.port_address);
		context = getApplicationContext();
		sharedPrefIp = context.getSharedPreferences(getString(R.string.preference_ip), Context.MODE_PRIVATE);
		sharedPrefPort = context.getSharedPreferences(getString(R.string.preference_port), Context.MODE_PRIVATE);
		
		String defaultIp = getResources().getString(R.string.defaultIP);
		String defaultPort = getResources().getString(R.string.defaultPort);
		
		String textIp = sharedPrefIp.getString(getString(R.string.preference_ip), defaultIp);
		String textPort = sharedPrefPort.getString(getString(R.string.preference_port), defaultPort);
		
		etIp.setText(textIp);
		etPort.setText(textPort);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ip_settings, menu);
		return super.onCreateOptionsMenu(menu);
		
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_undo:
	        	finish();
	            return true;
	        case R.id.action_accept:
	    		SharedPreferences.Editor editorIp = sharedPrefIp.edit();
	    		SharedPreferences.Editor editorPort = sharedPrefPort.edit();
	    		editorIp.putString(getString(R.string.preference_ip), etIp.getText().toString());
	    		editorPort.putString(getString(R.string.preference_port), etPort.getText().toString());
	    		editorIp.commit();
	    		editorPort.commit();
	    		finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
