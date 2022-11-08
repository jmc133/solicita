package ufg.br.solicitadlog.classe;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "t_solicitacao")
public class solicitacao implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data_ini;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data_fim;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date data_solicitacao;
    @Column(name = "aceite", length = 1)
    private String aceite;
    private String destino;
    private String motivo;
    private String observacao;
    private String solicitante;
    private int especie;
    private Long requisicao;
    private Long unidade;
    private String nome_unidade;
    private String cpf_solicitante;
    private String contato;
    
    
	public Date getData_solicitacao() {
		return data_solicitacao;
	}
	public void setData_solicitacao(Date data_solicitacao) {
		this.data_solicitacao = data_solicitacao;
	}
	public String getContato() {
		return contato;
	}
	public void setContato(String contato) {
		this.contato = contato;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	public int getEspecie() {
		return especie;
	}
	public void setEspecie(int especie) {
		this.especie = especie;
	}
	public Long getRequisicao() {
		return requisicao;
	}
	public void setRequisicao(Long requisicao) {
		this.requisicao = requisicao;
	}
	public Long getUnidade() {
		return unidade;
	}
	public void setUnidade(Long unidade) {
		this.unidade = unidade;
	}
	public String getNome_unidade() {
		return nome_unidade;
	}
	public void setNome_unidade(String nome_unidade) {
		this.nome_unidade = nome_unidade;
	}
	public String getCpf_solicitante() {
		return cpf_solicitante;
	}
	public void setCpf_solicitante(String cpf_solicitante) {
		this.cpf_solicitante = cpf_solicitante;
	}
    
    
    

}
