import java.io.*;
import java.util.*;
class Sexp {
	public Boolean isList;
	// public Atom atom;
	public String kind;
	public String val;
	public Sexp left;
	public Sexp right;
	Sexp() {
		this.isList = true;
		this.kind = null;
		this.val = null;
		this.left = null;
		this.right = null;
	}
	void reset() {
		this.isList = true;
		this.kind = null;
		this.val = null;
		this.left = null;
		this.right = null;
	}
	public static Boolean isListTree(Sexp sexp) {
		if(sexp == null) {
			return true;
		}
		else if(sexp.right == null) {
			return true;
		}
		else if(sexp.right.right == null) {
			if(sexp.right.val.equals("NIL")) {
				// System.out.println("True with" + sexp.right.val);
			}
			else {
				// System.out.println("false!");
				return false;
			}
		}
		return isListTree(sexp.left) && isListTree(sexp.right);
	}
}