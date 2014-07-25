package br.com.min.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VerificacaoVO implements Serializable{

	private boolean success;
	private boolean criticalError;
	private List<MessageVO> messages = new ArrayList<>();
	
	public void addMessage(String severity, String message){
		messages.add(new MessageVO(severity, message));
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<MessageVO> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageVO> messages) {
		this.messages = messages;
	}

	public boolean isCriticalError() {
		return criticalError;
	}

	public void setCriticalError(boolean criticalError) {
		this.criticalError = criticalError;
	}
	
}
