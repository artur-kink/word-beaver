package com.merccoder.wordbeaver;

import java.io.IOException;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity{
	

	public Vector<Word> words;
	
	public Vector<Button> letterButtons;
	public Vector<Button> selectedButtons;
	
	public RelativeLayout gameScreen;
	
	public static Typeface loggerFont;
	
	public final int ROWS = 8;
	public final int COLUMN_WIDTH = 8;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        loggerFont = Typeface.createFromAsset(getAssets(), "font/Logger.ttf");
        
        letterButtons = new Vector<Button>();
        selectedButtons = new Vector<Button>();
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
	    RelativeLayout mainActivity = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);
	    mainActivity.setId(0);
	    setContentView(mainActivity);
	    
	    //Initialize game screen.
	    gameScreen = (RelativeLayout)inflater.inflate(R.layout.game_layout, null);
	    for(int i = 0; i < ROWS*COLUMN_WIDTH; i++){
	    	Button tile = new Button(this);
	    	tile.setId(i+1);
	    	

	    	double randomImage = Math.random();
	    	if(randomImage > 0.75)
	    		tile.setBackgroundResource(R.drawable.logbottom);
	    	else if(randomImage > 0.5)
	    		tile.setBackgroundResource(R.drawable.logleft);
	    	else if(randomImage > 0.25)
	    		tile.setBackgroundResource(R.drawable.logright);
	    	else
	    		tile.setBackgroundResource(R.drawable.log);

	    	tile.setTypeface(loggerFont);
	    	tile.setTextColor(Color.argb(255, 69, 46, 30));
	    	tile.setText("");
	    	
	    	/*
	    	double randomRotation = Math.random();
	    	if(randomRotation > 0.75)
	    		tile.setRotation(90);
	    	else if(randomRotation > 0.5)
	    		tile.setRotation(180);
	    	else if(randomRotation > 0.25)
	    		tile.setRotation(270);
	    	*/
	    	
	    	tile.setPadding(2, 2, 2, 2);
	    	tile.setFocusable(false);
	    	tile.setClickable(false);
	    	letterButtons.add(tile);
	    	
	    	int buttonWidth = (int)((getWindowManager().getDefaultDisplay().getWidth()*0.95)/8);
	    	
	    	tile.setTextSize((int)(buttonWidth*0.35));
	    	
	    	LayoutParams params = new LayoutParams(buttonWidth,
	    		buttonWidth);
	    	
	    	//Vertical alignment
	    	if(i >= 8){
	     		params.addRule(RelativeLayout.BELOW, i-8 + 1);
	    	}
	    	//Horizontal alignment
	    	if(i%8 > 0){
	    		params.addRule(RelativeLayout.RIGHT_OF, i-1 + 1);
	    	}
	    	
	    	gameScreen.addView(tile, params);
	    }
	    
	    LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	    	RelativeLayout.LayoutParams.WRAP_CONTENT);
 		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
 		params.addRule(RelativeLayout.CENTER_VERTICAL);
	    mainActivity.addView(gameScreen, params);
	    
	    
	    //Parse words lists.
	    Log.println(Log.INFO, "Init", "Loading word list");
	    XmlResourceParser xmlResource = getResources().getXml(R.xml.wordlist);
	    words = new Vector<Word>();
	    try {
			xmlResource.next();
			
			int eventType = xmlResource.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT){
				
				if(eventType == XmlPullParser.START_DOCUMENT){
					
			    }else if(eventType == XmlPullParser.START_TAG){
			    	Log.println(Log.INFO, "Init", xmlResource.getName());
			    	if(xmlResource.getName().compareTo("word") == 0){
			    		Word word = new Word();
			    		for(int i = 0; i < xmlResource.getAttributeCount(); i++){
			    			if(xmlResource.getAttributeName(i).compareTo("from") == 0){
			    				word.from = xmlResource.getAttributeValue(i);
			    			}else if(xmlResource.getAttributeName(i).compareTo("to") == 0){
			    				word.to = xmlResource.getAttributeValue(i);
			    			}else if(xmlResource.getAttributeName(i).compareTo("pronoun") == 0){
			    				word.pronoun = xmlResource.getAttributeValue(i);
			    			}else if(xmlResource.getAttributeName(i).compareTo("article") == 0){
			    				word.article = xmlResource.getAttributeValue(i);
			    			}
			    			Log.println(Log.INFO, "Init", xmlResource.getAttributeName(i) + ": " + xmlResource.getAttributeValue(i));
			    		}
			    		words.add(word);
			    	}
			    }else if(eventType == XmlPullParser.END_TAG){
			    	
			    }else if(eventType == XmlPullParser.TEXT){
			    	Log.println(Log.INFO, "Init", "TEXT: " + xmlResource.getText());
			    }
			    eventType = xmlResource.next();
			}
			
		}catch (Exception e) {
			if(e.toString() != null)
				Log.println(Log.ERROR, "Init", e.toString());
		}
	    Log.println(Log.INFO, "Init", "Word list loaded");
	    
	    for(int i = 0; i < 10; i++){
	    	Word word = words.get((int)(Math.random()*5));
	    	if(word.to.length() > 7)
	    		continue;
	    	
	    	for(int tries = 0; tries < 10; tries++){
	    		
	    		//Try to position word vertically
	    		if(Math.random() > 0.5){
	    			int r = (int)(Math.random()*(8-word.to.length()));
	    			int c = (int)(Math.random()*7);
	    			
	    			boolean fits = true;
	    			for(int l = 0; l < word.to.length(); l++){
	    				if(letterButtons.get(r*8 + c + l*8).getText().length() != 0 &&
	    						letterButtons.get(r*8 + c + l*8).getText().charAt(0) != word.to.charAt(l)){
	    					fits = false;
	    					break;
	    				}
	    			}
	    			
	    			if(fits){
	    				for(int l = 0; l < word.to.length(); l++){
		    				letterButtons.get(r*8 + c + l*8).setText("" + word.to.charAt(l));
		    			}
	    				break;
	    			}
	    		}else{
	    			//Try to position word horizontally
	    			int r = (int)(Math.random()*(7));
	    			int c = (int)(Math.random()*(7-word.to.length()));
	    			
	    			boolean fits = true;
	    			for(int l = 0; l < word.to.length(); l++){
	    				if(letterButtons.get(r*8 + c + l).getText().length() != 0  &&
	    					letterButtons.get(r*8 + c + l).getText().charAt(0) != word.to.charAt(l)){
	    					fits = false;
	    					break;
	    				}
	    			}
	    			
	    			if(fits){
	    				for(int l = 0; l < word.to.length(); l++){
		    				letterButtons.get(r*8 + c + l).setText("" + word.to.charAt(l));
		    			}
	    				break;
	    			}
	    		}
	    	}
	    }
	    
	    for(int i = 0; i < letterButtons.size(); i++){
	    	if(letterButtons.get(i).getText().length() == 0){
	    		letterButtons.get(i).setText("" + (char)(Math.random()*26 + 65));
	    	}
	    }
	    
	    clearButtons();
    }

    /**
     * Clear selected state of tile buttons.
     */
    public void clearButtons(){
    	selectedButtons.clear();
    	for(int i = 0; i < letterButtons.size(); i++){
    		setButtonSelected(letterButtons.get(i), false);
		}
    }
    
    public void setButtonSelected(Button button, boolean selected){
    	if(selected){
    		//button.setBackgroundColor(Color.WHITE);
    		button.setTextColor(Color.argb(255, 115, 87, 87));
    	}else{
    		//button.setBackgroundResource(R.drawable.log);
    		button.setTextColor(Color.argb(255, 69, 46, 30));
    	}
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
	    
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		clearButtons();
    		return true;
    	}
    	
    	if(event.getAction() == MotionEvent.ACTION_UP){
    		
    		if(selectedButtons.size() > 1){
	    		String word = "";
	    		if(selectedButtons.get(0).getId() < selectedButtons.lastElement().getId()){
		    		for(int i = 0; i < selectedButtons.size(); i++){
		    			word += selectedButtons.get(i).getText();
		    		}
	    		}else{
	    			for(int i = selectedButtons.size() - 1; i >= 0; i--){
		    			word += selectedButtons.get(i).getText();
		    		}
	    		}
	    		System.out.println("Selected Word: " + word);
	    		clearButtons();
    		}
    		return true;
    	}
    	
    	for(int i = 0; i < letterButtons.size(); i++){
    		
    		//Calculate the button collision rectangle.
    		Rect hitRect = new Rect();
    		letterButtons.get(i).getHitRect(hitRect);
    		hitRect.top += gameScreen.getY();
    		hitRect.bottom += gameScreen.getY();
    		hitRect.left += gameScreen.getX();
    		hitRect.right += gameScreen.getX();
    		
    		//Check if finger is over button.
    		if(hitRect.contains((int)event.getRawX(), (int)event.getRawY())){
    			
    			//Check that the button is not already selected
        		Button found = null;
        		for(int b = 0; b < selectedButtons.size(); b++){
        			if(letterButtons.get(i).getId() == selectedButtons.get(b).getId()){
        				found = letterButtons.get(i);
        				break;
        			}
        		}
        		if(found != null){
        			if(selectedButtons.size() > 1 && selectedButtons.get(selectedButtons.size() - 2) == found){
        				setButtonSelected(selectedButtons.lastElement(), false);
        				selectedButtons.remove(selectedButtons.lastElement());
        			}
        			continue;
        		}
    			
    			if(selectedButtons.size() == 0){
    				selectedButtons.add(letterButtons.get(i));
    				setButtonSelected(letterButtons.get(i), true);
    			}else if(selectedButtons.size() == 1){
    				//Check if the second button selected is next to the first one.
    				if(selectedButtons.get(0).getId() + COLUMN_WIDTH == letterButtons.get(i).getId() ||
    					selectedButtons.get(0).getId() - COLUMN_WIDTH == letterButtons.get(i).getId() ||
    					selectedButtons.get(0).getId() + 1 == letterButtons.get(i).getId() ||
    					selectedButtons.get(0).getId() - 1 == letterButtons.get(i).getId()){
    					selectedButtons.add(letterButtons.get(i));
    					setButtonSelected(letterButtons.get(i), true);
    				}
    			}else if(selectedButtons.size() > 1){
    				//Check if selection is along the existing column.
    				if(selectedButtons.get(0).getId() % COLUMN_WIDTH == letterButtons.get(i).getId() % COLUMN_WIDTH){
    					//Check if selection is next to the previous selected button.
    					if(selectedButtons.lastElement().getId() + COLUMN_WIDTH == letterButtons.get(i).getId()){
    						selectedButtons.add(letterButtons.get(i));
    						setButtonSelected(letterButtons.get(i), true);
        				}else if(selectedButtons.lastElement().getId() - COLUMN_WIDTH == letterButtons.get(i).getId()){
    						selectedButtons.add(letterButtons.get(i));
    						setButtonSelected(letterButtons.get(i), true);
        				}
    				//Check if selection is along the existing row.
    				}else if((selectedButtons.get(0).getId()-1) / COLUMN_WIDTH == (letterButtons.get(i).getId()-1) / COLUMN_WIDTH){
    					//Check if selection is next to the previous selected button.
    					if(selectedButtons.lastElement().getId() + 1 == letterButtons.get(i).getId()){
    						selectedButtons.add(letterButtons.get(i));
    						setButtonSelected(letterButtons.get(i), true);
        				}else if(selectedButtons.lastElement().getId() - 1 == letterButtons.get(i).getId()){
    						selectedButtons.add(letterButtons.get(i));
    						setButtonSelected(letterButtons.get(i), true);
        				}
    				}
    			}
    			
    			return true;
    		}
    	}
    	
	    return false;
	}

}
