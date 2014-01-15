package com.example.workplace;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RegistrationScreen extends Activity {
	
	private String email;
	private String pass1;
	private String pass2;
	private String realPass;
	private String phone;
	private String firstName;
	private String lastName;
	private int buttonChoice;
	private boolean uniqueEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_screen);
		
		final Button registerConButton = (Button) findViewById(R.id.registerConfirmButton);
		registerConButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				AlertDialog.Builder waitDialog = new AlertDialog.Builder(RegistrationScreen.this);
				waitDialog.setMessage("Please wait...");
				AlertDialog waitPop = waitDialog.create();
				waitPop.show();
				email = ((EditText) findViewById(R.id.emailRegister)).getText().toString();
				email = email.trim();
				phone = ((EditText) findViewById(R.id.phoneRegister)).getText().toString();
				phone = phone.trim();
				pass2 = ((EditText) findViewById(R.id.confirmPasswordRegister)).getText().toString();
				pass1 = ((EditText) findViewById(R.id.passwordRegister)).getText().toString();
				pass1.trim();
				pass2.trim();
				if (!validFields()) {
					waitPop.cancel();
					AlertDialog.Builder validDialog = new AlertDialog.Builder(RegistrationScreen.this);
					validDialog.setTitle("Error");
					validDialog.setMessage("Please fill all required fields");
					validDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});	
					AlertDialog validPop = validDialog.create();
					validPop.show();
				}
				else if (!emailUnique()) {
					waitPop.cancel();
					AlertDialog.Builder emailDialog = new AlertDialog.Builder(RegistrationScreen.this);
					emailDialog.setTitle("Error");
					emailDialog.setMessage("The e-mail you entered has already been taken, please use "
							+ "a different e-mail");
					emailDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText)findViewById(R.id.emailRegister)).setText("");
							dialog.cancel();
						}
					});	
					AlertDialog emailPop = emailDialog.create();
					emailPop.show();
				}
				else if (!(validEmail(email))) {
					waitPop.cancel();
					AlertDialog.Builder emailDialog2 = new AlertDialog.Builder(RegistrationScreen.this);
					emailDialog2.setTitle("Error");
					emailDialog2.setMessage("The e-mail you entered is not valid, please use "
							+ "a different e-mail");
					emailDialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText)findViewById(R.id.emailRegister)).setText("");
							dialog.cancel();
						}
					});
					AlertDialog emailPop2 = emailDialog2.create();
					emailPop2.show();
									
					
				}
				else if (!(validPhone(phone))) {
					waitPop.cancel();
					AlertDialog.Builder phoneDialog = new AlertDialog.Builder(RegistrationScreen.this);
					phoneDialog.setTitle("Error");
					phoneDialog.setMessage("The phone number you entered is not valid, please use "
							+ "a different phone number. Valid phone number formats (Where d represents a digit)"
							+ "include (ddd)ddddddd, (ddd) ddddddd, (ddd)-ddddddd, (ddd)-ddd-dddd, (ddd)ddd-dddd"
							+ ", (ddd) ddd-dddd, ddd-ddd-dddd, ddd ddddddd, ddd-ddddddd, ddd ddd-dddd,"
							+ ", dddddd-dddd, and dddddddddd");
					phoneDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText) findViewById(R.id.phoneRegister)).setText("");
							dialog.cancel();
						}
					});
					AlertDialog phonePop = phoneDialog.create();
					phonePop.show();
				}
				else if (!(matchingPasswords(pass1, pass2))) {
					waitPop.cancel();
					AlertDialog.Builder passDialog = new AlertDialog.Builder(RegistrationScreen.this);
					passDialog.setTitle("Error");
					passDialog.setMessage("Passwords do not match. Please re-enter passwords");
					passDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((EditText) findViewById(R.id.passwordRegister)).setText("");
							((EditText) findViewById(R.id.confirmPasswordRegister)).setText("");
							dialog.cancel();
						}
					});
					AlertDialog passPop = passDialog.create();
					passPop.show();
				}
				else {

					waitPop.cancel();
					
					RadioGroup radio = (RadioGroup) findViewById(R.id.networkRegister);
					if (radio.getCheckedRadioButtonId() == 0) {
						Intent joinNetworkIntent = new Intent(RegistrationScreen.this, 
								JoinNetwork.class);
						joinNetworkIntent.putExtra("email", email);
						joinNetworkIntent.putExtra("password", pass1);
						joinNetworkIntent.putExtra("firstName", firstName);
						joinNetworkIntent.putExtra("lastName", lastName);
						joinNetworkIntent.putExtra("phoneNum", phone);
						startActivity(joinNetworkIntent);
						
					}
					else {
						Intent createNetworkIntent = new Intent(RegistrationScreen.this, 
								CreateNetwork.class);
						createNetworkIntent.putExtra("email", email);
						createNetworkIntent.putExtra("password", pass1);
						createNetworkIntent.putExtra("firstName", firstName);
						createNetworkIntent.putExtra("lastName", lastName);
						createNetworkIntent.putExtra("phoneNum", phone);
						
						startActivity(createNetworkIntent);
					}
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration_screen, menu);
		return true;
	}
	
	//Makes sure all fields have data entered into them.
	private boolean validFields() {
		firstName = ((EditText) findViewById(R.id.firstRegister)).getText().toString().trim();
		lastName = ((EditText) findViewById(R.id.lastRegister)).getText().toString().trim();
		String pass1Temp = pass1.trim();
		String pass2Temp = pass2.trim();
		return (email.length() > 0 && phone.length() > 0 && pass1Temp.length() > 0 && 
				pass2Temp.length() > 0 && firstName.length() > 0 &&
				lastName.length() > 0);
		//isEmpty(((EditText) findViewById(R.id.firstRegister)));
	}
	
	//Checks to see if entered email is valid:
	private boolean validEmail(String emailText) {
		
		if(!emailText.contains("@")) {
			return false;
		}
		if (emailText.indexOf("@") != emailText.lastIndexOf("@")) {
			return false;
		}
		String emailFirst = emailText.split("@")[0];
		String emailSecond = emailText.split("@")[1];
		
		for (int i = 1; i < emailFirst.length(); i++) {
			if (emailFirst.charAt(i) == '.' && emailFirst.charAt(i - 1) == '.') {
				return false;
			}
			
		
		}
		if (emailSecond.lastIndexOf(".") < emailSecond.length() - 1) {
			
		}
		else return false;
		
		for (int i = 1; i < emailSecond.length(); i++) {
			if (emailSecond.charAt(i) == '.' && emailSecond.charAt(i - 1) == '.') {
				return false;
			}			
		}
		
		return true;
	}
	
	//Helper function that decides if anyone else has used that email before
	private boolean emailUnique() {
		ParseQuery<ParseObject> findEmailQuery = ParseQuery.getQuery("GlobalUsers");
		findEmailQuery.whereEqualTo("email", email);
		try {
			List<ParseObject> queryList = findEmailQuery.find();
			if (queryList.isEmpty()) {
				return true;
			}
			else return false;
		}
		catch (ParseException e) {
			return false;
		}
		

	}
	
	//Checks to see if entered phone number is valid.
	private boolean validPhone(String phoneText) {

		StringBuilder phoneStr = new StringBuilder(phoneText);
		if(phoneStr.charAt(0) == '(' && phoneStr.length() > 4 && phoneStr.charAt(4) != ')') {
			return false;
		}
		else if (phoneStr.charAt(0) == '('){
			phoneStr.deleteCharAt(0);
			phoneStr.deleteCharAt(3);
			
		}

		if(phoneStr.charAt(3) == ' ' || phoneStr.charAt(3) == '-') {
			phoneStr.deleteCharAt(3);
		}
		if(phoneStr.charAt(6) == '-') {
			phoneStr.deleteCharAt(6);
		}
		phoneText = phoneStr.toString().trim();
		if(phoneText.length() != 10) return false;
		
		String test;
		int digit;
		for (int i = 0; i < phoneText.length(); i++) {
			try {
				test = "" + phoneText.charAt(i);
				digit = Integer.parseInt(test);
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
		this.phone = phoneText;
		return true;
	}
	
	//Checks to see if passwords match
	private boolean matchingPasswords(String pass1, String pass2) {
		if(pass1.equals(pass2)) {
			return true;
		}
		else {
			return false;
		}
	}

}
