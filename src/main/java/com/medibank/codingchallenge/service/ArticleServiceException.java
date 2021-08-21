package com.medibank.codingchallenge.service;

public class ArticleServiceException extends Exception{
	private static final long serialVersionUID = -470180507998010368L;

	public ArticleServiceException() {
		super();
	}

	public ArticleServiceException(final String message) {
		super(message);
	}
}
