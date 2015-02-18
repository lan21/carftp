package exception;

public class UnknownUserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnknownUserException(String s) {
		super(s);
	}

}
