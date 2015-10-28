import java.io.*;
import java.util.*;
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