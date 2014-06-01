package com.merccoder.wordbeaver;

import java.util.Vector;

public class WordList {

	public String title;
	
	public String type;
	public String language;
	
	public Vector<Word> words;
	
	public WordList(){
		words = new Vector<Word>();
	}
}
