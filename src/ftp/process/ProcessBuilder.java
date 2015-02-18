package ftp.process;

public class ProcessBuilder {

	public ProcessCommand processBuild(String[] processString) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String command = processString[0];
		Class cls = Class.forName("ftp.process.Process"+command);
		Object obj = cls.newInstance();
		ProcessCommand cmd = (ProcessCommand) obj;
		return cmd;
	}


	public static void main(String[]args) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String[] processString = {"USER", "test"};
		String[] processString2 = {"CWD", "test"};
		ProcessBuilder pb = new ProcessBuilder();
		System.out.println((pb.processBuild(processString) instanceof ProcessCommand));
		System.out.println((pb.processBuild(processString) instanceof ProcessUSER));
		System.out.println((pb.processBuild(processString) instanceof ProcessSYST));
		System.out.println((pb.processBuild(processString2) instanceof ProcessCWD));

	}

}
