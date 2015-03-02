package br.com.min.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Imagem {

	@Id
	@GeneratedValue
	private Long id;
	@Lob()
	@Column(length=16000000)
	@JsonIgnore
	@com.fasterxml.jackson.annotation.JsonIgnore
	private byte[] imagem;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public byte[] getImagem() {
		return imagem;
	}
	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}
	
}
