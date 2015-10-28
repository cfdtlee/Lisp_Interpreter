import java.io.*;
import java.util.*;
public class Interpreter {
	public static void main(String[] args) {
		Lexical lex = new Lexical();
		Printer pri = new Printer();
		Evaluator eva = new Evaluator();
		Parse parse = new Parse(lex, pri, eva);
		parse.ParseStart();
		
		// String token = lex.getNextToken();
		// while(token != "EOF") {
		// 	try {
		// 		System.out.print(token);
		// 	} catch(Exception e) {
		// 		e.printStackTrace();
		// 	}
		// 	token = lex.getNextToken();
		// }	
	}
}