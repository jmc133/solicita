package ufg.br.solicitadlog.classe;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SolicitacaoFant {
	
	@Id
	private Long id;
	private Date data_solicitacao;
	private Date data_ini;
	private Date data_fim;
	private String aceite;
	private String requisicao;
	private String especie;
	private String destino;
	private String motivo;
    private String solicitante;
    private String nome_unidade;
    private String observacao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getData_solicitacao() {
		return data_solicitacao;
	}
	public void setData_solicitacao(Date data_solicitacao) {
		this.data_solicitacao = data_solicitacao;
	}
	public Date getData_ini() {
		return data_ini;
	}
	public void setData_ini(Date data_ini) {
		this.data_ini = data_ini;
	}
	public Date getData_fim() {
		return data_fim;
	}
	public void setData_fim(Date data_fim) {
		this.data_fim = data_fim;
	}
	public String getAceite() {
		return aceite;
	}
	public void setAceite(String aceite) {
		this.aceite = aceite;
	}
	public String getRequisicao() {
		return requisicao;
	}
	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}
	public String getEspecie() {
		return especie;
	}
	public void setEspecie(String especie) {
		this.especie = especie;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	public String getNome_unidade() {
		return nome_unidade;
	}
	public void setNome_unidade(String nome_unidade) {
		this.nome_unidade = nome_unidade;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
    
    
    
}
