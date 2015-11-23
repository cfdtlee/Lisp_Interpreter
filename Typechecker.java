import java.io.*;
import java.util.*;

// T, NIL, 
// ATOM, EQ,
// NULL, INT
// PLUS
// LESS, 
// CONS, CAR, CDR, COND, 

class Typechecker {
	Sexp check(Sexp sexp) {
		if(atom(sexp)) {
			if (sexp.val.equals("T") || sexp.val.equals("F"))
				sexp.kind = "Bool";
			else if (sexp.val.equals("NIL"))
				sexp.kind = "List";
			else if (inte(sexp))
				sexp.kind = "Nat";
			else {
				System.out.println("ERROR: UNBOUNDED LITERAL:" + sexp.val);
				System.exit(1);
				return null;
			}
			return sexp;
		}
		else {
			if (sexp.left.val.equals("COND")) {
				return evcon(cdr(sexp));
			}
			return apply(car(sexp), chclist(cdr(sexp))); // a, d
		}
	}
	Sexp apply(Sexp f, Sexp x) {
		// System.out.println("dlist in apply is:");
		// Printer pt1 = new Printer();
		// pt1.launch(d);
		if (atom(f)) {
			// atom
			// System.out.println("atom" + f.val);
			if(eq(f, "CAR")) {
				if(car(x).kind != "List") { //// !!!!
					System.out.println("ERROR: CAR must have a list parameter");
					System.exit(1);
					return null;
				}
				car(car(x)).kind = "Nat"; //!!!!
				return car(car(x));
			}
			if(eq(f, "CDR")) {
				if(car(x).kind != "List") { // !!!
					System.out.println("ERROR: CDR must have a list parameter");
					System.exit(1);
					return null;
				}
				cdr(car(x)).kind = "List";
				return cdr(car(x));
			}
			if(eq(f, "CONS")) {
				if(car(x).kind != "Nat") {
					System.out.println("ERROR: CONS' first parameter must be Nat");
					System.exit(1);
					return null;
				}
				if(car(cdr(x)).kind != "List") { // s2 is not List
					System.out.println("ERROR: CONS' second parameter must be List");
					System.exit(1);
					return null;
				}
				Sexp t = cons(car(x), car(cdr(x)));
				t.kind = "List";
				return t;
			}
			if(eq(f, "EQ")) {
				Sexp t = new Sexp();
				t.kind = "Bool";
				if(car(x).kind != "Nat") {
					System.out.println("ERROR: EQ' first parameter must be Nat");
					System.exit(1);
					return null;
				}
				if(car(cdr(x)).kind != "Nat") { // s2 is not List
					System.out.println("ERROR: EQ' second parameter must be Nat");
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
				Sexp t = new Sexp();
				t.kind = "Bool";
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
				if(check(x).kind != "List") { //!!!!!
					System.out.println("ERROR: NULL's parameter must be List");
					System.exit(1);
					return null;
				}
				Sexp t = new Sexp();
				t.kind = "Bool";
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
				t.kind = "Bool";
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
				t.kind = "Nat";
				if(car(x).kind != "Nat") {
					System.out.println("ERROR: PLUS' first parameter must be Nat");
					System.exit(1);
					return null;
				}
				if(car(cdr(x)).kind != "Nat") { // s2 is not Nat
					System.out.println("ERROR: PLUS' second parameter must be Nat");
					System.exit(1);
					return null;
				}
				int i = Integer.parseInt(car(x).val) + Integer.parseInt(car(cdr(x)).val);
				t.val = Integer.toString(i);
				return t;
			}
			if(eq(f, "LESS")) {
				Sexp t = new Sexp();
				t.kind = "Bool";
				if(car(x).kind != "Nat") {
					System.out.println("ERROR: LESS' first parameter must be Nat");
					System.exit(1);
					return null;
				}
				if(car(cdr(x)).kind != "Nat") { // s2 is not List
					System.out.println("ERROR: LESS' second parameter must be Nat");
					System.exit(1);
					return null;
				}
				Boolean i = Integer.parseInt(car(x).val) < Integer.parseInt(car(cdr(x)).val);
				t.val = i ? "T" : "NIL";
				return t;
			}
			
			
			else {
				System.out.println("ERROR: INVALID input");
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
	Sexp chclist(Sexp x) {
		if(atom(x)) {
			if (x.val.equals("T") || x.val.equals("F"))
				x.kind = "Bool";
			else if (x.val.equals("NIL"))
				x.kind = "List";
			else if (inte(x))
				x.kind = "Nat";
			else {
				System.out.println("ERROR: UNBOUNDED LITERAL:" + x.val);
				System.exit(1);
				return null;
			}
			return x; //
		}
		x.kind = "List";
		return cons(check(car(x)), chclist(cdr(x)));
	}
	Sexp evcon(Sexp x) {
		if(nil(x)) {
			System.out.println("ERROR: COND's para cannot be empty!");
			System.exit(1);
			return null;
		}
		Sexp t = x;
		Sexp cur = car(x);
		if(cur.left.val != "T" || cur.left.val != "F") {
			System.out.println("ERROR: COND's CONDITION must be a BOOLEAN value!");
			System.exit(1);
			return null;
		}
		String kind = check(cur.right.left).kind;
		t = cdr(t);
		while (t.val != "NIL") {
			cur = car(t);
			if(cur.left.val != "T" || cur.left.val != "F") {
				System.out.println("ERROR: COND's CONDITION must be a BOOLEAN value!");
				System.exit(1);
				return null;
			}
			if(check(cur.right.left).kind != kind) {
				System.out.println("ERROR: COND's parameters are not consistant");
				System.exit(1);
				return null;				
			}
		}
		x.kind = kind;
		return x;
	}
}