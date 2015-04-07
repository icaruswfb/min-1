package br.com.min.entity;

import java.io.Serializable;

public class Link implements Serializable{
	
	public static final String TYPE_POST = "POST";
	public static final String TYPE_GET = "GET";
	/**
	 * 
	 */
	private static final long serialVersionUID = 7788561645144322458L;

	public Link(){
		
	}
	
	public Link(String href, String rel, String type) {
		super();
		this.href = href;
		this.rel = rel;
		this.type = type;
	}
	private String href;
	private String rel;
	private String type;
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
