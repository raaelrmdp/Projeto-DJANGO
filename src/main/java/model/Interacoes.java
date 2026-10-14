package model;

import java.sql.Timestamp;

public class Interacoes {

	private int id;
	private String mensagem;
	private Timestamp data_interacao;
	private int solicitacao_id;
	private int usuario_id;

	// Apoio para exibicao (JOIN com usuarios)
	private String usuario_nome;
	private String usuario_perfil;

	public Interacoes() {
	}

	public Interacoes(int id, String mensagem, Timestamp data_interacao, int solicitacao_id, int usuario_id) {
		this.id = id;
		this.mensagem = mensagem;
		this.data_interacao = data_interacao;
		this.solicitacao_id = solicitacao_id;
		this.usuario_id = usuario_id;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	public Timestamp getData_interacao() { return data_interacao; }
	public void setData_interacao(Timestamp data_interacao) { this.data_interacao = data_interacao; }

	public int getSolicitacao_id() { return solicitacao_id; }
	public void setSolicitacao_id(int solicitacao_id) { this.solicitacao_id = solicitacao_id; }

	public int getUsuario_id() { return usuario_id; }
	public void setUsuario_id(int usuario_id) { this.usuario_id = usuario_id; }

	public String getUsuario_nome() { return usuario_nome; }
	public void setUsuario_nome(String usuario_nome) { this.usuario_nome = usuario_nome; }

	public String getUsuario_perfil() { return usuario_perfil; }
	public void setUsuario_perfil(String usuario_perfil) { this.usuario_perfil = usuario_perfil; }

	public String getPerfilRotulo() {
		if (usuario_perfil == null) return "";
		switch (usuario_perfil) {
			case "ADMIN": return "Administrador";
			case "TECNICO": return "Técnico";
			default: return "Solicitante";
		}
	}
}
