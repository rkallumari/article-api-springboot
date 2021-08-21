package com.medibank.codingchallenge.service;

import com.medibank.codingchallenge.model.Response;

public interface ArticleService {
	public Response fetchArticle(String query) throws ArticleServiceException, ArticleNotFoundException;
}
