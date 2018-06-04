package com.matthew.launch;

public class Interest {

	private Long id;
	private String title;
	
	Interest(){}
	
	Interest(Long id, String title) {
		this.setId(id);
		this.setTitle(title);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString() {
		return id + " " + title;
	}
}
