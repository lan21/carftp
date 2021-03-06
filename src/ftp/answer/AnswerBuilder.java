package ftp.answer;

import java.util.HashMap;

/**
 * the AnswerBuilder Class generate the appropriate message with the return code of a processCommand
 * @author Tanguy Maréchal, Allan Rakotoarivony
 */
public class AnswerBuilder {
	public HashMap<Integer, String> anwswers;
	
	public static AnswerBuilder instance = new AnswerBuilder();
	
	private AnswerBuilder(){
		this.anwswers = new HashMap<Integer, String>();
		
		this.anwswers.put(150,"File status okay. About to open data connection.");
		
		this.anwswers.put(200,"");
		this.anwswers.put(212,"");
		this.anwswers.put(215,"UNIX System Type");
		this.anwswers.put(220,"Service ready for new user");
		this.anwswers.put(221,"Bye bye");
		this.anwswers.put(226,"Closing data connection. File transfert done");
		this.anwswers.put(227,"");
		this.anwswers.put(250,"");
		this.anwswers.put(257,"");
		this.anwswers.put(230,"User logged in, proceed.");
		
		this.anwswers.put(331,"User name okay, need password.");
		this.anwswers.put(332,"Need account for logging in");
		
		this.anwswers.put(421,"Service not available, closing control connection");
		this.anwswers.put(425,"Can't open data connection");
		this.anwswers.put(426,"Connection closed. Transfer aborted.");
		this.anwswers.put(430,"Invalid username or password");
		this.anwswers.put(434,"Requested host unavailable");
		this.anwswers.put(451,"Requested action aborted. Local error in processing.");
		
		this.anwswers.put(501,"Syntax error in parameters or arguments");
		this.anwswers.put(502,"Command not implemented");		
		this.anwswers.put(530,"Not logged in.");
		this.anwswers.put(532,"");
		this.anwswers.put(550,"");
		this.anwswers.put(553,"Requested action not taken. File name not allowed.");
	}

	/**
	 * Return the message depending of the code returned by a processCommand
	 * @param codeAnswer the code which we need the message
	 * @param additionalAnswer the additional message to add to the normal message obtained with the code
	 * @return codeAnswer the formatted answer for the code
	 */
	public String buildAnswer(int codeAnswer,String additionalAnswer){
		
		return codeAnswer+" "+this.anwswers.get(codeAnswer)+additionalAnswer;
	}
}
