package de.letsbuildacompiler.compiler;

import de.letsbuildacompiler.parser.DemoBaseVisitor;
import de.letsbuildacompiler.parser.DemoParser.*;


public class MyVisitor extends DemoBaseVisitor<String> {
	
	@Override
	public String visitPrintln(PrintlnContext ctx) {
		return "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
				visit(ctx.argument) + "\n" +
				"invokevirtual java/io/PrintStream/println(I)V\n";
	}
	
	@Override
	public String visitPLUS(PLUSContext ctx) {
		return visitChildren(ctx) + "\n" +   
				"iadd";
	}
	
	@Override
	public String visitMINUS(MINUSContext ctx) {
		return visitChildren(ctx) + "\n" +   
				"isub";
	}
	
	@Override
	public String visitDIV(DIVContext ctx) {
		return visitChildren(ctx) + "\n" +   
				"idiv";
	}
	
	@Override
	public String visitMULT(MULTContext ctx) {
		return visitChildren(ctx) + "\n" +   
				"imul";
	}
	
	@Override
	public String visitNumber(NumberContext ctx) {
		return "ldc " + ctx.num.getText();
	}
	
	@Override
	protected String aggregateResult(String aggregate, String nextResult) {
		if(aggregate == null){
			return nextResult;
		}
		if(nextResult == null){
			return aggregate;
		}
		return aggregate + "\n" + nextResult;
	}

}
