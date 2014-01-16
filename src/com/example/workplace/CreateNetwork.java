package com.example.workplace;

import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateNetwork extends Activity {
	
	String networkName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_network);
		
		final Button createNetworkButton = (Button) findViewById(R.id.createNetworkButton);
		createNetworkButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder waitDialog = new AlertDialog.Builder(CreateNetwork.this);
				waitDialog.setMessage("Please wait...");
				AlertDialog waitPop = waitDialog.create();
				waitPop.show();
				networkName = ((EditText) findViewById(R.id.newNetworkName)).getText().toString();
				if(!uniqueNetwork()) {
					waitPop.cancel();
					AlertDialog.Builder networkDialog = new AlertDialog.Builder(CreateNetwork.this);
					networkDialog.setTitle("Error");
					networkDialog.setMessage("The network name you have entered has already been taken, "
							+ "please select a new one.");
					networkDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText)findViewById(R.id.newNetworkName)).setText("");
							dialog.cancel();
						}
					});
					AlertDialog emailPop2 = networkDialog.create();
					networkDialog.show();
				}
				else {
					waitPop.cancel();
					AlertDialog.Builder networkDialog2 = new AlertDialog.Builder(CreateNetwork.this);
					networkDialog2.setTitle("Congratulations!");
					networkDialog2.setMessage("You have successfully created an account and new network"
							+ ". You may now send out email invitations for coworkers to join the network"
							+ "or simply wait for coworkers to join the network on their own. You will now"
							+ "be taken back to the login screen to login.");
					networkDialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText)findViewById(R.id.newNetworkName)).setText("");
							dialog.cancel();
						}
					});
				}
				
			}
			
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_network, menu);
		return true;
	}
	
	//Checks to see if chosen network name is unique.
	private boolean uniqueNetwork() {
		ParseQuery<ParseObject> findNetQuery = ParseQuery.getQuery("Networks");
		findNetQuery.whereEqualTo("name", networkName);
		try {
			List<ParseObject> list;

			list = findNetQuery.find();

			if (list.isEmpty()) {
				return true;
			}
			else return false;
		}
		catch (com.parse.ParseException e) {
			return false;
		}
		
	}

}
