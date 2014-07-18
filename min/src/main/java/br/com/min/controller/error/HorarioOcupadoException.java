package br.com.min.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Horário já ocupado")
public class HorarioOcupadoException extends RuntimeException {
	
	public HorarioOcupadoException(String msg){
		super(msg);
	}
	
}
