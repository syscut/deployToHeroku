package com.syscut.model;

import lombok.Data;

@Data
public class ReadArticle {
	private String date;
	private String title;
	private String content;
	private String tags;
}
