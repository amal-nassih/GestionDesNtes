package com.gsnotes.exceptionhandlers;

public class NoteIncorrectException extends RuntimeException {

	public NoteIncorrectException() {
		super();
	}
	
	public NoteIncorrectException(String message) {
		super(message);
	}
}
