import java.io.*;
public class Project1 {
	public static void main(String[] args) {
		// BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		File file = new File("myInput");
		
		try {
			System.out.println("以字符为单位读取文件内容，一次读一个字节：");
			Reader reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
        	    if (((char) tempchar) != '\r') {
            	    System.out.print((char) tempchar);
            	}

			}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}

class Sexp {
	public Atom atom;
	public Sexp left;
	public Sexp right;
}

class Atom {
	public String kind;
	public String literalVal;
	public int numericVal;
}

class Lexical {
	void getNextToken() {

	}
}

class Parse {
	// ParseStart will call ParseSexp and then will check for end of file. 
	// If the end is reached, the parser will terminate. If not, ParseStart will call itself.
	void ParseStart() { 

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
	void ParseSexp() {

	}
}

// class Printer {

// }