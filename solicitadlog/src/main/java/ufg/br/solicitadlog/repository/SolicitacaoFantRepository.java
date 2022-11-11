package ufg.br.solicitadlog.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.SolicitacaoFant;

@Repository
public interface SolicitacaoFantRepository extends JpaRepository<SolicitacaoFant, Long> {
	
	@Query(value = "select t_solicitacao.id,t_solicitacao.data_solicitacao,t_solicitacao.data_ini,t_solicitacao.data_fim,\r\n"
			+ "t_solicitacao.aceite,t_requisicao.requisicao,t_especie.especie,t_solicitacao.destino,t_solicitacao.motivo,\r\n"
			+ "t_solicitacao.solicitante,t_solicitacao.nome_unidade,t_solicitacao.observacao\r\n"
			+ "from t_solicitacao, t_requisicao, t_especie\r\n"
			+ "where t_solicitacao.especie=t_especie.cd_especie\r\n"
			+ "and t_solicitacao.requisicao=t_requisicao.cod_req\r\n"
			+ "and t_solicitacao.data_ini>=CURRENT_DATE\r\n"
			+ "and t_solicitacao.cpf_solicitante=?1\r\n"
			+ "order by t_solicitacao.data_ini ",nativeQuery = true)
	Collection<SolicitacaoFant> solicitacoesPorCpf(String cpf);

}
