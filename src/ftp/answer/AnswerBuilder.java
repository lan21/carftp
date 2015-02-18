package ftp.answer;

import java.util.HashMap;

public class AnswerBuilder {
	public HashMap<Integer, String> anwswers;
	
	public AnswerBuilder instance = new AnswerBuilder();
	
	private AnswerBuilder(){
		this.anwswers = new HashMap<Integer, String>();
		this.anwswers.put(220,"Service ready for new user");
		this.anwswers.put(230,"User logged in, proceed.");
		this.anwswers.put(331,"User name okay, need password.");
		this.anwswers.put(332,"Need account for logging in");
		this.anwswers.put(215,"UNIX System Type");
		this.anwswers.put(530, "Not logged in");
		this.anwswers.put(502,"Command not implemented");
	}

	public String getAnswer(int codeAnswer){
		return codeAnswer+" "+this.anwswers.get(codeAnswer);
	}
}
