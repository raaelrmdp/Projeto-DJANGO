package service;

import java.util.LinkedHashMap;
import java.util.Map;

import model.Solicitacoes;
import model.Usuario;

/**
 * Regras de quem pode fazer o que em um chamado. E a unica fonte dessas regras:
 * os servlets usam para BLOQUEAR e as telas usam (via mapa "acoes") para MOSTRAR os botoes.
 */
public class SolicitacaoRegras {

	public static final String ASSUMIR = "assumir";
	public static final String ATRIBUIR = "atribuir";
	public static final String RESOLVER = "resolver";
	public static final String FECHAR = "fechar";
	public static final String REABRIR = "reabrir";
	public static final String CANCELAR = "cancelar";

	private SolicitacaoRegras() {
	}

	/** Solicitante so ve os proprios chamados; tecnico e admin veem todos. */
	public static boolean podeVer(Usuario u, Solicitacoes s) {
		if (u == null || s == null) return false;
		if ("SOLICITANTE".equals(u.getPerfil())) return s.getUsuario_id() == u.getId();
		return true;
	}

	/** Mensagens so enquanto o chamado nao esta encerrado (fechado ou cancelado). */
	public static boolean podeInteragir(Usuario u, Solicitacoes s) {
		if (!podeVer(u, s)) return false;
		String st = s.getStatus();
		return "ABERTA".equals(st) || "EM_ATENDIMENTO".equals(st) || "RESOLVIDA".equals(st);
	}

	public static boolean pode(String acao, Usuario u, Solicitacoes s) {
		if (acao == null || !podeVer(u, s)) return false;

		boolean admin = "ADMIN".equals(u.getPerfil());
		boolean tecnico = "TECNICO".equals(u.getPerfil());
		boolean dono = s.getUsuario_id() == u.getId();
		boolean meuChamado = s.getTecnico_id() != null && s.getTecnico_id() == u.getId();
		String st = s.getStatus();
		boolean emAndamento = "ABERTA".equals(st) || "EM_ATENDIMENTO".equals(st);

		switch (acao) {
			case ASSUMIR:
				return (tecnico || admin) && "ABERTA".equals(st) && s.getTecnico_id() == null;
			case ATRIBUIR:
				return admin && emAndamento;
			case RESOLVER:
				return "EM_ATENDIMENTO".equals(st) && (admin || (tecnico && meuChamado));
			case FECHAR:
			case REABRIR:
				return "RESOLVIDA".equals(st) && (dono || admin);
			case CANCELAR:
				return (admin && emAndamento) || (dono && "ABERTA".equals(st));
			default:
				return false;
		}
	}

	/** Mapa acao -> permitido, para as telas (${acoes.assumir}). */
	public static Map<String, Boolean> acoes(Usuario u, Solicitacoes s) {
		Map<String, Boolean> mapa = new LinkedHashMap<>();
		for (String acao : new String[] { ASSUMIR, ATRIBUIR, RESOLVER, FECHAR, REABRIR, CANCELAR }) {
			mapa.put(acao, pode(acao, u, s));
		}
		return mapa;
	}
}
