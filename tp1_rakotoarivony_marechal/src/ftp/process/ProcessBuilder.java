package ftp.process;

/**
 * Class ProcessBuilder 
 * Construct a instanceof ProcessCommand depending on a string
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessBuilder {

	/**
	 * Return a processCommand dependin of the first index of the string passed in parameter
	 * @param processString a string array made of the two parts of the command send by the client
	 * @return the processCommand depending of the string array passed in parameters
	 * @throws ClassNotFoundException the class 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ProcessCommand processBuild(String[] processString) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String command = processString[0];
		Class<?> cls = Class.forName("ftp.process.Process"+command);
		Object obj = cls.newInstance();
		ProcessCommand cmd = (ProcessCommand) obj;
		return cmd;
	}

}
