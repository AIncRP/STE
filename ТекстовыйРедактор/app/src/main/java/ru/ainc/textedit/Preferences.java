package ru.ainc.textedit;

import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import android.support.v7.widget.*;

public class Preferences extends AppCompatActivity{

	AppCompatSpinner sp;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefs);
		
		LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
		
		LayoutInflater ltInflater = getLayoutInflater();
		
		View item = ltInflater.inflate(R.layout.pref, linLayout, false);
		
		item.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		
		linLayout.addView(item);
		
		sp = (AppCompatSpinner) findViewById(R.id.Spinner);
		
		
	}
	
}
