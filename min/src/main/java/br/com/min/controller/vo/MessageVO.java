package br.com.min.controller.vo;

import java.io.Serializable;

public class MessageVO implements Serializable {

	public static final String ERROR = "danger";
	public static final String WARNING = "warning";
	public static final String INFO = "info";
	public static final String SUCCESS = "success";
	
	private String severity;
	private String message;
	
	public MessageVO(){
		
	}
	
	public MessageVO(String severity, String message) {
		super();
		this.severity = severity;
		this.message = message;
	}
	
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
