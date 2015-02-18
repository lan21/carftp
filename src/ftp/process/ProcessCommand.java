package ftp.process;

import ftp.FTPClient;

public interface ProcessCommand {
	/**
	 * process the parameter for this command and return the FTP code corresponding to the processing
	 * @param param
	 * @param client TODO
	 * @return
	 */
	public int process(String[] param, FTPClient client);
}
