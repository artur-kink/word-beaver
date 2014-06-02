package com.merccoder.wordbeaver;

import android.content.Context;
import android.widget.Button;

public class LetterButton extends Button {

	public boolean found;
	
	public LetterButton(Context context) {
		super(context);
		found = false;
	}

}
