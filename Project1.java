import java.io.*;
import java.util.*;
public class Project1 {
	public static void main(String[] args) {
		// BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
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
}

class Atom {
	public String kind;
	public String literalVal;
	public int numericVal;
	Atom(String val) {
		this.kind = "literal";
		this.literalVal = val;
	}
	Atom(int val) {
		this.kind = "numeric";
		this.numericVal = val;
	}
}

class Lexical {
	public File file;
	public String line;
	public InputStream reader = System.in;
	// void setInputFile(String fileName) {
	// 	try{
	// 		this.file = new File(fileName);
	// 		this.reader = new InputStreamReader(new FileInputStream(file));	
	// 	} catch(Exception e) {
	// 		e.printStackTrace();
	// 	}
		
	// }
	String getNextToken() {
		if(line == null) {

		}
		try {
			int tempchar;
			tempchar = reader.read();
			// while ((tempchar = reader.read()) != -1) {
			// 	if (((char) tempchar) != '\r') {
			// 		System.out.print((char) tempchar);
			// 	}
			// }
			if(tempchar == -1) {
				return "EOF";
			}
			else if(tempchar == '\n') {
				return "EOL";
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
			// Printer pri = new Printer();
			// sexpList.remove(sexpList.size()-1);
			// pri.print(sexpList);
			System.exit(0);
		}
		// check end?
		else if(sexp.left != null){
			printer.print(sexp, true);
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
		// System.out.println(token);
		if(token == "EOF") {
			return null;
		}
		else if(token == "EOL") {
			// System.out.println("PaeseSexp reached EOL");
			// printer.print(tempSexp, true);
			System.out.print('\n');
			// System.out.println(sexp.val);
			// System.out.println("EOL" + sexp.val);
			// sexp.reset();
			// sexp = new Sexp();
			// sexp0 = sexp;
			return tempSexp;
			// ParseSexp(0);
		}
		else if(token == "(") {
			tempSexp.left = ParseSexp(1);
			token = lexical.getNextToken();
			// System.out.println(token);
			if(token != ".") {
				System.out.println("erro: here should be a '.' not" + token);
				System.exit(1);
			}
			tempSexp.right = ParseSexp(2);
			token = lexical.getNextToken();
			// System.out.println(token);
			if(token != ")") {
				System.out.println("erro: here should be a ')'");
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
		return tempSexp;
	}
}

class Printer {
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
			// System.out.println("Printer: Nothing to print!");
			return;
		}
		else if(sexp.isList) {
			// System.out.println("left print was called");
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
		else if(!sexp.val.equals("NIL")) {
			// System.out.println("sexp isnotList");
			System.out.print(sexp.val);
		}
	}
	void print(Sexp sexp) {
		// System.out.println("print was called");
		if(sexp == null) {
			return;
		}
		else if(sexp.isList) {
			// System.out.print("(");
			print(sexp.left, true);
			if(sexp.right != null && sexp.right.val != null && sexp.right.val.equals("NIL")){
			}
			else {
				System.out.print(" ");
				print(sexp.right);
			}
			// System.out.print(")");
		}
		else if(!sexp.val.equals("NIL")) {
			// System.out.println("sexp isnotList");
			// System.out.print(sexp.val.equals("NIL")); // Java string cannot use "=="
			System.out.print(sexp.val);

		}
	}
}