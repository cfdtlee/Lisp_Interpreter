import java.io.*;
import java.util.*;
// T, NIL, 
//   ATOM, EQ,
//   NULL, INT, QUOTE
// PLUS, MINUS, TIMES, QUOTIENT, REMAINDER, 
// LESS, GREATER, 
// CONS, CAR, CDR, COND, 
class Evaluator {
	Sexp eval(Sexp sexp) {
		if (atom(sexp)) {
			// atom
			return sexp;
		}
		else {
			if (sexp.left.val.equals("QUOTE")) {
				return car(cdr(sexp));
			}
			if (sexp.left.val.equals("COND")) {
				return evcon(cdr(sexp));
			}
			// System.out.println("apply");
			return apply(car(sexp), evlist(cdr(sexp))); // a, b
		}
	}
	Sexp apply(Sexp f, Sexp x) {
		if (atom(f)) {
			// atom
			// System.out.println("atom" + f.val);
			if(eq(f, "CAR")) {
				return car(car(x));
			}
			if(eq(f, "CDR")) {
				return cdr(car(x));
			}
			if(eq(f, "CONS")) {
				return cons(car(x), car(cdr(x)));
			}
			if(eq(f, "EQ")) {
				Sexp t = new Sexp();
				if(eq(car(x), car(cdr(x)))) {
					t.val = "T";
					return t;
				}
				else {
					t.val = "NIL";
					return t;
				}
			}
			if(eq(f, "ATOM")) {
				Sexp t = new Sexp();
				if(atom(car(x))) {
					// System.out.println("atom?");
					t.val = "T";
					return t;
				}
				else {
					t.val = "NIL";
					return t;
				}
			}
			if(eq(f, "NULL")) {
				Sexp t = new Sexp();
				if(nil(car(x))) {
					t.val = "T";
					return t;
				}
				else {
					t.val = "NIL";
					return t;
				}
			}
			if(eq(f, "INT")) {
				Sexp t = new Sexp();
				if(inte(car(x))) {
					t.val = "T";
					return t;
				}
				else {
					t.val = "NIL";
					return t;
				}
			}
			if(eq(f, "PLUS")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) + Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("PLUS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "MINUS")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) - Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("MINUS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "TIMES")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) * Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("TIMES must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "QUOTIENT")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x))) && !car(cdr(x)).val.equals("0")) {
					int i = Integer.parseInt(car(x).val) / Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else if(!car(cdr(x)).val.equals("0")){
					System.out.println("QUOTIENT must has two INT opts");
					System.exit(1);
					return null;
				}
				else {
					System.out.println("TRY to divide by 0");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "REMAINDER")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) % Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("REMAINDER must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "LESS")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					Boolean i = Integer.parseInt(car(x).val) > Integer.parseInt(car(cdr(x)).val);
					t.val = i ? "NIL" : "T";
					return t;
				}
				else {
					System.out.println("LESS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			
			if(eq(f, "GREATER")) {
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					Boolean i = Integer.parseInt(car(x).val) < Integer.parseInt(car(cdr(x)).val);
					t.val = i ? "NIL" : "T";
					return t;
				}
				else {
					System.out.println("GREATER must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			
			else {
				System.out.println(f.val + " is not defuned");
				System.exit(1);
				return null;
			}
		}
		else {
			System.out.println("f is not an atom");
			System.exit(1);
			return null;
		}

	}
	Boolean atom(Sexp sexp) {
		return sexp.left == null && sexp.right == null;
	}
	Boolean nil(Sexp sexp) {
		if(sexp.val == null) return false;
		return sexp.val.equals("NIL");
	}
	Boolean inte(Sexp sexp) {
		String str = sexp.val;
		if(str.charAt(0) != '-' && (str.charAt(0) < '0' || str.charAt(0) >= '9')) return false;
		for(int i = 1; i < str.length(); i++) {
			if(str.charAt(i) > '9' && str.charAt(i) < '0') return false;

		}
		return true;
	}
	Sexp car(Sexp sexp) {
		if(atom(sexp)) {
			System.out.println(sexp.val + " is not a list");
			System.exit(1);
		}
		return sexp.left;
	}
	Sexp cdr(Sexp sexp) {
		if(atom(sexp)) {
			System.out.println(sexp.val + " is not a list");
			System.exit(1);
		}
		return sexp.right;
	}
	Boolean eq(Sexp x, String op) {
		return x.val.equals(op);
	}
	Boolean eq(Sexp x1, Sexp x2) { //
		if(atom(x1) && atom(x2)) {
			return x1.val.equals(x2.val);
		}
		if(atom(x1) || atom(x2)) {
			return false;
		}
		else {
			return eq(car(x1), car(x2)) && eq(cdr(x1), cdr(x2));
		}
	}
	Sexp cons(Sexp sexp1, Sexp sexp2) {
		Sexp sexp = new Sexp();
		sexp.left = sexp1;
		sexp.right = sexp2;
		return sexp;
	}
	Sexp evlist(Sexp x) {
		if(atom(x)) {
			return x; //
		}
		return cons(eval(car(x)), evlist(cdr(x)));
	}
	Sexp evcon(Sexp x) {
		if(nil(x)) {
			System.out.println("COND's para cannot be empty!");
			System.exit(1);
			return null;
		}
		if(eval(car(car(x))).val.equals("T")) {
			return eval(car(cdr(car(x))));
		}
		else return evcon(cdr(x));
	}
}