package ftp.process;

public interface ProcessCommand {
	/**
	 * process the parameter for this command and return the FTP code corresponding to the processing
	 * @param param
	 * @return
	 */
	public int process(String param);
}
