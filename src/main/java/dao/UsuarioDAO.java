package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Usuario;

public class UsuarioDAO {
	
	public Usuario logar(String email, String senha) throws SQLException, ClassNotFoundException{
		
		String sql = "select id, nome, email, perfil from usuarios " +
		   " where email = ? and senha = ? and ativo = true";
		
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement logar = conexao.prepareStatement(sql)){
				
				logar.setString(1, email);
				logar.setString(2, senha);
				
				try (ResultSet resultado = logar.executeQuery()){
					if (resultado.next()) {
						Usuario usu = new Usuario();
						usu.setId(resultado.getInt("id"));
						usu.setNome(resultado.getString("nome"));
						usu.setEmail(resultado.getString("email"));
						usu.setPerfil(resultado.getString("perfil"));
						
						return usu;
					}
				}
					
		}
		return null;
	}

	/** Usuarios ativos que podem atender chamados (tecnicos e administradores). */
	public List<Usuario> listarAtendentes() throws SQLException, ClassNotFoundException {
		List<Usuario> lista = new ArrayList<>();
		String sql = "select id, nome, perfil from usuarios "
				+ "where ativo = true and perfil in ('TECNICO', 'ADMIN') order by nome";

		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql);
				ResultSet resultado = comando.executeQuery()) {

			while (resultado.next()) {
				Usuario usu = new Usuario();
				usu.setId(resultado.getInt("id"));
				usu.setNome(resultado.getString("nome"));
				usu.setPerfil(resultado.getString("perfil"));
				lista.add(usu);
			}
		}
		return lista;
	}
}
