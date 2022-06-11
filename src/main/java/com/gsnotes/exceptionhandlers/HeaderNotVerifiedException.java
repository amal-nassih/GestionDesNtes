package com.gsnotes.exceptionhandlers;

public class HeaderNotVerifiedException extends RuntimeException {

	public HeaderNotVerifiedException() {
		super();
	}
	
	public HeaderNotVerifiedException(String message) {
		super(message);
	}
}
