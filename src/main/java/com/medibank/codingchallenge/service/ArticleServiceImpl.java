package com.medibank.codingchallenge.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.medibank.codingchallenge.model.Cache;
import com.medibank.codingchallenge.model.HackerNewsResponse;
import com.medibank.codingchallenge.model.Hits;
import com.medibank.codingchallenge.model.Response;
import com.medibank.codingchallenge.model.UnsplashImageResults;
import com.medibank.codingchallenge.model.UnsplashResponse;
import com.medibank.codingchallenge.repository.CacheRepository;

/**
 * 
 * Article service class implementation
 *
 */
@Service
public class ArticleServiceImpl implements ArticleService{
	
	private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
	
	// Taken from environment variable
	// Will be induced into the docker container in docker-compose file
	@Value("${UNSPLASH_ACCESS_KEY}")
	private String unsplashAccessKey;
	
	// Taken from application.properties file
	@Value("${hacker.api.url}")
	private String hackerApiUrl;
	
	@Value("${unsplash.api.url}")
	private String unsplashApiUrl;
	
	@Value("${hacker.api.defaultQuery}")
	private String hackerApiDefaultQuery;
	
	@Value("${unsplash.api.defaultQuery}")
	private String unsplashApiDefaultQuery;
	
	private final CacheRepository cacheRepository;
	
	public ArticleServiceImpl(CacheRepository cacheRepository) {
		this.cacheRepository = cacheRepository;
	}
	
	@Override
	public Response fetchArticle(String query) throws ArticleServiceException, ArticleNotFoundException {
		logger.info("---- Starting the fetch article service implementation----");
		try {
			List<Cache> cacheData = cacheRepository.findAll();
			Response response = new Response();
			if(cacheData != null && cacheData.size()> 0 && !exceededOneHour(cacheData.get(0).getDateCreated())) {
				logger.info("fetching details from cache");
				Cache cache = cacheData.get(0);
				response.setAuthor(cache.getAuthor());
				response.setTitle(cache.getTitle());
				response.setUrl(cache.getUrl());
				response.setImageUrl(cache.getImageUrl());
				return response;
			} else {
				logger.info("fetching details from api calls and updating cache");
				cacheRepository.deleteAll();
				Hits randomHit = fetchHackerNewsArticle(query);
				String imageUrl = fetchUnsplashImageUrl(query);
				response.setAuthor(randomHit.getAuthor());
				response.setTitle(randomHit.getTitle());
				response.setUrl(randomHit.getUrl());
				response.setImageUrl(imageUrl);
				Cache cache = new Cache(new Date(), randomHit.getTitle(), randomHit.getUrl(),
						randomHit.getAuthor(), imageUrl);
				cacheRepository.save(cache);
				return response;
			}
		} catch (ArticleServiceException| ArticleNotFoundException e) {
			logger.error("Article Exception occured in fetch article servie impl".concat(e.getMessage()));
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured in fetch article servie impl", e);
			throw new ArticleServiceException("Intenral server error occured");
		} finally {
			logger.info("---- Finishing the fetch article service implementation ----");
		}
	}
	
	/**
	 * Fetches response from hacker news api
	 * @param query - query to be searched upon (takes query from application properties by default)
	 * @return hits response from hacker news API
	 * @throws ArticleServiceException
	 * @throws ArticleNotFoundException
	 */
	private Hits fetchHackerNewsArticle(String query) throws ArticleServiceException, ArticleNotFoundException{
		logger.info("---- Starting to fetch from hacker news api -----");
		try {
			URI url = new URI(hackerApiUrl.concat("?tags=story&query=").concat( ObjectUtils.isEmpty(query) ? hackerApiDefaultQuery : query));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<HackerNewsResponse> hackerNewsResponse = restTemplate.getForEntity(url, HackerNewsResponse.class);
			logger.debug("Status code received from the hacker news api".concat(String.valueOf(hackerNewsResponse.getStatusCodeValue())));
			if(hackerNewsResponse.getStatusCode().is2xxSuccessful()) {
				HackerNewsResponse hacker = hackerNewsResponse.getBody();
				ArrayList<Hits> hits = hacker.getHits();
				return hits.get(getRandomNumber(0, hits.size()-1));
			} else if(hackerNewsResponse.getStatusCode() == HttpStatus.NOT_FOUND || hackerNewsResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
				throw new ArticleNotFoundException("No article found for given search criteria");
			} else {
				throw new ArticleServiceException("Error while consuming hacker article news service");
			}
			
		} catch (RestClientException e) {
			logger.error("Error occurred while hitting hacker news api ", e);
			throw new ArticleServiceException("Error while consuming hacker article news service");
		} catch (Exception e) {
			logger.error("Exception occured in fetchHackerNewsArticle method ", e);
			throw new ArticleServiceException("Internal server occured when consuming hacker article news service");
		} finally {
			logger.info("---- Finsihed fethcing from hacker news api -----");
		}
	}
	
	/**
	 * Fetches image url from unsplash image api
	 * @param query
	 * @return image url
	 * @throws ArticleServiceException
	 * @throws ArticleNotFoundException
	 */
	private String fetchUnsplashImageUrl(String query) throws ArticleServiceException, ArticleNotFoundException {
		logger.info("---- Starting to fetch from unsplash image api -----");
		try {
			URI url = new URI(unsplashApiUrl.concat("?page=1&query=").concat(ObjectUtils.isEmpty(query) ? unsplashApiDefaultQuery : query ).concat("&client_id=").concat(unsplashAccessKey));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<UnsplashResponse> unsplResponseEntity = restTemplate.getForEntity(url, UnsplashResponse.class);
			logger.debug("Status code received from the hacker news api".concat(String.valueOf(unsplResponseEntity.getStatusCodeValue())));
			if(unsplResponseEntity.getStatusCode().is2xxSuccessful()) {
				UnsplashResponse unsplashResponse = unsplResponseEntity.getBody();
				ArrayList<UnsplashImageResults> results = unsplashResponse.getResults();
				return results.get(getRandomNumber(0, results.size()-1)).getUrls().getSmall();
			} else if(unsplResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND || unsplResponseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
				throw new ArticleNotFoundException("No image found for given search criteria in unsplash");
			} else {
				throw new ArticleServiceException("Error while consuming unsplash image service");
			}
			
		}  catch (RestClientException e) {
			logger.error("Error occurred while hitting unsplash image api ", e);
			throw new ArticleServiceException("Error while consuming unsplash image service");
		} catch (Exception e) {
			logger.error("Exception occured in fetchUnsplashImageUrl method ", e);
			throw new ArticleServiceException("Internal server occured when consuming unsplash image service");
		} finally {
			logger.info("---- Finsihed fethcing from unsplash image api -----");
		}
	}
	
	/** 
	 * @param start
	 * @param end
	 * @return a random number between given start and end numbers
	 */
	private Integer getRandomNumber(Integer start, Integer end) {
		Random r = new Random();
		return r.nextInt(end-start) + start;
	}
	
	/**
	 * @param date
	 * @return if the given date exceeds 1 hour for the current date
	 */
	private Boolean exceededOneHour(Date date) {
		Date currentDate =new Date();
		Long timeDiff = currentDate.getTime() - date.getTime();
		return TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS) > 1;
	}
}
