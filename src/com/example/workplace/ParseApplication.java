package com.example.workplace;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.ParseAnalytics;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();


		// Add your initialization code here
		Parse.initialize(this, "UkGPWGDUpc1OeJrSEEkhNvICSGJ9NlSJ8OZtYnOF", "7imj7XZXsVsYdIBjPsUuJBH8AXrYcx2hC9mlo5ck");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
