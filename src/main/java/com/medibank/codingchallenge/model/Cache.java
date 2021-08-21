package com.medibank.codingchallenge.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cache {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Date dateCreated;
	private String title;
	private String url;
	private String author;
	private String imageUrl;
	
	Cache() {}
	
	public Cache(Date dateCreated,
			String title, String url, String author, String imageUrl) {
		this.dateCreated = dateCreated;
		this.title = title;
		this.imageUrl = imageUrl;
		this.url = url;
		this.author = author;
	}
	
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Override
	public String toString() {
		return "Cache{" +
				"dateCreated=" + dateCreated.toString() +
		        "title=" + title +
		        ", url='" + url + '\'' +
		        ", author='" + author + '\'' +
		        '}';
	}

}
