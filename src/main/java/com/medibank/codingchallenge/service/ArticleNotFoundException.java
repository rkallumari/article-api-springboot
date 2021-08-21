package com.medibank.codingchallenge.service;

public class ArticleNotFoundException extends Exception{
	private static final long serialVersionUID = -9079454849611061074L;

	public ArticleNotFoundException() {
		super();
	}

	public ArticleNotFoundException(final String message) {
		super(message);
	}
}
