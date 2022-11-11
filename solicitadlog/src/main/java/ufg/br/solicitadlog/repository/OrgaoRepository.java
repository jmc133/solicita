package ufg.br.solicitadlog.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufg.br.solicitadlog.classe.*;

@Transactional
@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {
	
	@Query(value = "select * from orgao order by sg_orgao", nativeQuery = true)
	List<Orgao> buscaOrgaoOrdenadoPorSgOrgao();
	
	@Query(value = "select * from orgao where cd_orgao=?1", nativeQuery = true)
	Orgao buscaPorId(Long id);
	
	@Query(value = "select * from orgao where nm_orgao LIKE %?1%", nativeQuery = true)
	List<Orgao> buscaPorNome(String nome);
	
	@Query(value = "select count(cd_orgao) from orgao where nm_orgao LIKE %?1%", nativeQuery = true)
	Long contaBuscaPorNome(String nome);
	
	@Query(value = "select max(cd_orgao)from orgao", nativeQuery = true)
	Long maximoCodigoOrgaos();
	
	@Query(value = "select * from orgao where sg_orgao =?1", nativeQuery = true)
	List<Orgao> pesquisaOrgaoPorSigla(String sigla);
	
	@Query(value = "select cd_orgao from orgao where sg_orgao=?1", nativeQuery = true)
	Long[] buscaCodigoPorSg_orgao(String sg);

}
