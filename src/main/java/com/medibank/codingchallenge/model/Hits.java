package com.medibank.codingchallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hits {
	private String title;
	private String url;
	private String author;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Override
	public String toString() {
		return "Hits{" +
		        "title=" + title +
		        ", url='" + url + '\'' +
		        ", author='" + author + '\'' +
		        '}';
	}
}
