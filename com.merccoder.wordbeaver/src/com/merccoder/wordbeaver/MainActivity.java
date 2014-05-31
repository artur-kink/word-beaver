package com.merccoder.wordbeaver;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
	    RelativeLayout mainActivity = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);
	    setContentView(mainActivity);
	    
	    LinearLayout gameScreen = (LinearLayout)inflater.inflate(R.layout.game_layout, null);
	    
	    for(int i = 0; i < 6; i++){
	    	Button tile = new Button(this);
	    	tile.setId(i+1);
	    	tile.setWidth(32);
	    	tile.setHeight(32);
	    	gameScreen.addView(tile);
	    }
	    
	    LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	    	RelativeLayout.LayoutParams.WRAP_CONTENT);
 		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
 		params.addRule(RelativeLayout.CENTER_VERTICAL);
	    mainActivity.addView(gameScreen, params);
    }

}
