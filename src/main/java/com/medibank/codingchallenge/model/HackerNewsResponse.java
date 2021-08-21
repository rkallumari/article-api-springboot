package com.medibank.codingchallenge.model;

import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HackerNewsResponse {
	private ArrayList<Hits> hits;

	public ArrayList<Hits> getHits() {
		return hits;
	}

	public void setHits(ArrayList<Hits> hits) {
		this.hits = hits;
	}
	
	@Override
	public String toString() {
		return "HackerNewsResponse{" +
		        "hits=" + hits.toString() +
		        '}';
	}
}
