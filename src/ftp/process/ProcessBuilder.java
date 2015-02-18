package ftp.process;

public class ProcessBuilder {

	public ProcessCommand processBuild(String[] processString) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String command = processString[0];
		Class cls = Class.forName("ftp.process.Process"+command);
		Object obj = cls.newInstance();
		ProcessCommand cmd = (ProcessCommand) obj;
		return cmd;
	}

}
