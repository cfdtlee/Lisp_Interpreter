import java.io.*;
import java.util.*;
public class Interpreter {
	public static void main(String[] args) {
		Lexical lex = new Lexical();
		Printer pri = new Printer();
		Parse parse = new Parse(lex, pri);
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

class Sexp {
	public Boolean isList;
	// public Atom atom;
	public String kind;
	public String val;
	public Sexp left;
	public Sexp right;
	Sexp() {
		this.isList = true;
		this.kind = null;
		this.val = null;
		this.left = null;
		this.right = null;
	}
	void reset() {
		this.isList = true;
		this.kind = null;
		this.val = null;
		this.left = null;
		this.right = null;
	}
	public static Boolean isListTree(Sexp sexp) {
		if(sexp == null) {
			return true;
		}
		else if(sexp.right == null) {
			return true;
		}
		else if(sexp.right.right == null) {
			if(sexp.right.val.equals("NIL")) {
				// System.out.println("True with" + sexp.right.val);
			}
			else {
				// System.out.println("false!");
				return false;
			}
		}
		return isListTree(sexp.left) && isListTree(sexp.right);
	}
}

class Lexical {
	public File file;
	public String line;
	public InputStream reader = System.in;
	String getNextToken() {
		if(line == null) {

		}
		try {
			int tempchar;
			tempchar = reader.read();
			if(tempchar == -1) {
				return "EOF";
			}
			else if(tempchar == '\n') {
				// return "EOL";
				while(tempchar == '\n') {
					reader.mark(32);
					tempchar = reader.read();
				}
				System.in.reset();
				return getNextToken();
			}
			else if((char)tempchar == '(') {
				return "("; //OpenParenthesis";
			}
			else if((char)tempchar == ')') {
				return ")"; //ClosingParenthesis";
			}
			else if((char)tempchar == '.') {
				return ".";
			}
			else if(tempchar >= 'A' && tempchar <= 'Z') {
				String literalVal = new String();
				while((tempchar >= 'A' && tempchar <= 'Z') || (tempchar >= '0' && tempchar <='9')) {
					reader.mark(32);
					literalVal += String.valueOf((char) tempchar);
					tempchar = reader.read();
				}
				System.in.reset();
				return literalVal;
			}
			else if(tempchar >= '0' && tempchar <= '9') {
				String numericVal = new String();
				while(tempchar >= '0' && tempchar <='9') {
					reader.mark(32);
					numericVal += String.valueOf((char) tempchar);
					tempchar = reader.read();
					if(tempchar <= '0' && tempchar >= '9') {
						System.out.println("Invalid Input: " + numericVal + String.valueOf((char) tempchar) + "...");
						System.exit(1);
					}
				}
				reader.reset();
				return numericVal;
			} //    ?????
			else if(tempchar == ' ') {
				while(tempchar == ' ') {
					reader.mark(32);
					tempchar = reader.read();
				}			
				reader.reset();
				return getNextToken();
			}
			else {
				System.out.println("Invalid Input: " + String.valueOf((char) tempchar) + "...");
				System.exit(1);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERR";
	}
}

// build a binary tree that captures the structure of an S-expression.
// The leaves of this tree are atoms. 
class Parse {
	public Lexical lexical;
	public Printer printer;
	public List<Sexp> sexpList = new ArrayList<Sexp>();
	public Sexp sexp = new Sexp();
	public Sexp sexp0 = sexp;
	Parse(Lexical lex, Printer pri) {
		this.lexical = lex;
		this.printer = pri;
	}
	// ParseStart will call ParseSexp and then will check for end of file. 
	// If the end is reached, the parser will terminate. If not, ParseStart will call itself.
	void ParseStart() { 
		// sexpList.add(sexp);
		sexp = ParseSexp(0);
		if(sexp == null) {
			System.exit(0);
		}
		// check end?
		else if(sexp.val != null || sexp.left != null){
			printer.launch(sexp);
			ParseStart();
		}
		else {
			ParseStart();
		}
	}
	// get the next token. If it is not Atom or OpenParenthesis, an error will be reported. If it is Atom, the function returns. If it is OpenParenthesis, the function will call itself, then will get the next token, report an error if it is not Dot, call itself again, get the next token, and report an error if it is not ClosingParenthesis.
	Sexp ParseSexp(int dir) {
		String token = lexical.getNextToken();
		Sexp tempSexp = new Sexp();
		if(token == "EOF") {
			return null;
		}
		else if(token == "EOL") {
			System.out.print('\n');
			return tempSexp;
			// ParseSexp(0);
		}
		else if(token == "(") {
			tempSexp.left = ParseSexp(1);
			token = lexical.getNextToken();
			// System.out.println(token);
			if(token != ".") {
				System.out.println("ERROR: here should be a '.' not" + token);
				System.exit(1);
			}
			tempSexp.right = ParseSexp(2);
			token = lexical.getNextToken();
			// System.out.println(token);
			if(token != ")") {
				System.out.println("ERROR: here should be a ')'");
				System.exit(1);
			}
			// System.out.println(tempSexp.)
			return tempSexp;
		}
		else if(token.charAt(0) >= 'A' && token.charAt(0) <= 'Z') { // litera
			if(dir == 0) {
				tempSexp.kind = "literal";
				tempSexp.val = token;
			}
			else if(dir == 1) { // add to left child
				tempSexp.isList = false;
				tempSexp.kind = "literal";
				tempSexp.val = token;
			}
			else if(dir == 2) {
				tempSexp.isList = (token == "NIL");
				tempSexp.kind = token == "NIL" ? null : "literal";
				tempSexp.val = token;
			}
			return tempSexp;
		}
		else if(token.charAt(0) >= '0' && token.charAt(0) <= '9') { // numeric
			tempSexp.isList = false;
			tempSexp.kind = "numeric";
			tempSexp.val = token;
			return tempSexp;
		}
		else{
			System.out.println("ERROR: Invalid character" + token);
			System.exit(1);
			return tempSexp;
		}
	}
}

class Printer {
	void launch(Sexp sexp) {
		// System.out.println("print was launched");
		if(Sexp.isListTree(sexp)) {
			print(sexp, true);
			System.out.print('\n');
		}
		else {
			// System.out.println("print with false");
			printRaw(sexp);
			System.out.print('\n');
		}
	}
	void print(List<Sexp> sexpList) {
		// System.out.println("print List was called");
		int length = sexpList.size();
		// System.out.println(length);
		for(int i = 0; i < length; i++) {
			print(sexpList.get(i));
			System.out.print('\n');
		}
	}
	void print(Sexp sexp, Boolean withPa) {
		if(sexp == null) {
			return;
		}
		if(sexp.isList) {
			System.out.print("(");
			print(sexp.left, true);
			if(sexp.right != null && sexp.right.val != null && sexp.right.val.equals("NIL")) {
			}
			else {
				System.out.print(" ");
				print(sexp.right); 
			}
			System.out.print(")");
		}
		else if(sexp.val != null) {
			System.out.print(sexp.val);
		}
	}
	void print(Sexp sexp) {
		if(sexp == null) {
			return;
		}
		else if(sexp.isList) {
			print(sexp.left, true);
			if(sexp.right != null && sexp.right.val != null && sexp.right.val.equals("NIL")){
			}
			else {
				System.out.print(" ");
				print(sexp.right);
			}
		}
		else if(sexp.val != null) {
			// Java string cannot use "=="
			System.out.print(sexp.val);
		}
	}
	void printRaw(Sexp sexp) {
		if(sexp == null) {
			return;
		}
		else if(sexp.isList) {
			System.out.print("(");
			printRaw(sexp.left);
			System.out.print(" . ");
			printRaw(sexp.right); 
			System.out.print(")");
		}
		else {
			System.out.print(sexp.val);
		}
	}
}