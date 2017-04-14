package de.letsbuildacompiler.compiler;

import org.antlr.v4.runtime.*;

import de.letsbuildacompiler.parser.DemoLexer;
import de.letsbuildacompiler.parser.DemoParser;
import de.letsbuildacompiler.parser.DemoParser.ProgramContext;

public class Main {

	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRFileStream("code.demo");
		System.out.println(compile(input));
	}
	
	public static String compile(ANTLRInputStream input) {
		DemoLexer lexer = new DemoLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DemoParser parser = new DemoParser(tokens);
		
		ProgramContext tree = parser.program();
		return createJasminFile(new MyVisitor().visit(tree));
	}
	
	private static String createJasminFile(String instructions){
		return ".class public HelloWorld\n" +
				".super java/lang/Object\n" +
				"\n" + 
				instructions;
	}
}
