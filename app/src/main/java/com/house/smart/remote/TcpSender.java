package com.house.smart.remote;

import android.content.Context;
import android.content.Intent;

public class TcpSender {
	
	public void runTcpClientAsService(Context context) {
		Intent lIntent = new Intent(context.getApplicationContext(), TcpClientService.class);
		context.startService(lIntent);
	}
}
