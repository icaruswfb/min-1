package br.com.min.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Historico implements Serializable{

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Pessoa cliente;
	@ManyToOne
	private Pessoa funcionario;
	@ManyToOne
	private Pessoa criador;
	private String texto;
	private Date data;
	private String textoPequeno;
	@ManyToOne
	private Produto produto;
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getTextoPequeno() {
		return textoPequeno;
	}
	public void setTextoPequeno(String textoPequeno) {
		this.textoPequeno = textoPequeno;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Pessoa getCliente() {
		return cliente;
	}
	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}
	public Pessoa getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}
	public Pessoa getCriador() {
		return criador;
	}
	public void setCriador(Pessoa criador) {
		this.criador = criador;
	}
	
}
