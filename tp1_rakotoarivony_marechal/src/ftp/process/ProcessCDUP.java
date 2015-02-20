package ftp.process;

import ftp.FTPClient;

/**
 * Class ProcessCDUP
 * Allow to go on the parent directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessCDUP implements ProcessCommand {

	@Override
	/**
	 * This method process the CDUP command
	 * @param param the parameters of the command
	 * @param client the client who execute the command
	 * @return 200 if everything works fine
	 * 		   550 if the directory can't be found or is inaccessible
	 */
	public int process(String[] param, FTPClient client) {
		String newParam[] = {"CWD",".."};
		int codeAnswer = new ProcessCWD().process(newParam, client);
		if (codeAnswer==250){
			client.setAdditionalAnswer("Okay");
			return 200;
		}
		else return codeAnswer;
	}

}
