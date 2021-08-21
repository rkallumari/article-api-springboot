package com.medibank.codingchallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnsplashImageResults {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class ImageType {
		private String small;

		public String getSmall() {
			return small;
		}

		public void setSmall(String small) {
			this.small = small;
		}
		
		@Override
		public String toString() {
			return "ImageType{" +
			        "small=" + small +
			        '}';
		}
		
	}
	private ImageType urls;
	

	public ImageType getUrls() {
		return urls;
	}

	public void setUrls(ImageType urls) {
		this.urls = urls;
	}
	
	@Override
	public String toString() {
		return "UnsplashImageResults{" +
		        "urls=" + urls.toString() +
		        '}';
	}
}
