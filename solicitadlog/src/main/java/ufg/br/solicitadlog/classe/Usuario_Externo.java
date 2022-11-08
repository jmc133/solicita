package ufg.br.solicitadlog.classe;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usuario_externo")
public class Usuario_Externo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String cpf;
	@NotNull
	private String nome;
	private String vinculo;
	private String categoria;
	private String matricula;
	private String situacao;
	private Integer ano_ingresso;
	private String unidade_vinculo;
	private String sigla_unidade;
	private Integer id_unidade;
	private String cidade_vinculo;
	private String senha;
	
	
	public String getSigla_unidade() {
		return sigla_unidade;
	}
	public void setSigla_unidade(String sigla_unidade) {
		this.sigla_unidade = sigla_unidade;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getVinculo() {
		return vinculo;
	}
	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public Integer getAno_ingresso() {
		return ano_ingresso;
	}
	public void setAno_ingresso(Integer ano_ingresso) {
		this.ano_ingresso = ano_ingresso;
	}
	public String getUnidade_vinculo() {
		return unidade_vinculo;
	}
	public void setUnidade_vinculo(String unidade_vinculo) {
		this.unidade_vinculo = unidade_vinculo;
	}

	public Integer getId_unidade() {
		return id_unidade;
	}
	public void setId_unidade(Integer id_unidade) {
		this.id_unidade = id_unidade;
	}
	public String getCidade_vinculo() {
		return cidade_vinculo;
	}
	public void setCidade_vinculo(String cidade_vinculo) {
		this.cidade_vinculo = cidade_vinculo;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	

}
