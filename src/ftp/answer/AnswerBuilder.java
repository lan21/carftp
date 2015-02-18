package ftp.answer;

import java.util.HashMap;

public class AnswerBuilder {
	public HashMap<Integer, String> anwswers;
	
	public static AnswerBuilder instance = new AnswerBuilder();
	
	private AnswerBuilder(){
		this.anwswers = new HashMap<Integer, String>();
		
		this.anwswers.put(150,"File status okay. About to open data connection.");
		
		this.anwswers.put(200,"Port OK");
		this.anwswers.put(215,"UNIX System Type");
		this.anwswers.put(220,"Service ready for new user");
		this.anwswers.put(226,"Closing data connection. File transfert done");
		this.anwswers.put(230,"User logged in, proceed.");
		
		this.anwswers.put(331,"User name okay, need password.");
		this.anwswers.put(332,"Need account for logging in");
		
		this.anwswers.put(425,"Can't open data connection");
		this.anwswers.put(434,"Requested host unavailable");
		
		this.anwswers.put(502,"Command not implemented");		
		this.anwswers.put(530, "Not logged in");
	}

	public String buildAnswer(int codeAnswer,String additionalAnswer){
		return codeAnswer+" "+this.anwswers.get(codeAnswer)+additionalAnswer;
	}
}
