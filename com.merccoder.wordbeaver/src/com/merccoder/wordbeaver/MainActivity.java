package com.merccoder.wordbeaver;

import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	
	//List of word lists.
	public Vector<WordList> wordLists;

	public RelativeLayout mainScreen;
	public View currentScreen;
	
	
	public RelativeLayout startScreen;
	public Button newGameButton;
	public Button achievementButton;
	public Button settingsButton;
	public Button newThemesButton;
	
	
	public ScrollView gameOptionsScreen;
	
	
	public RelativeLayout gameScreen;
	public RelativeLayout tilesContainer;
	public TextView usedWordsTextViews[];
	public Vector<Button> letterButtons;
	public Vector<Button> selectedButtons;
	
	
	public static Typeface loggerFont;
	public static Typeface deliusFont;
	public static Typeface deliusBoldFont;
	
	//Number of rows in letter tile buttons.
	public final int ROWS = 8;
	//Number of buttons in each letter tile row.
	public final int COLUMN_WIDTH = 8;
	
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        loggerFont = Typeface.createFromAsset(getAssets(), "font/Logger.ttf");
        deliusFont = Typeface.createFromAsset(getAssets(), "font/delius-regular.ttf");
        deliusBoldFont = Typeface.createFromAsset(getAssets(), "font/deliusuni-bold.ttf");
        
        letterButtons = new Vector<Button>();
        selectedButtons = new Vector<Button>();
        
        loadWordLists();
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        mainScreen = (RelativeLayout)inflater.inflate(R.layout.activity_main, null);
        mainScreen.setId(0);
	    setContentView(mainScreen);
	    
	    
	    //Initialize start screen.
	    startScreen = (RelativeLayout)inflater.inflate(R.layout.start_screen, null);
	    newGameButton = (Button)startScreen.findViewById(R.id.new_game);
	    newGameButton.setTypeface(deliusBoldFont);
	    newGameButton.setOnClickListener(this);
	    
	    achievementButton = (Button)startScreen.findViewById(R.id.achievements);
	    achievementButton.setTypeface(deliusBoldFont);
	    achievementButton.setOnClickListener(this);
	    
	    settingsButton = (Button)startScreen.findViewById(R.id.settings);
	    settingsButton.setTypeface(deliusBoldFont);
	    settingsButton.setOnClickListener(this);
	    
	    newThemesButton = (Button)startScreen.findViewById(R.id.new_themes);
	    newThemesButton.setTypeface(deliusBoldFont);
	    newThemesButton.setOnClickListener(this);
	    
	    
	    //Initialize game options screen.
	    gameOptionsScreen = (ScrollView)inflater.inflate(R.layout.game_options, null);
	    LinearLayout categoryList = (LinearLayout)gameOptionsScreen.findViewById(R.id.category_list);
	    for(int i = 0; i < wordLists.size(); i++){
	    	CategoryButton categoryButton = new CategoryButton(this, wordLists.get(i));
	    	categoryButton.setOnClickListener(this);
	    	
	    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	    	    RelativeLayout.LayoutParams.WRAP_CONTENT);
	    	params.setMargins(0, 24, 0, 0);
	    	categoryList.addView(categoryButton, params);
	    }
	    
	    
	    //Initialize game screen.
	    gameScreen = (RelativeLayout)inflater.inflate(R.layout.game_layout, null);
	    tilesContainer = (RelativeLayout)gameScreen.findViewById(R.id.tiles_container);
	    usedWordsTextViews = new TextView[8];
	    usedWordsTextViews[0] = (TextView)gameScreen.findViewById(R.id.word1);
	    usedWordsTextViews[1] = (TextView)gameScreen.findViewById(R.id.word2);
	    usedWordsTextViews[2] = (TextView)gameScreen.findViewById(R.id.word3);
	    usedWordsTextViews[3] = (TextView)gameScreen.findViewById(R.id.word4);
	    usedWordsTextViews[4] = (TextView)gameScreen.findViewById(R.id.word5);
	    usedWordsTextViews[5] = (TextView)gameScreen.findViewById(R.id.word6);
	    usedWordsTextViews[6] = (TextView)gameScreen.findViewById(R.id.word7);
	    usedWordsTextViews[7] = (TextView)gameScreen.findViewById(R.id.word8);
	    
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
	    	
	    	
	    	tile.setPadding(2, 2, 2, 2);
	    	tile.setFocusable(false);
	    	tile.setClickable(false);
	    	letterButtons.add(tile);
	    	
	    	int buttonWidth = (int)((getWindowManager().getDefaultDisplay().getWidth()*0.95)/8);
	    	
	    	tile.setTextSize((int)(buttonWidth*0.35));
	    	tile.setWidth(buttonWidth);
	    	tile.setHeight(buttonWidth);
	    	
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
	    	
	    	tilesContainer.addView(tile, params);
	    }
 		gameScreen.setPadding(0, 0, 0, 25);
 		
 		
	    setScreen(startScreen);
    }

    /**
     * Loads word lists from xml.
     */
    public void loadWordLists(){
    	//Parse words lists.
	    Log.println(Log.INFO, "Init", "Loading word list");
	    XmlResourceParser xmlResource = getResources().getXml(R.xml.wordlist);
	    wordLists = new Vector<WordList>();
	    
	    try {
			xmlResource.next();
			
			int eventType = xmlResource.getEventType();
			WordList currentList = null;
			while (eventType != XmlPullParser.END_DOCUMENT){
				
				if(eventType == XmlPullParser.START_DOCUMENT){
			    }else if(eventType == XmlPullParser.START_TAG){
			    	Log.println(Log.INFO, "Init", xmlResource.getName());
			    	if(xmlResource.getName().compareTo("word") == 0){
			    		//Parse <word> tag data
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
			    		currentList.words.add(word);
			    	}else if(xmlResource.getName().compareTo("list") == 0){
			    		//Parse <list> tag data
			    		currentList = new WordList();
			    		wordLists.add(currentList);
			    		for(int i = 0; i < xmlResource.getAttributeCount(); i++){
			    			if(xmlResource.getAttributeName(i).compareTo("type") == 0){
			    				currentList.type = xmlResource.getAttributeValue(i);
			    			}else if(xmlResource.getAttributeName(i).compareTo("lang") == 0){
			    				currentList.language = xmlResource.getAttributeValue(i);
			    			}else if(xmlResource.getAttributeName(i).compareTo("title") == 0){
			    				currentList.title = xmlResource.getAttributeValue(i);
			    			}
			    			Log.println(Log.INFO, "Init", xmlResource.getAttributeName(i) + ": " + xmlResource.getAttributeValue(i));
			    		}
			    	}
			    }
			    eventType = xmlResource.next();
			}
			
		}catch (Exception e) {
			if(e.toString() != null)
				Log.println(Log.ERROR, "Init", e.toString());
		}
	    Log.println(Log.INFO, "Init", "Word list loaded");
    }
    
    /**
     * Set current game screen.
     * @param screen Screen to set to.
     */
    public void setScreen(View screen){
    	mainScreen.removeView(currentScreen);
    	currentScreen = screen;
    	
    	if(screen == startScreen){
    		LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
    	    	RelativeLayout.LayoutParams.MATCH_PARENT);
    	    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	    //params.addRule(RelativeLayout.CENTER_VERTICAL);
    	    mainScreen.addView(screen, params);
    	}else if(screen == gameOptionsScreen){
    		LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
        	    RelativeLayout.LayoutParams.MATCH_PARENT);
        	mainScreen.addView(screen, params);
        }else if(screen == gameScreen){
    		LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
    	    	RelativeLayout.LayoutParams.WRAP_CONTENT);
    	    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
    	    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	    mainScreen.addView(screen, params);
    	}
    	
    }
    
    /**
     * Populate game view.
     */
    public void createGame(WordList list){
    	
    	//Clear characters from letter buttons.
    	for(int i = 0; i < letterButtons.size(); i++){
	    	letterButtons.get(i).setText("");
	    }
    	
    	//List of used words, to avoid doubles.
    	Vector<Word> wordsUsed = new Vector<Word>();
    	
    	//Populate words into game tiles.
	    for(int i = 0; i < 8; i++){
	    	Word word = list.words.get((int)(Math.random()*list.words.size()));
	    	
	    	//Make sure word fits.
	    	if(word.to.length() > 8){
	    		i--;
	    		continue;
	    	}
	    	
	    	//Check if word already in use.
	    	if(wordsUsed.contains(word)){
	    		i--;
	    		continue;
	    	}
	    	
	    	for(int tries = 0; tries < 25; tries++){
	    		
	    		//Try to position word vertically
	    		if(Math.random() > 0.5){
	    			int r = (int)(Math.random()*(8-word.to.length()));
	    			int c = (int)(Math.random()*8);
	    			
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
	    				wordsUsed.add(word);
	    				usedWordsTextViews[wordsUsed.size()-1].setText(word.from);
	    				break;
	    			}
	    		}else{
	    			//Try to position word horizontally
	    			int r = (int)(Math.random()*(8));
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
	    				wordsUsed.add(word);
	    				usedWordsTextViews[wordsUsed.size()-1].setText(word.from);
	    				break;
	    			}
	    		}
	    	}
	    }
	    
	    //Fill random letters for unused buttons
	    for(int i = 0; i < letterButtons.size(); i++){
	    	if(letterButtons.get(i).getText().length() == 0){
	    		letterButtons.get(i).setText("" + (char)(Math.random()*26 + 65));
	    	}
	    }
	    
	    clearButtons();
    }
    
    /**
     * Clear selected state of tile letter buttons.
     */
    public void clearButtons(){
    	selectedButtons.clear();
    	for(int i = 0; i < letterButtons.size(); i++){
    		setButtonSelected(letterButtons.get(i), false);
		}
    }
    
    /**
     * Mark a letter button as selected or unselected.
     * @param button Button to mark.
     * @param selected Selected state.
     */
    public void setButtonSelected(Button button, boolean selected){
    	if(selected){
    		button.setTextColor(Color.argb(255, 115, 87, 87));
    	}else{
    		button.setTextColor(Color.argb(255, 69, 46, 30));
    	}
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
	    
    	if(currentScreen == gameScreen){
    	
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
	    	
	    	//Check for letter button inputs
	    	for(int i = 0; i < letterButtons.size(); i++){
	    		
	    		//Calculate the button collision rectangle.
	    		Rect hitRect = new Rect();
	    		letterButtons.get(i).getHitRect(hitRect);
	    		hitRect.top += gameScreen.getY() + tilesContainer.getY();
	    		hitRect.bottom += gameScreen.getY() + tilesContainer.getY();
	    		hitRect.left += gameScreen.getX() + tilesContainer.getX();
	    		hitRect.right += gameScreen.getX() + tilesContainer.getX();
	    		
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
	        		//If button is already selected, check if we are unselecting it
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
    	}
	    return false;
	}

	@Override
	public void onClick(View v) {
		if(v == newGameButton){
			setScreen(gameOptionsScreen);
		}else if(v instanceof CategoryButton){
			createGame(((CategoryButton)v).list);
			setScreen(gameScreen);
		}
	}
	
	@Override
	public void onBackPressed (){
		if(currentScreen == startScreen){
			super.onBackPressed();
		}else{
			setScreen(startScreen);
		}
	}

}
