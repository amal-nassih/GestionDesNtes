package com.gsnotes.exceptionhandlers;

public class FormatException extends RuntimeException {

	public FormatException() {
		super();
	}
	
	public FormatException(String message) {
		super(message);
	}
}
