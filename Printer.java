import java.io.*;
import java.util.*;
class Printer {
	void launch(Sexp sexp) {
		// System.out.println("print was launched");
		if(Sexp.isListTree(sexp) && sexp.val == null) {
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
		else if(sexp.val == null) {
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