import java.io.*;
import java.util.*;
// T, NIL, 
//   ATOM, EQ,
//   NULL, INT, QUOTE
// PLUS, MINUS, TIMES, QUOTIENT, REMAINDER, 
// LESS, GREATER, 
// CONS, CAR, CDR, COND, 
class Evaluator {
	Sexp eval(Sexp sexp, Sexp a, Sexp d) {
		if (atom(sexp)) {
			// atom
			if (sexp.val.equals("T") || sexp.val.equals("NIL") || inte(sexp))
				return sexp;
			else if (bound(exp, a))
				return getval(exp, a);
			else {
				System.out.println("ERROR: UNBOUNDED LITERAL");
				exit(1);
				return null;
			}
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
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: EQ can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: EQ's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				if(eq(car(x), car(cdr(x)))) {
					// System.out.println("EQ");
					t.val = "T";
					return t;
				}
				else {
					t.val = "NIL";
					return t;
				}
			}
			if(eq(f, "ATOM")) {
				if(cdr(x).val == null) {
					System.out.println("ERROR: ATOM can only have 1 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).val.equals("NIL")) {
					System.out.println("ERROR: ATOM's para must be a list contains 1 element");
					System.exit(1);
					return null;
				}
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
				if(cdr(x).val == null) {
					System.out.println("ERROR: ATOM can only have 1 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).val.equals("NIL")) {
					System.out.println("ERROR: NULL's para must be a list contains 1 element");
					System.exit(1);
					return null;
				}
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
				if(cdr(x).val == null) {
					System.out.println("ERROR: ATOM can only have 1 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).val.equals("NIL")) {
					System.out.println("ERROR: INT's para must be a list contains 1 element");
					System.exit(1);
					return null;
				}
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
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: PLUS can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: PLUS's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) + Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("ERROR: PLUS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "MINUS")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: MINUS can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: MINUS's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) - Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("ERROR: MINUS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "TIMES")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: TIMES can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: TIMES's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) * Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("ERROR: TIMES must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "QUOTIENT")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: QUOTIENT can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: QUOTIENT's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x))) && !car(cdr(x)).val.equals("0")) {
					int i = Integer.parseInt(car(x).val) / Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else if(!car(cdr(x)).val.equals("0")){
					System.out.println("ERROR: QUOTIENT must has two INT opts");
					System.exit(1);
					return null;
				}
				else {
					System.out.println("ERROR: TRY to divide by 0");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "REMAINDER")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: REMAINDER can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: REMAINDER's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					int i = Integer.parseInt(car(x).val) % Integer.parseInt(car(cdr(x)).val);
					t.val = Integer.toString(i);
					return t;
				}
				else {
					System.out.println("ERROR: REMAINDER must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			if(eq(f, "LESS")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: LESS can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: LESS's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					Boolean i = Integer.parseInt(car(x).val) < Integer.parseInt(car(cdr(x)).val);
					t.val = i ? "T" : "NIL";
					return t;
				}
				else {
					System.out.println("ERROR: LESS must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			
			if(eq(f, "GREATER")) {
				if(cdr(x).right.val == null) {
					System.out.println("ERROR: GREATER can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) {
					System.out.println("ERROR: GREATER's para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				if(inte(car(x)) && inte(car(cdr(x)))) {
					Boolean i = Integer.parseInt(car(x).val) > Integer.parseInt(car(cdr(x)).val);
					t.val = i ? "T" : "NIL";
					return t;
				}
				else {
					System.out.println("ERROR: GREATER must has two INT opts");
					System.exit(1);
					return null;
				}
			}
			
			else {
				System.out.println("ERROR: " + f.val + " is not defuned");
				System.exit(1);
				return null;
			}
		}
		else {
			System.out.println("ERROR: f is not an atom");
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
		if(str == null) return false;
		if(str.charAt(0) != '-' && (str.charAt(0) < '0' || str.charAt(0) > '9')) return false;
		for(int i = 1; i < str.length(); i++) {
			if(str.charAt(i) > '9' && str.charAt(i) < '0') return false;
		}
		return true;
	}
	Sexp car(Sexp sexp) {
		if(atom(sexp)) {
			System.out.println("ERROR: " + sexp.val + " is not a list");
			System.exit(1);
		}
		return sexp.left;
	}
	Sexp cdr(Sexp sexp) {
		if(atom(sexp)) {
			System.out.println("ERROR: " + sexp.val + " is not a list");
			System.exit(1);
		}
		return sexp.right;
	}
	Boolean eq(Sexp x, String op) {
		return x.val.equals(op);
	}
	Boolean eq(Sexp x1, Sexp x2) { //
		if(atom(x1) && atom(x2)) {
			// System.out.println("&&");
			return x1.val.equals(x2.val);
		}
		if(atom(x1) || atom(x2)) {
			// System.out.println("||");
			return false;
		}
		else {
			System.out.println("not yet");
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
			System.out.println("ERROR: COND's para cannot be empty!");
			System.exit(1);
			return null;
		}
		if(eval(car(car(x))).val.equals("T")) {
			return eval(car(cdr(car(x))));
		}
		else return evcon(cdr(x));
	}
	Boolean bound(Sexp exp, Sexp z) {
		if(nil(car(z))) return false;
		return exp.val.equals(car(car(z)).val) || bound(cdr(exp), z);
	}
	Sexp getval(Sexp exp, Sexp z) {
		if(exp.val.equals(car(car(z)).val)) return cdr(car(z));
		else return getval(exp, cdr(z));
	}
}