package ru.ainc.textedit;

import android.os.*;
import android.preference.*;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.prefs);		
		
	}
	
}
