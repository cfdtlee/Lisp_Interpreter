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
			else if (bound(sexp, a)) {

				return getval(sexp, a);
			}
			else {
				System.out.println("ERROR: UNBOUNDED LITERAL:" + sexp.val);
				System.exit(1);
				return null;
			}
		}
		else {
			if (sexp.left.val.equals("QUOTE")) {
				Sexp x = cdr(sexp);
				if(x == null || cdr(x) == null || cdr(x).val == null) {// less than one para
					System.out.println("ERROR: QUOTE must have 1 para");
					System.exit(1);
					return null;
				}
				return car(cdr(sexp));
			}
			if (sexp.left.val.equals("COND")) {
				return evcon(cdr(sexp), a, d);
			}
			if (sexp.left.val.equals("DEFUN")) {
				Sexp res = new Sexp();
				res.left = new Sexp();
				res.left.left = new Sexp(car(cdr(sexp)));
				res.left.right = new Sexp();
				res.left.right.left = new Sexp(car(cdr(cdr(sexp))));
				res.left.right.right = new Sexp(car(cdr(cdr(cdr(sexp)))));
				res.right = d.right;
				d.right = new Sexp(res);
				// defun(sexp, d);
				// System.out.println("dlist now is:");
				// Printer pt = new Printer();
				// pt.launch(d); //??
				return new Sexp(car(cdr(sexp)));
				// return new Sexp(car(cdr(sexp)));
			}
			// System.out.println("apply "+car(sexp).val+" to ");
			// System.out.println(sexp.right.left.val + " " + sexp.right.right.left.val);
			// System.out.println("dlist in apply is:");
			// Printer pt1 = new Printer();
			// pt1.launch(d);
			// if(cdr(cdr(sexp)).val != "NIL") {
			// 	System.out.println("ERROR: "+car(sexp).val+" has invalid parameters");
			// 	System.exit(1);
			// }
			return apply(car(sexp), evlist(cdr(sexp), a, d), a, d); // a, d
		}
	}
	void defun(Sexp sexp, Sexp d) {
		Sexp t = d.right;
		d.right = sexp.right;
		sexp.right = t;
	}
	Sexp apply(Sexp f, Sexp x, Sexp a, Sexp d) {
		// System.out.println("dlist in apply is:");
		// Printer pt1 = new Printer();
		// pt1.launch(d);
		if (atom(f)) {
			// atom
			// System.out.println("atom" + f.val);
			if(eq(f, "CAR")) {
				if(x == null || cdr(x) == null || cdr(x).val == null) {// less than one para
					System.out.println("ERROR: CAR can only have 1 para");
					System.exit(1);
					return null;
				}

				return car(car(x));
			}
			if(eq(f, "CDR")) {
				if(x == null || cdr(x) == null || cdr(x).val == null) {// less than one para
					System.out.println("ERROR: CDR can only have 1 para");
					System.exit(1);
					return null;
				}
				return cdr(car(x));
			}
			if(eq(f, "CONS")) {
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
					System.out.println("ERROR: CONS can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) { // more than 2 para
					System.out.println("ERROR: CONS' para must be a list contains 2 elements");
					System.exit(1);
					return null;
				}
				return cons(car(x), car(cdr(x)));
			}
			if(eq(f, "EQ")) {
				Sexp t = new Sexp();
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
					System.out.println("ERROR: EQ can only have 2 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).right.val.equals("NIL")) { // more than 2 para
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
				if(x == null || cdr(x) == null || cdr(x).val == null) {// less than one para
					System.out.println("ERROR: ATOM can only have 1 para");
					System.exit(1);
					return null;
				}
				if(!cdr(x).val.equals("NIL")) { // more than one para
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
				if(x == null || cdr(x) == null || cdr(x).val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				if(x == null || cdr(x) == null || cdr(x).right == null || cdr(x).right.val == null) {
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
				// System.out.println("dlist is:");
				// Printer pt = new Printer();
				// pt.launch(d);
				return eval(cdr(getval(f, d)), addpair(car(getval(f, d)), x, a), d);
				// System.out.println("ERROR: " + f.val + " is not defuned");
				// System.exit(1);
				// return null;
			}
		}
		else {
			System.out.println("ERROR: f is not an atom");
			System.exit(1);
			return null;
		}

	}
	Boolean atom(Sexp sexp) {
		if(sexp == null) {
			System.out.println("ERROR: sexp cannot be null");
			System.exit(1);
		}
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
		// if(atom(sexp)) {
		// 	System.out.println("ERROR: " + sexp.val + " is not a list(trying to use car)");
		// 	System.exit(1);
		// }
		return sexp.left;
	}
	Sexp cdr(Sexp sexp) {
		// if(atom(sexp)) {
		// 	System.out.println("ERROR: " + sexp.val + " is not a list(trying to use cdr)");
		// 	System.exit(1);
		// }
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
			// System.out.println("not yet");
			return eq(car(x1), car(x2)) && eq(cdr(x1), cdr(x2));
		}
	}
	Sexp cons(Sexp sexp1, Sexp sexp2) {
		Sexp sexp = new Sexp();
		sexp.left = sexp1;
		sexp.right = sexp2;
		return sexp;
	}
	Sexp evlist(Sexp x, Sexp a, Sexp d) {
		if(atom(x)) {
			return x; //
		}
		return cons(eval(car(x), a, d), evlist(cdr(x), a, d));
	}
	Sexp evcon(Sexp x, Sexp a, Sexp d) {
		if(nil(x)) {
			System.out.println("ERROR: COND's para cannot be empty!");
			System.exit(1);
			return null;
		}
		if(eval(car(car(x)), a, d).val.equals("T")) {
			// if(cdr(cdr(car(x))).val != "NIL") {
			// 	System.out.println("ERROR: COND has invalid parameters");
			// 	System.exit(1);
			// }
			return eval(car(cdr(car(x))), a, d);
		}
		else if(eval(car(car(x)), a, d).val.equals("NIL")) 
			return evcon(cdr(x), a, d);
		else {
			System.out.println("ERROR: COND's CONDITION must be a BOOLEAN value!");
			System.exit(1);
			return null;
		}
	}
	Boolean bound(Sexp exp, Sexp z) {
		if(car(z) == null || nil(car(z))) return false;
		return exp.val.equals(car(car(z)).val) || bound(exp, cdr(z));
	}
	Sexp getval(Sexp exp, Sexp z) { // 
		if(z == null || car(z) == null) {
			System.out.println("ERROR: UNDEFUNED FUCTION: " + exp.val);
			System.exit(1);
		}
		if(exp.val.equals(car(car(z)).val)) {
			// System.out.println("equals" + cdr(car(z)).right.left.left.val);
			return cdr(car(z));
		}
		else return getval(exp, cdr(z));
	}
	Sexp addpair(Sexp formalPara, Sexp x, Sexp a) { //add (formalPara, x) to a, and return a
		if(lengthOf(formalPara) != lengthOf(x)) {
			System.out.println("ERROR: Number of formal parameters is not equal to number of actuall parameters!");
			System.exit(0);
			return null;
		}
		if(lengthOf(formalPara) == 0) return new Sexp(a);
		Sexp res = new Sexp();
		res.left = new Sexp();
		res.left.left = new Sexp(car(formalPara));
		res.left.right = new Sexp(car(x));
		// System.out.println("add" + car(formalPara).val + car(x).val);
		res.right = addpair(cdr(formalPara), cdr(x), a);
		return res;
	}
	int lengthOf(Sexp sexp) {
		if(nil(sexp)) return 0;
		else return 1+lengthOf(sexp.right);
	}
}