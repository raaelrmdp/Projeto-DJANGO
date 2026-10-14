package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Usuario;

/** Cadastro de usuarios (tela de administracao). Arquivo novo: nao altera o UsuarioDAO. */
public class UsuarioAdminDAO {

	private static final String COLUNAS = "id, nome, email, perfil, ativo, data_cadastro";

	private Usuario ler(ResultSet r) throws SQLException {
		Usuario u = new Usuario();
		u.setId(r.getInt("id"));
		u.setNome(r.getString("nome"));
		u.setEmail(r.getString("email"));
		u.setPerfil(r.getString("perfil"));
		u.setAtivo(r.getBoolean("ativo"));
		Timestamp t = r.getTimestamp("data_cadastro");
		if (t != null) u.setData_cadastro(new java.sql.Date(t.getTime()));
		return u;
	}

	public List<Usuario> listar() throws SQLException, ClassNotFoundException {
		List<Usuario> lista = new ArrayList<>();
		String sql = "select " + COLUNAS + " from usuarios order by ativo desc, nome";
		try (Connection c = Conexao.obterConexao();
				PreparedStatement p = c.prepareStatement(sql);
				ResultSet r = p.executeQuery()) {
			while (r.next()) lista.add(ler(r));
		}
		return lista;
	}

	public Usuario localizar(int id) throws SQLException, ClassNotFoundException {
		String sql = "select " + COLUNAS + " from usuarios where id = ?";
		try (Connection c = Conexao.obterConexao(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setInt(1, id);
			try (ResultSet r = p.executeQuery()) {
				return r.next() ? ler(r) : null;
			}
		}
	}

	public boolean emailEmUso(String email, int idIgnorar) throws SQLException, ClassNotFoundException {
		String sql = "select 1 from usuarios where email = ? and id <> ?";
		try (Connection c = Conexao.obterConexao(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setString(1, email);
			p.setInt(2, idIgnorar);
			try (ResultSet r = p.executeQuery()) {
				return r.next();
			}
		}
	}

	public void inserir(Usuario u) throws SQLException, ClassNotFoundException {
		String sql = "insert into usuarios (nome, email, senha, perfil) values (?, ?, ?, ?)";
		try (Connection c = Conexao.obterConexao(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setString(1, u.getNome());
			p.setString(2, u.getEmail());
			p.setString(3, u.getSenha());
			p.setString(4, u.getPerfil());
			p.executeUpdate();
		}
	}

	/** Atualiza dados; a senha so muda se vier preenchida. */
	public void alterar(Usuario u) throws SQLException, ClassNotFoundException {
		boolean trocaSenha = u.getSenha() != null && !u.getSenha().isEmpty();
		String sql = "update usuarios set nome = ?, email = ?, perfil = ?"
				+ (trocaSenha ? ", senha = ?" : "") + " where id = ?";
		try (Connection c = Conexao.obterConexao(); PreparedStatement p = c.prepareStatement(sql)) {
			int i = 1;
			p.setString(i++, u.getNome());
			p.setString(i++, u.getEmail());
			p.setString(i++, u.getPerfil());
			if (trocaSenha) p.setString(i++, u.getSenha());
			p.setInt(i, u.getId());
			p.executeUpdate();
		}
	}

	public void definirAtivo(int id, boolean ativo) throws SQLException, ClassNotFoundException {
		String sql = "update usuarios set ativo = ? where id = ?";
		try (Connection c = Conexao.obterConexao(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setBoolean(1, ativo);
			p.setInt(2, id);
			p.executeUpdate();
		}
	}
}
