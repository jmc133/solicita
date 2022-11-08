package ufg.br.solicitadlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.Usuario_Externo;

@Repository
public interface UsuarioExternoRepository extends JpaRepository<Usuario_Externo, String> {

}
