// T, NIL, 
// CAR, CDR, CONS, 
// ATOM, EQ, NULL, INT, 
// PLUS, MINUS, TIMES, QUOTIENT, REMAINDER, 
// LESS, GREATER, COND, QUOTE
class Evaluator {
	Sexp eval(Sexp sexp) {
		if (atom(sexp)) {
			// atom
			return sexp;
		}
		else {
			if (sexp.left.val == "QUOTE") {
				return car(cdr(sexp));
			}
			if (sexp.left.val == "COND") {
				return evcon(cdr(sexp));
			}
			return apply(car(sexp), evlist(cdr(sexp))); // a, b
		}
	}
	Sexp apply(Sexp f, Sexp x) {
		if (atom(f)) {
			// atom
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
				return eq(car(x), car(cdr(x)));
			}
		}
		else {
			System.out.println("f is not an atom");
			System.exit(1);
		}
	}
	Boolean atom(Sexp sexp) {
		return sexp.left == null && sexp.right == null;
	}
	Boolean null(Sexp sexp) {
		return sexp.val == "NULL";
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
	Boolean eq(Sexp sexp, String op) {
		return sexp.val == op;
	}
	Sexp cons(Sexp sexp1, Sexp sexp2) {
		Sexp sexp = new Sexp();
		sexp.left = sexp1;
		sexp.right = sexp2;
		return sexp;
	}
	Sexp evlist(Sexp x) {
		if(null(x)) {
			return x; //
		}
		return cons(eval(car(x)), evlist(cdr(x)));
	}
}