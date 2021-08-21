package com.medibank.codingchallenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medibank.codingchallenge.model.Response;
import com.medibank.codingchallenge.service.ArticleNotFoundException;
import com.medibank.codingchallenge.service.ArticleService;
import com.medibank.codingchallenge.service.ArticleServiceException;


@RestController
public class ArticleController {
	
	private final ArticleService articleService;
	private final Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}
	@GetMapping("/api/article")
	public Response fetchArticle(@RequestParam(required=false) String query) throws ArticleNotFoundException, ArticleServiceException{
		logger.info("---- Starting the fetch article details request -----");
		try {
			Response response = articleService.fetchArticle(query);
			if(response == null) {
				logger.error("No article details received for the query");
				throw new ArticleNotFoundException("article queried for not found");
			}
			logger.debug("Response rereived successfully for the fetch article request and response is ".concat(response.toString()));	
			return response;
		} catch (ArticleServiceException | ArticleNotFoundException e) {
			logger.error("Service threw a exception for the fetch article detaisl request");
			throw e;
		} finally {
			logger.info("---- Finishing the fetch article details request -----");
		}
	}
}

