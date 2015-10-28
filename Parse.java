import java.io.*;
import java.util.*;
// build a binary tree that captures the structure of an S-expression.
// The leaves of this tree are atoms. 
class Parse {
	public Lexical lexical;
	public Printer printer;
	public Evaluator evaluator;
	public List<Sexp> sexpList = new ArrayList<Sexp>();
	public Sexp sexp = new Sexp();
	public Sexp sexp0 = sexp;
	Parse(Lexical lex, Printer pri, Evaluator eva) {
		this.lexical = lex;
		this.printer = pri;
		this.evaluator = eva;
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
			sexp = evaluator.eval(sexp);
			// System.out.println("sexp.val = "+sexp.val);
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
				tempSexp.isList = false;
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