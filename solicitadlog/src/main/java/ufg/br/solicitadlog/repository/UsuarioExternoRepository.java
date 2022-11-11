package ufg.br.solicitadlog.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufg.br.solicitadlog.classe.Usuario_Externo;

@Repository
public interface UsuarioExternoRepository extends JpaRepository<Usuario_Externo, String> {
	@Query(value = "select * from usuario_externo where cpf =?1", nativeQuery = true)
	Usuario_Externo verExistenciaLocalDeUsuarioExterno(String cpf);

}
