package model;

import java.sql.Timestamp;

public class Solicitacoes {

	private int id;
	private String titulo;
	private String descricao;
	private String local_ocorrencia;
	private String prioridade;
	private String status;
	private Timestamp data_abertura;
	private Timestamp data_fechamento;
	private int usuario_id;
	private Integer tecnico_id; // nulo enquanto ninguem assumiu o chamado
	private int categoria_id;

	// Campos de apoio para exibicao (preenchidos pelos JOINs do SolicitacaoDAO)
	private String usuario_nome;
	private String tecnico_nome;
	private String categoria_nome;

	public Solicitacoes() {
	}

	public Solicitacoes(int id, String titulo, String descricao, String local_ocorrencia, String prioridade,
			String status, Timestamp data_abertura, Timestamp data_fechamento, int usuario_id, Integer tecnico_id,
			int categoria_id) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.local_ocorrencia = local_ocorrencia;
		this.prioridade = prioridade;
		this.status = status;
		this.data_abertura = data_abertura;
		this.data_fechamento = data_fechamento;
		this.usuario_id = usuario_id;
		this.tecnico_id = tecnico_id;
		this.categoria_id = categoria_id;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getTitulo() { return titulo; }
	public void setTitulo(String titulo) { this.titulo = titulo; }

	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

	public String getLocal_ocorrencia() { return local_ocorrencia; }
	public void setLocal_ocorrencia(String local_ocorrencia) { this.local_ocorrencia = local_ocorrencia; }

	public String getPrioridade() { return prioridade; }
	public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public Timestamp getData_abertura() { return data_abertura; }
	public void setData_abertura(Timestamp data_abertura) { this.data_abertura = data_abertura; }

	public Timestamp getData_fechamento() { return data_fechamento; }
	public void setData_fechamento(Timestamp data_fechamento) { this.data_fechamento = data_fechamento; }

	public int getUsuario_id() { return usuario_id; }
	public void setUsuario_id(int usuario_id) { this.usuario_id = usuario_id; }

	public Integer getTecnico_id() { return tecnico_id; }
	public void setTecnico_id(Integer tecnico_id) { this.tecnico_id = tecnico_id; }

	public int getCategoria_id() { return categoria_id; }
	public void setCategoria_id(int categoria_id) { this.categoria_id = categoria_id; }

	public String getUsuario_nome() { return usuario_nome; }
	public void setUsuario_nome(String usuario_nome) { this.usuario_nome = usuario_nome; }

	public String getTecnico_nome() { return tecnico_nome; }
	public void setTecnico_nome(String tecnico_nome) { this.tecnico_nome = tecnico_nome; }

	public String getCategoria_nome() { return categoria_nome; }
	public void setCategoria_nome(String categoria_nome) { this.categoria_nome = categoria_nome; }

	// ---- Textos e classes CSS para as telas ----

	public String getStatusRotulo() {
		if (status == null) return "";
		switch (status) {
			case "ABERTA": return "Aberta";
			case "EM_ATENDIMENTO": return "Em atendimento";
			case "RESOLVIDA": return "Resolvida";
			case "FECHADA": return "Fechada";
			case "CANCELADA": return "Cancelada";
			default: return status;
		}
	}

	/** Sufixo da classe CSS: pill--status-{valor}. */
	public String getStatusClasse() {
		return status == null ? "" : status.toLowerCase().replace('_', '-');
	}

	public String getPrioridadeRotulo() {
		if (prioridade == null) return "";
		switch (prioridade) {
			case "BAIXA": return "Baixa";
			case "MEDIA": return "Média";
			case "ALTA": return "Alta";
			case "URGENTE": return "Urgente";
			default: return prioridade;
		}
	}

	/** Sufixo da classe CSS: pill--prio-{valor}. */
	public String getPrioridadeClasse() {
		return prioridade == null ? "" : prioridade.toLowerCase();
	}
}
