import java.io.*;
public class Project1 {
	public static void main(String[] args) {
		// BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		for(int i = 0; i < args.length; i++) {
			if(args[i++] == "<") {
				Lexical lex = new Lexical(args[i]);
			}
			else if(args[i++] == ">") {
				Printer pri = new Printer(args[i]);
			}
		}
		Parse parse = new Parse(lex, pri);
		parse.ParseStart();
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
		this.isList = false;
		this.kind = null;
		this.atom = null;
		this.left = null;
		this.right = null;
	}
	void reset() {
		this.isList = false;
		this.kind = null;
		this.atom = null;
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
	public Reader reader;
	Lexical(String fileName) {
		this.file = new File(fileName);
		this.reader = new InputStreamReader(new FileInputStream(file));
	}
	String getNextToken() {
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
					reader.mark();
					literalVal += String.valueOf((char) tempchar);
					tempchar = reader.read();
				}
				reader.reset();
				return literalVal;
			}
			else if(tempchar >= '0' && tempchar <= '9') {
				String numericVal = new String();
				while(tempchar >= '0' && tempchar <='9') {
					reader.mark()
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

			}
			else {
				System.out.println("Invalid Input: " + String.valueOf((char) tempchar) + "...");
				System.exit(1);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

// build a binary tree that captures the structure of an S-expression.
// The leaves of this tree are atoms. 
class Parse {
	public Lexical lexical;
	public Printer printer;
	public List<Sexp> sexpList = new ArrayList<Sexp>();
	public Sexp sexp = new Sexp();
	Parse(Lexical lex, Printer pri) {
		this.lexical = lex;
		this.printer = pri;
	}
	// ParseStart will call ParseSexp and then will check for end of file. 
	// If the end is reached, the parser will terminate. If not, ParseStart will call itself.
	void ParseStart() { 
		if(ParseSexp(0) == "EOF") {
			System.exit(0);
		}
		// check end?
		else {
			ParseStart();
		}
	}
	// get the next token. 
	// If it is not Atom or OpenParenthesis, 
	// an error will be reported. 
	// If it is Atom, the function returns. 
	// If it is OpenParenthesis, 
	// the function will call itself, 
	// then will get the next token, 
	// report an error if it is not Dot, 
	// call itself again, get the next token, 
	// and report an error if it is not ClosingParenthesis.
	String ParseSexp(int dir) {
		String token = lexical.getNextToken();
		if(token == "EOF") {
			return token;
		}
		else if(token == "EOL") {
			sexpList.add(sexp);
			sexp.reset();
		}
		else if(token == "(") {
			ParseSexp(1);
			token = lexical.getNextToken();
			if(token != ".") {
				System.out.pringln("erro: here should be a '.'");
				System.exit(1);
			}
			ParseSexp(2);
			token = lexical.getNextToken();
			if(token != ")") {
				System.out.pringln("erro: here should be a ')'");
				System.exit(1);
			}
		}
		else if(token.charAt(0) >= 'A' && token.charAt(0) <= 'Z') { // litera
			if(dir == 0) {
				sexp.kind = "literal";
				sexp.val = token;
			}
			else if(dir == 1) {
				sexp.isList = true;
				sexp.left = new Sexp();
				sexp.left.kind = "literal";
				sexp.left.val = token;
				sexp.right = new Sexp();
				sexp = sexp.right;
			}
			else if(dir == 2) {
				sexp.isList = true;
				sexp.val = "NIL";
			}
		}
		else if(token.charAt(0) >= '0' && token.charAt(0) <= '9') { // numeric
			if(dir == 0) {
				sexp.kind = "numeric";
				sexp.val = token;
			}
			else if(dir == 1) {
				sexp.isList = true;
				sexp.left = new Sexp();
				sexp.left.kind = "numeric";
				sexp.left.val = token;
				sexp.right = new Sexp();
				sexp = sexp.right;
			}
			else if(dir == 2) {
				sexp.isList = true;
				sexp.val = "NIL";
			}
		}
	}
}

class Printer {
	// public List<Sexp> sexpList = new ArrayList<Sexp>();
	public File file;
	public Writer writer
	Printer(String fileName) {
		// this.length = len;
		// this.file = 
		try (writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName), "utf-8"))) {
   				writer.write("something");
			}
	}
	void print(List<Sexp> sexpList) {
		int length = sexpList.size();
		for(int i = 0; i < length; i++) {
			printList(length.get(i));
			// print("\n");
		}
	}
	void printList(Sexp sexp) {
		if(sexp.isList()) {
			// print("(");
			printList(sexp.left);
			// print(".");
			printList(sexp.right);
			// print(")");
		}
		else {
			// print(sexp.val);
		}
	}
}