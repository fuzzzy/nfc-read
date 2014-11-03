package com.fz.nfctest;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView nfcDataView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nfcDataView = (TextView)findViewById(R.id.nfcData);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onResume() {
	    super.onResume();
	    
	    Intent i = getIntent();
	    if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(i.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(i.getAction())) {
	    	Tag t = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	    	
	    	String res = toStr(t.getId()) + "; \n";
	    	
	    	NfcA t1 = NfcA.get(t);
	    	if(t1 != null) {
	    		res += "NfcA: " + toStr(t1.getAtqa()) + ", " + t1.getSak() + "; \n";
	    	}
	    	
	    	IsoDep t2 = IsoDep.get(t);
	    	if(t2 != null) {
	    		res += "IsoDep: " + toStr(t2.getHiLayerResponse()) + ", " + toStr(t2.getHistoricalBytes()) + "; \n";
	    	}
	    	
	    	MifareUltralight t3 = MifareUltralight.get(t);
	    	if(t3 != null) {
	    		try {
	    			t3.connect();
					res += "MifareU: " + t3.getType() + ", " + toStr(t3.readPages( 0x00 )) + toStr(t3.readPages( 0x04 )) + toStr(t3.readPages( 0x08 )) + toStr(t3.readPages( 0x0b ))+ "; \n";
					t3.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	
//	    	NdefFormatable t4 = NdefFormatable.get(t);
//	    	if(t4 != null) {
//	    		res += "NdefF: " + toStr(t4.) + ", " + toStr(t2.getHistoricalBytes()) + "; \n";
//	    	}
	    	
	    	nfcDataView.setText(res);
	    }
	}
	
	public static String toStr(byte[] arr) {
		if(arr == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder(arr.length*3 + 3);
		
		for(int i = 0; i < arr.length; i++) {
			sb.append(String.format("%02X ", arr[i]));
		}
		
		return sb.toString();
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
