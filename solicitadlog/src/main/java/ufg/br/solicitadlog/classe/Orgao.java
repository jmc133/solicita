package ufg.br.solicitadlog.classe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Orgao")
public class Orgao implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CD_ORGAO")
	private Long cd_orgao;
	@NotNull
	@Column(name = "SG_ORGAO")
	private String sg_orgao;
	@NotNull
	@Column(name = "NM_ORGAO")
	private String nm_orgao;
	
	
	public Long getCd_orgao() {
		return cd_orgao;
	}
	public void setCd_orgao(Long cd_orgao) {
		this.cd_orgao = cd_orgao;
	}
	public String getSg_orgao() {
		return sg_orgao;
	}
	public void setSg_orgao(String sg_orgao) {
		this.sg_orgao = sg_orgao;
	}
	public String getNm_orgao() {
		return nm_orgao;
	}
	public void setNm_orgao(String nm_orgao) {
		this.nm_orgao = nm_orgao;
	}

	
	

}
