package com.arcana.passport.stored.procedure;

import org.apache.commons.ssl.asn1.Strings;

import sun.security.util.Length;

public class TestClass {

	@SuppressWarnings("null")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//System.out.println("+00000003100.00".split("\\+")[1]);
		
		
		int number = 1500;

	       // String format below will add leading zeros (the %0 syntax) 
	       // to the number above. 
	       // The length of the formatted string will be 7 characters.

	       String formatted = String.format("%010d", number);

	      // System.out.println("Number with leading zeros: " + formatted);
		//System.out.println(fixedLengthString("hassan", 45).length());
		//System.out.println(new StringBuilder(fixedLengthString("hassan", 45)).reverse().toString().length());
System.out.println(paddingLeft("2", 8));

	}
	public static String paddingLeft(String string, int length) {
	    return String.format("%1$" + length + "s", string);
	}
	
	public static String fixedLengthString(String string, int length) {
	    return String.format("%1$-" + length + "s", string);
	}
}
