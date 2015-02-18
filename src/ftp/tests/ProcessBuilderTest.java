package ftp.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import ftp.process.ProcessBuilder;
import ftp.process.ProcessCWD;
import ftp.process.ProcessCommand;
import ftp.process.ProcessSYST;
import ftp.process.ProcessUSER;

public class ProcessBuilderTest {

	@Test 
	public void processBuildTest(){
		String[] processString = {"USER", "test"};
		String[] processString2 = {"CWD", "test"};
		ProcessBuilder pb = new ProcessBuilder();
		try {
			assertTrue(pb.processBuild(processString) instanceof ProcessCommand);
			assertTrue(pb.processBuild(processString) instanceof ProcessUSER);
			assertFalse(pb.processBuild(processString) instanceof ProcessSYST);
			assertTrue(pb.processBuild(processString2) instanceof ProcessCWD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = ClassNotFoundException.class)
	public void processBuildTestException() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String[] processString = {"USERS", "test"};
		ProcessBuilder pb = new ProcessBuilder();
		ProcessCommand pc = pb.processBuild(processString);
	}

}
