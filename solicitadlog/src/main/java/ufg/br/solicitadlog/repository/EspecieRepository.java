package ufg.br.solicitadlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.Especie;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Integer>{

}
