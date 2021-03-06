package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {
	
	private static BigInteger reverse(BigInteger input) { //got help from someone else on this method
		DigitNode editor = input.front;
		DigitNode next = null; 
		DigitNode prev = null;
		while (editor != null) {
			next = editor.next;
			editor.next = prev;
			prev = editor;
			editor = next;
		}
		input.front = prev;
		return input;
	}
		/*BigInteger returnThis = new BigInteger(); 		      <----  what I was trying to do, before I got help
		returnThis.negative = input.negative;
		BigInteger copy = input;
		
		while (returnThis.numDigits != input.numDigits) {
			for (int counter = 0; counter<copy.numDigits; counter++) {
				if (counter == copy.numDigits-returnThis.numDigits) {
					returnThis.front = new DigitNode(copy.front.digit,null);
					System.out.println(copy.front);
					returnThis.front = returnThis.front.next;
					returnThis.numDigits += 1;
				}
				copy.front = copy.front.next;
			}
		}
		return returnThis;
	}/*

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		try {
			Integer.parseInt(integer);
		}
		catch (IllegalArgumentException e) {
			System.out.println("please input something valid");
		}
		
		//Variables
		BigInteger result = new BigInteger(); //return this
		int trueStart = -1; //where does integer actually start?
		int trueEnd = -1; //where does it actually end?
		boolean alert = false; //is there a space between numbers or something like that?
		String newInteger = ""; //we're gonna use this to make the actual ll
		
		//finds the information that will be input into newInteger
		for (int check = 0; check < integer.length(); check++) {
			if (integer.substring(check, check+1).equals("-") || integer.substring(check,check+1).equals("1") || integer.substring(check,check+1).equals("2") || integer.substring(check,check+1).equals("3") || integer.substring(check,check+1).equals("4") || integer.substring(check,check+1).equals("5") || integer.substring(check,check+1).equals("6") || integer.substring(check,check+1).equals("7") || integer.substring(check,check+1).equals("8") || integer.substring(check,check+1).equals("9") || integer.substring(check,check+1).equals("0")) { //Behold, the least graceless bit of spaghetti code
				if (alert == true) {
					throw new IllegalArgumentException("must not contain spaces between numbers");
				}
				if (integer.substring(check,check+1).equals("-")) {
						if (trueStart == -1) {
							result.negative = true;
						}
					}
					else {
						if (trueStart == -1) {
							trueStart = check;
							if(integer.substring(check,check+1).equals("0")) {
								trueStart = -1;
							}
						}
					}
				}
				if (trueStart != -1) {
					trueEnd = check+1;
				}
			if (integer.substring(check,check+1).equals(" ")) {
				alert = true;
			}
		}
	newInteger = integer.substring(trueStart,trueEnd);
	result.numDigits = trueEnd;
	result.front = new DigitNode(Integer.parseInt(newInteger.substring(0,1)),null);
	//reverses the values and puts them in the ll
	DigitNode nodeAddition = result.front;
	for(int counter = 1; counter<newInteger.length(); counter++) {
		nodeAddition.next = new DigitNode(Integer.parseInt(newInteger.substring(counter,counter+1)), null);
		nodeAddition = nodeAddition.next;
    }
	return reverse(result);
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		int holdOver = 0; //value used for carrying overflow in a digit's place, eg 9+6=5 and holdover=1
		BigInteger solution = new BigInteger(); //the solution to be returned
		boolean subtraction = false; //possible to do subtraction if need be
		BigInteger over = null;		//in the event we go into subtraction
		BigInteger under = null;
		DigitNode one = first.front;	
		DigitNode two = second.front;
		solution.front = new DigitNode(0,null);
		DigitNode solNode = solution.front; //off topic, but it hit me way late that i needed a digitnode for the solution, haha.
		boolean start = true; //small bit of code that does some tracking so i can time the creation of new nodes properly
		
		if (first == null || (first.front.digit == 0 && first.numDigits==1)) { //get the easy possibilities out of the way first
			return second;
		}
		else if (second == null || (first.front.digit == 0 && first.numDigits==1)) {
			return first;
		}
		
		if (first.negative == true || second.negative == true) { //preparations for subtraction
			subtraction = true;
			if (first.negative == true && second.negative == true) {
				subtraction = false;
			}
			if (first.negative == true) {
				over = second;
				under = first;
			}
			else {
				over = first;
				under = second;
			}
		}
		if (subtraction == false) { //the big split
			do {
				if (start == false) {
					solNode.next = new DigitNode(0,null);
					solNode = solNode.next;
				}
				start = false;
				int digitMule = (one.digit + two.digit) + holdOver; // can't believe i didn't think of this sooner
				if ((one.digit + two.digit) + holdOver >= 10) {
					solNode.digit = ((one.digit+two.digit)+holdOver)%10;
					if (digitMule > 10) {
						holdOver = 1;
					}
				}
				else if (digitMule<10) {
					solNode.digit = digitMule;
					holdOver = 0;
				}
				one = one.next;
				two = two.next;		
			} while (one != null && two != null); //note to self, && is not a typo; reminder that the numbers don't necessarily have the same number of digits
			if (holdOver == 1) {
				solNode.next = new DigitNode(0,null);
				solNode = solNode.next;
				solNode.digit += 1;
			}
			if (first.negative == true) {
				solution.negative = true;
			}
		}
	
		//testing if i have to code everything all over again, or if i can get away with ctrl c ctrl v
		if (subtraction == true) { //the big split 
			boolean persCheck = false; //persistent negative check
			one = over.front;
			two = under.front;
			System.out.println(one.digit + "," + two.digit);
			System.out.println(one.digit - two.digit);
			do {
				if (start == false) {
					solNode.next = new DigitNode(0,null);
					solNode = solNode.next;
				}
				start = false;
				int digitMule = (one.digit - two.digit) - holdOver; // can't believe i didn't think of this sooner
				if (digitMule< 0) {
					solNode.digit = (one.digit - two.digit)- holdOver;
					if (digitMule < 0) {
						holdOver = 1;
					}
				}
				else if (digitMule>0) {
					solNode.digit = digitMule;
					holdOver = 0;
				}
				if (one.digit < two.digit) {
					persCheck = true;
				}
				if (one.digit > two.digit) {
					persCheck = false;
				}
				one = one.next;
				two = two.next;		
				
			} while (one != null && two != null); //note to self, && is not a typo; reminder that the numbers don't necessarily have the same number of digits
				solution.negative = persCheck;
				subtraction = false;
		}
		
		
		
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		return solution;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		DigitNode one = first.front;
		DigitNode two = second.front;
		BigInteger solution = new BigInteger();
		DigitNode solNode = solution.front;
		DigitNode overflow = new DigitNode(0,null);
		if (first.negative == true && second.negative != true) {
			solution.negative = true;
		}
		else if (first.negative != true && second.negative == true) {
			solution.negative = true;
		}
		for (int counter1 = 0; counter1<first.numDigits;counter1++) {
			for (int counter2 = 0; counter2<second.numDigits;counter2++) {
				
			}
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}