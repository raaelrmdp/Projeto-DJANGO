package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bd.Conexao;
import model.Interacoes;
import model.Solicitacoes;
import model.Usuario;

public class SolicitacaoDAO {

	private static final String SELECT_BASE =
			"select s.id, s.titulo, s.descricao, s.local_ocorrencia, s.prioridade, s.status, "
			+ "s.data_abertura, s.data_fechamento, s.usuario_id, s.tecnico_id, s.categoria_id, "
			+ "u.nome as usuario_nome, t.nome as tecnico_nome, c.nome as categoria_nome "
			+ "from solicitacoes s "
			+ "join usuarios u on u.id = s.usuario_id "
			+ "left join usuarios t on t.id = s.tecnico_id "
			+ "join categorias c on c.id = s.categoria_id ";

	private static Timestamp agora() {
		// Gravamos o horario pelo Java (e nao por NOW() do MySQL) para o fuso ficar consistente
		return new Timestamp(System.currentTimeMillis());
	}

	/** Cria o chamado (sempre ABERTA, sem tecnico) e devolve o id gerado. */
	public int criar(Solicitacoes s) throws SQLException, ClassNotFoundException {
		String sql = "insert into solicitacoes "
				+ "(titulo, descricao, local_ocorrencia, prioridade, status, data_abertura, usuario_id, categoria_id) "
				+ "values (?, ?, ?, ?, 'ABERTA', ?, ?, ?)";

		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			comando.setString(1, s.getTitulo());
			comando.setString(2, s.getDescricao());
			comando.setString(3, s.getLocal_ocorrencia());
			comando.setString(4, s.getPrioridade());
			comando.setTimestamp(5, agora());
			comando.setInt(6, s.getUsuario_id());
			comando.setInt(7, s.getCategoria_id());
			comando.executeUpdate();

			try (ResultSet chaves = comando.getGeneratedKeys()) {
				return chaves.next() ? chaves.getInt(1) : 0;
			}
		}
	}

	/**
	 * Lista os chamados que o usuario pode ver.
	 * SOLICITANTE: so os proprios. TECNICO e ADMIN: todos.
	 * @param status filtro opcional (ex.: "ABERTA"); null = todos
	 * @param somenteAtribuidosAMim filtro para tecnico: so os chamados dele
	 */
	public List<Solicitacoes> listar(Usuario ator, String status, boolean somenteAtribuidosAMim)
			throws SQLException, ClassNotFoundException {

		StringBuilder sql = new StringBuilder(SELECT_BASE).append("where 1 = 1 ");
		List<Object> parametros = new ArrayList<>();

		if ("SOLICITANTE".equals(ator.getPerfil())) {
			sql.append("and s.usuario_id = ? ");
			parametros.add(ator.getId());
		}
		if (status != null) {
			sql.append("and s.status = ? ");
			parametros.add(status);
		}
		if (somenteAtribuidosAMim) {
			sql.append("and s.tecnico_id = ? ");
			parametros.add(ator.getId());
		}
		sql.append("order by s.data_abertura desc, s.id desc");

		List<Solicitacoes> lista = new ArrayList<>();
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql.toString())) {

			for (int i = 0; i < parametros.size(); i++) {
				comando.setObject(i + 1, parametros.get(i));
			}
			try (ResultSet rs = comando.executeQuery()) {
				while (rs.next()) {
					lista.add(montar(rs));
				}
			}
		}
		return lista;
	}

	/** Devolve o chamado ou null se nao existir. */
	public Solicitacoes buscar(int id) throws SQLException, ClassNotFoundException {
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(SELECT_BASE + "where s.id = ?")) {

			comando.setInt(1, id);
			try (ResultSet rs = comando.executeQuery()) {
				return rs.next() ? montar(rs) : null;
			}
		}
	}

	public List<Interacoes> listarInteracoes(int solicitacaoId) throws SQLException, ClassNotFoundException {
		String sql = "select i.id, i.mensagem, i.data_interacao, i.solicitacao_id, i.usuario_id, "
				+ "u.nome as usuario_nome, u.perfil as usuario_perfil "
				+ "from interacoes i join usuarios u on u.id = i.usuario_id "
				+ "where i.solicitacao_id = ? order by i.data_interacao, i.id";

		List<Interacoes> lista = new ArrayList<>();
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setInt(1, solicitacaoId);
			try (ResultSet rs = comando.executeQuery()) {
				while (rs.next()) {
					Interacoes i = new Interacoes();
					i.setId(rs.getInt("id"));
					i.setMensagem(rs.getString("mensagem"));
					i.setData_interacao(rs.getTimestamp("data_interacao"));
					i.setSolicitacao_id(rs.getInt("solicitacao_id"));
					i.setUsuario_id(rs.getInt("usuario_id"));
					i.setUsuario_nome(rs.getString("usuario_nome"));
					i.setUsuario_perfil(rs.getString("usuario_perfil"));
					lista.add(i);
				}
			}
		}
		return lista;
	}

	public void adicionarInteracao(int solicitacaoId, int usuarioId, String mensagem)
			throws SQLException, ClassNotFoundException {
		String sql = "insert into interacoes (mensagem, data_interacao, solicitacao_id, usuario_id) "
				+ "values (?, ?, ?, ?)";
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql)) {

			comando.setString(1, mensagem);
			comando.setTimestamp(2, agora());
			comando.setInt(3, solicitacaoId);
			comando.setInt(4, usuarioId);
			comando.executeUpdate();
		}
	}

	// ---- Acoes sobre o chamado. Cada uma so altera se o status atual permitir (evita corrida entre dois usuarios).
	// ---- Devolvem false quando nada foi alterado.

	/** Tecnico assume um chamado ABERTA e sem tecnico. */
	public boolean assumir(int id, int tecnicoId) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes set tecnico_id = ?, status = 'EM_ATENDIMENTO' "
				+ "where id = ? and status = 'ABERTA' and tecnico_id is null", tecnicoId, id);
	}

	/** Admin define (ou troca) o tecnico de um chamado ainda em andamento. */
	public boolean atribuir(int id, int tecnicoId) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes set tecnico_id = ?, status = 'EM_ATENDIMENTO' "
				+ "where id = ? and status in ('ABERTA', 'EM_ATENDIMENTO')", tecnicoId, id);
	}

	public boolean resolver(int id) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes set status = 'RESOLVIDA', data_fechamento = ? "
				+ "where id = ? and status = 'EM_ATENDIMENTO'", agora(), id);
	}

	public boolean fechar(int id) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes set status = 'FECHADA' "
				+ "where id = ? and status = 'RESOLVIDA'", id);
	}

	/** Volta para EM_ATENDIMENTO (se ja tinha tecnico) ou ABERTA, e limpa a data de fechamento. */
	public boolean reabrir(int id) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes "
				+ "set status = if(tecnico_id is null, 'ABERTA', 'EM_ATENDIMENTO'), data_fechamento = null "
				+ "where id = ? and status = 'RESOLVIDA'", id);
	}

	public boolean cancelar(int id) throws SQLException, ClassNotFoundException {
		return executar("update solicitacoes set status = 'CANCELADA', data_fechamento = ? "
				+ "where id = ? and status in ('ABERTA', 'EM_ATENDIMENTO')", agora(), id);
	}

	private boolean executar(String sql, Object... parametros) throws SQLException, ClassNotFoundException {
		try (Connection conexao = Conexao.obterConexao();
				PreparedStatement comando = conexao.prepareStatement(sql)) {

			for (int i = 0; i < parametros.length; i++) {
				comando.setObject(i + 1, parametros[i]);
			}
			return comando.executeUpdate() > 0;
		}
	}

	private Solicitacoes montar(ResultSet rs) throws SQLException {
		Solicitacoes s = new Solicitacoes();
		s.setId(rs.getInt("id"));
		s.setTitulo(rs.getString("titulo"));
		s.setDescricao(rs.getString("descricao"));
		s.setLocal_ocorrencia(rs.getString("local_ocorrencia"));
		s.setPrioridade(rs.getString("prioridade"));
		s.setStatus(rs.getString("status"));
		s.setData_abertura(rs.getTimestamp("data_abertura"));
		s.setData_fechamento(rs.getTimestamp("data_fechamento"));
		s.setUsuario_id(rs.getInt("usuario_id"));
		int tecnico = rs.getInt("tecnico_id");
		s.setTecnico_id(rs.wasNull() ? null : tecnico);
		s.setCategoria_id(rs.getInt("categoria_id"));
		s.setUsuario_nome(rs.getString("usuario_nome"));
		s.setTecnico_nome(rs.getString("tecnico_nome"));
		s.setCategoria_nome(rs.getString("categoria_nome"));
		return s;
	}
}
