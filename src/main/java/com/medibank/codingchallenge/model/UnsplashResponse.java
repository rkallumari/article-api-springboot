package com.medibank.codingchallenge.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnsplashResponse {
	private ArrayList<UnsplashImageResults> results;

	public ArrayList<UnsplashImageResults> getResults() {
		return results;
	}

	public void setResults(ArrayList<UnsplashImageResults> unsplashImageResults) {
		this.results = unsplashImageResults;
	}
	
	@Override
	public String toString() {
		return "UnsplashResponse{" +
		        "hits=" + results.toString() +
		        '}';
	}
}
