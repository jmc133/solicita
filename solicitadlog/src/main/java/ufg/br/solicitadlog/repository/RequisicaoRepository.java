package ufg.br.solicitadlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.Requisicao;

@Repository
public interface RequisicaoRepository extends JpaRepository<Requisicao, Long>{

}
