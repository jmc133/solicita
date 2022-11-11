package ufg.br.solicitadlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.solicitacao;

@Repository
public interface SolicitacaoRepository extends JpaRepository<solicitacao, Long> {
	
	@Query(value = "select max(id) from t_solicitacao", nativeQuery = true)
	Long verMaximoId();

}
