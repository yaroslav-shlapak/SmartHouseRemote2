package com.house.smart.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.calc.ever.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Context context;
	private SharedPreferences sharedPrefIp, sharedPrefPort;
	private String textIp, textPort, defaultIp, defaultPort;
	private Toast currentToast;
	private Dimensions buttonsSize = new Dimensions();

	private SharedPreferences[] sharedPrefNameButton = new SharedPreferences[Constants.BUTTONS_NUMBER];
	private SharedPreferences[] sharedPrefStringButton = new SharedPreferences[Constants.BUTTONS_NUMBER];
	private Button[] buttons = new Button[Constants.BUTTONS_NUMBER];

	private SmartHouseButtonsAdapter buttonsAdapter;
	private GridView keypadGrid;

//	private final static int[] buttonsRid = { R.id.button1, R.id.button2,
//			R.id.button3, R.id.button4, R.id.button5, R.id.button6,
//			R.id.button7, R.id.button8, R.id.button9, R.id.button10,
//			R.id.button11, R.id.button12, R.id.button13, R.id.button14,
//			R.id.button15};

	private final static int[] buttonStrNames = { R.string.buttonName1,
			R.string.buttonName2, R.string.buttonName3, R.string.buttonName4,
			R.string.buttonName5, R.string.buttonName6, R.string.buttonName7,
			R.string.buttonName8, R.string.buttonName9, R.string.buttonName10,
			R.string.buttonName11, R.string.buttonName12, R.string.buttonName13,
			R.string.buttonName14, R.string.buttonName15};
	private final static int[] buttonStrStrings = { R.string.textButton1,
			R.string.textButton2, R.string.textButton3, R.string.textButton4,
			R.string.textButton5, R.string.textButton6, R.string.textButton7,
			R.string.textButton8, R.string.textButton9, R.string.textButton10,
			R.string.textButton11, R.string.textButton12, R.string.textButton13,
			R.string.textButton14, R.string.textButton15};

	private OnClickListener buttonOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sendData(v);
		}
	};

	private OnLongClickListener buttonOnLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getApplicationContext(),
					ButtonsSettingsActivity.class);

			for (int i = 0; i < Constants.BUTTONS_NUMBER; i++) {
				if (buttonsRid[i] == v.getId()) {
					intent.putExtra(Constants.BUTTON_NAME, buttonStrNames[i]);
					intent.putExtra(Constants.BUTTON_STRING, buttonStrStrings[i]);
					startActivity(intent);
					break;
				}
			}
			return true;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createButtons();
		initButtonsSize();

		initIPandPort();
		createSharedPrefName();
		createSharedPrefString();

		initButtonsNames();

        runTcpClient();
        finish();
	}

	public void onResume() {
		super.onResume();
		initButtonsNames();
	}

	private void initButtonsNames() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Constants.BUTTONS_NUMBER; i++)
			buttons[i].setText(sharedPrefNameButton[i].getString(
					getString(buttonStrNames[i]),
					getResources().getString(buttonStrNames[i])));
	}

	private void initButtonsSize() {
		// TODO Auto-generated method stub
		getButtonSize();
		for (int i = 0; i < Constants.BUTTONS_NUMBER; i++) {
			ViewGroup.LayoutParams params = buttons[i].getLayoutParams();
			params.width = buttonsSize.width;
			params.height = buttonsSize.height;
			buttons[i].setLayoutParams(params);
		}
	}

	private void createButtons() {
		// TODO Auto-generated method stub
		buttonsAdapter = new SmartHouseButtonsAdapter(this);
		keypadGrid = (GridView) findViewById(R.id.grdButtons);

		keypadGrid.setAdapter(buttonsAdapter);

		buttonsAdapter.setButtonOnClickListener(buttonOnClickListener);
		buttonsAdapter.setButtonOnLongClickListener(buttonOnLongClickListener);
	}

	private void createSharedPrefName() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Constants.BUTTONS_NUMBER; i++)
			sharedPrefNameButton[i] = context.getSharedPreferences(
					getString(buttonStrNames[i]), Context.MODE_PRIVATE);
	}

	private void createSharedPrefString() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Constants.BUTTONS_NUMBER; i++)
			sharedPrefStringButton[i] = context.getSharedPreferences(
					getString(buttonStrStrings[i]), Context.MODE_PRIVATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.ip_settings:
			openIpSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openIpSettings() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, IpSettingsActivity.class);
		startActivity(intent);
	}

	private void sendData(View view) {
		context = getApplicationContext();

		if(!isWifiConnected()) {
			showShortToast(Constants.WIFI_DISCONNECTED_MESSAGE);
			return;
		}

		textIp = sharedPrefIp.getString(getString(R.string.preference_ip),
				defaultIp);
		textPort = sharedPrefPort.getString(
				getString(R.string.preference_port), defaultPort);

		String host = textIp;
		if (!host.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
			showShortToast(Constants.INVALID_IP_ERROR_MESSAGE);
			return;
		}

		String port = textPort;
		if (!port.matches("^(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}|0)$")) {
			showShortToast(Constants.INVALID_PORT_ERROR_MESSAGE);
			return;
		}

		String dataText = getButtonText(view);
		String dataHex = "";
		if (dataText.length() < 1 && dataHex.length() < 2) {
			showShortToast(Constants.SENDING_CONTENT_ERROR_MESSAGE);
			return;
		}
		String uriString = "udp://" + host + ":" + port + "/";
		if (dataHex.length() >= 2) {
			uriString += Uri.encode("0x" + dataHex);
		} else {
			uriString += Uri.encode(dataText);
		}
		Uri uri = Uri.parse(uriString);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
		intent.addCategory(Intent.CATEGORY_DEFAULT);

		startActivity(intent);
	}

	private String getButtonText(View v) {
		String result = "";
		for (int i = 0; i < Constants.BUTTONS_NUMBER; i++) {
			if (buttonsRid[i] == v.getId()) {
				result = sharedPrefStringButton[i].getString(
						getString(buttonStrStrings[i]), getResources()
								.getString(buttonStrStrings[i]));
				break;
			}
		}
		return result;
	}

	private void initIPandPort() {
		context = getApplicationContext();
		sharedPrefIp = context.getSharedPreferences(
				getString(R.string.preference_ip), Context.MODE_PRIVATE);
		sharedPrefPort = context.getSharedPreferences(
				getString(R.string.preference_port), Context.MODE_PRIVATE);

		defaultIp = getResources().getString(R.string.defaultIP);
		defaultPort = getResources().getString(R.string.defaultPort);
	}

	private boolean isWifiConnected() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}

	void showShortToast(String text)
	{
	    if(currentToast != null)
	    {
	        currentToast.cancel();
	    }
	    currentToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
	    currentToast.show();

	}

	private void getButtonSize() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		buttonsSize.height = (metrics.heightPixels - 150) / 5;
		buttonsSize.width = (metrics.widthPixels - 50) / 3;
	}


    private static final int TCP_SERVER_PORT = 21111;
	private void runTcpClient() {
    	try {
			Socket s = new Socket("localhost", TCP_SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			//send output msg
			String outMsg = "TCP connecting to " + TCP_SERVER_PORT + System.getProperty("line.separator");
			out.write(outMsg);
			out.flush();
			Log.i("TcpClient", "sent: " + outMsg);
			//accept server response
			String inMsg = in.readLine() + System.getProperty("line.separator");
			Log.i("TcpClient", "received: " + inMsg);
			//close connection
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	//replace runTcpClient() at onCreate with this method if you want to run tcp client as a service

}
