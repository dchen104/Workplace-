package com.example.workplace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class JoinNetwork extends Activity {
	
	AlertDialog waitPop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_network);
		pleaseWaitDialog();
		loadSpinner();
		waitPop.cancel();
		final Button searchFilterButton = (Button) findViewById(R.id.searchFilterButton);
		searchFilterButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder searchDialog = new AlertDialog.Builder(JoinNetwork.this);
				searchDialog.setMessage("Enter text into the below field and the spinner will try to"
						+ "only diplay existing network names that closely or exactly match your entered"
						+ "text. You may then go back to the join network page and pick the appropriate"
						+ "network. Leave the field blank if you want to clear the search filter.");
				searchDialog.setTitle("Search filter");
				final EditText input = new EditText(JoinNetwork.this); 
				searchDialog.setView(input);
				searchDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						String networkFilter = input.getText().toString();
						dialog.cancel();
						pleaseWaitDialog();
						Spinner spin = (Spinner) findViewById(R.id.spinner1);
						ParseQuery<ParseObject> getAllNetworks = ParseQuery.getQuery("Networks");
						try {
							List<ParseObject> networks2 = getAllNetworks.find();
							ArrayAdapter<String> adapter = setSpinAdapter(searchFilterQuery(networks2,
									networkFilter));

							spin.setAdapter(adapter);
						}
						catch (ParseException e) {
							failedQuery();
						}
						waitPop.cancel();
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_network, menu);
		return true;
	}
	
	//Displays a waiting dialog.
	private void pleaseWaitDialog() {
		AlertDialog.Builder waitDialog = new AlertDialog.Builder(JoinNetwork.this);
		waitDialog.setMessage("Please wait...");
		waitPop = waitDialog.create();
		waitPop.show();
	}
	
	//Loads the spinner with all networks available to join
	private void loadSpinner() {
		ParseQuery<ParseObject> getAllNetworks = ParseQuery.getQuery("Networks");
		try {
			List<ParseObject> networks = getAllNetworks.find();
			

			Spinner spin = (Spinner) findViewById(R.id.spinner1);
			ArrayAdapter<String> adapter = setSpinAdapter(networks);

			spin.setAdapter(adapter);
	
		} catch (ParseException e) {
			failedQuery();
		}
		
	}
	
	//Builds an ArrayAdapter given the List of ParseObject data.
	private ArrayAdapter<String> setSpinAdapter(List<ParseObject> networks) {
		Iterator<ParseObject> it = networks.iterator();
		ParseObject element;
		ArrayList<String> networkNames = new ArrayList<String>();
		while(it.hasNext()) {
			element = it.next();
			networkNames.add(element.getString("name"));	
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(JoinNetwork.this, android.R.layout.simple_spinner_item,
				networkNames);
		return adapter;
	}
	
	//Builds a List of ParseObjects that match the search filter criteria.
	private List<ParseObject> searchFilterQuery(List<ParseObject> list, String name) {
		Iterator<ParseObject> it = list.iterator();
		ArrayList<ParseObject> arrayL = new ArrayList<ParseObject>();
		ParseObject element;
		while(it.hasNext()) {
			element = it.next();
			if(element.getString("name").contains(name)) {
				arrayL.add(element);
			}
		}
		return (List<ParseObject>) arrayL;
	}
	
	private void failedQuery() {
		AlertDialog.Builder errorDialog = new AlertDialog.Builder(JoinNetwork.this);
		errorDialog.setMessage("Could not fetch list of networks. Refresh page to try again.");
		errorDialog.setTitle("Error");
		errorDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		AlertDialog errorPop = errorDialog.create();
		errorPop.show();
	}

}
