package br.com.min.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class Pessoa implements Serializable{
	
	@Transient
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
	
	private Long id;
	private String nome;
	private String telefone;
	private Date aniversario;
	private String email;
	private String endereco;
	private String cep;
	private String cidade;
	private Funcao funcaoPrincipal;
	private Boolean funcionario;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Date getAniversario() {
		return aniversario;
	}
	public void setAniversario(Date aniversario) {
		this.aniversario = aniversario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	@Transient
	public String getAniversarioStr(){
		if(aniversario == null){
			return null;
		}
		return sdf.format(aniversario);
	}
	public void setAniversarioStr(String data){
		try {
			aniversario = sdf.parse(data);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	@Enumerated(EnumType.STRING)
	public Funcao getFuncaoPrincipal() {
		return funcaoPrincipal;
	}
	public void setFuncaoPrincipal(Funcao funcaoPrincipal) {
		this.funcaoPrincipal = funcaoPrincipal;
	}
	public Boolean getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}
	
}