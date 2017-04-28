package de.letsbuildacompiler.compiler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import jasmin.ClassFile;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CompilerTest {
	
	private Path tempDir;
  
  	@BeforeMethod
  	public void createTempDir() throws IOException {
  		tempDir = Files.createTempDirectory("compilerTest");
  	}
  
  	@AfterMethod
  	public void deleteTempDir() {
  		deleteRecursively(tempDir.toFile());
  	}
  
  	private void deleteRecursively(File file) {
  		if(file.isDirectory()){
  			for(File child : file.listFiles()){
  				deleteRecursively(child);
			}
		}
		if(!file.delete()){
			throw new Error("Could not find the file <" +file+ ">");
		}
  	}
  
  	@Test(dataProvider = "provide_code_expectedText")
	public void runningCodeoutputsExpectedText(String code, String expectedText) throws Exception {
		// executing...
		String actualOutput = compileAndRun(code);
		  
		//evaluating...
		Assert.assertEquals(actualOutput, expectedText);
	}
	
	@DataProvider
	public Object[][] provide_code_expectedText() {
		return new Object[][] {
			  {"println(1+2);","3" + System.lineSeparator()},
			  {"println(1+2+53);","56" + System.lineSeparator()},
			  {"println(1); println(2);",
				  "1" + System.lineSeparator() +
				  "2" + System.lineSeparator(),
			  },
			  {"println(3-2);","1" + System.lineSeparator()},
			  {"println(2*3);","6" + System.lineSeparator()},
			  {"println(8/2);","4" + System.lineSeparator()},
			  {"println(9-2*3);","3" + System.lineSeparator()},
			  {"println(8/2*4);","16" + System.lineSeparator()},
			  {"println(2+3*3);","11" + System.lineSeparator()},
			  {"println(18-2+7);","23" + System.lineSeparator()},
			  {"int x; x = 33; println(x);", "33" + System.lineSeparator()},
			  {"int y; y = 8843; println(y+2);","8845" + System.lineSeparator()},
			  {"int a; int b; a = 3; b = 5; println(a+b);","8" + System.lineSeparator()},
			  //{"bool x; x = true; println(x);", "true" + System.lineSeparator()},
			  {"int example() {return 1;} println(example());", "1" + System.lineSeparator()},
			  {"int example() {int i; i = 4; return i;} println(example());", "4" + System.lineSeparator()},
			  {"int example() {int i; i = 1; return i;} int i; i = 63; println(example()); println(i);", 
				  "1" + System.lineSeparator() 
				+ "63" + System.lineSeparator()
			  },
			  {"if (0) {println(42);} else {println(81);}", "81" + System.lineSeparator()},
			  {"if (1) {println(42);} else {println(81);}", "42" + System.lineSeparator()},
		};
	}
	
	private String compileAndRun(String code) throws Exception {
		code = Main.compile(new ANTLRInputStream(code));
		ClassFile classfile = new ClassFile();
		classfile.readJasmin(new StringReader(code), "", false);
		Path outputPath = tempDir.resolve(classfile.getClassName() + ".class");
		classfile.write(Files.newOutputStream(outputPath));
		return runJavaClass(tempDir,classfile.getClassName());	
	}
	
	private String runJavaClass(Path dir, String className) throws Exception {
		Process process = Runtime.getRuntime().exec(new String[]{"java","-cp",dir.toString(),className});
		try(InputStream in = process.getInputStream()){
			return new Scanner(in).useDelimiter("\\A").next();
		}
	}
}