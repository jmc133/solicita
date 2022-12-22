package ufg.br.solicitadlog.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ufg.br.solicitadlog.classe.Usuario_Externo;

@Repository
public interface UsuarioExternoRepository extends JpaRepository<Usuario_Externo, String> {
	@Query(value = "select * from usuario_externo where cpf =?1", nativeQuery = true)
	Usuario_Externo verExistenciaLocalDeUsuarioExterno(String cpf);
	
	@Modifying
	@Transactional(readOnly = false)
	@Query(value = "update usuario_externo set senha=?1 where cpf=?2", nativeQuery = true)
	String updateSenha(String senha, String cpf);

}
