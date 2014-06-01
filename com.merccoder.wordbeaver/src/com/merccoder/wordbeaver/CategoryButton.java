package com.merccoder.wordbeaver;

import android.content.Context;
import android.widget.Button;

public class CategoryButton extends Button {

	public WordList list;
	
	public CategoryButton(Context context) {
		super(context);
	}

	public CategoryButton(Context context, WordList l) {
		super(context);
		list = l;
		
		setText(list.title);
    	setTypeface(MainActivity.deliusBoldFont);
    	
    	if(list.type.compareTo("vocabulary") == 0){
    		setBackgroundResource(R.drawable.pinkbutton);
    	}else if(list.type.compareTo("conjugation") == 0){
    		setBackgroundResource(R.drawable.violetbutton);
    	}else if(list.type.compareTo("verbs") == 0){
    		setBackgroundResource(R.drawable.purplebutton);
    	}
	}
	
}
