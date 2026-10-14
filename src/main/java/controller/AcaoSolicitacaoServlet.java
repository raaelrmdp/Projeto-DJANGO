package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.SolicitacaoDAO;
import dao.UsuarioDAO;
import model.Solicitacoes;
import model.Usuario;
import service.SolicitacaoRegras;
import util.Paginas;

/** Executa as acoes do chamado: assumir, atribuir, resolver, fechar, reabrir e cancelar. */
@WebServlet("/solicitacoes/acao")
public class AcaoSolicitacaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		Integer id = Paginas.inteiro(request, "id");
		String acao = Paginas.texto(request, "acao");

		try {
			Solicitacoes s = (id == null) ? null : solicitacaoDAO.buscar(id);

			if (!SolicitacaoRegras.podeVer(usuario, s)) {
				Paginas.erro(request, response, 404, "Chamado não encontrado.");
				return;
			}
			if (!SolicitacaoRegras.pode(acao, usuario, s)) {
				Paginas.erro(request, response, 403, "Esta ação não está disponível para este chamado.");
				return;
			}

			switch (acao) {
				case SolicitacaoRegras.ASSUMIR:
					solicitacaoDAO.assumir(s.getId(), usuario.getId());
					break;
				case SolicitacaoRegras.ATRIBUIR:
					Integer tecnicoId = Paginas.inteiro(request, "tecnico");
					if (tecnicoId == null || !ehAtendente(tecnicoId)) {
						Paginas.erro(request, response, 400, "Escolha um técnico válido.");
						return;
					}
					solicitacaoDAO.atribuir(s.getId(), tecnicoId);
					break;
				case SolicitacaoRegras.RESOLVER:
					solicitacaoDAO.resolver(s.getId());
					break;
				case SolicitacaoRegras.FECHAR:
					solicitacaoDAO.fechar(s.getId());
					break;
				case SolicitacaoRegras.REABRIR:
					solicitacaoDAO.reabrir(s.getId());
					break;
				case SolicitacaoRegras.CANCELAR:
					solicitacaoDAO.cancelar(s.getId());
					break;
				default:
					break;
			}

			response.sendRedirect(request.getContextPath() + "/solicitacoes/ver?id=" + s.getId());

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao atualizar o chamado", e);
		}
	}

	private boolean ehAtendente(int usuarioId) throws SQLException, ClassNotFoundException {
		for (Usuario u : usuarioDAO.listarAtendentes()) {
			if (u.getId() == usuarioId) return true;
		}
		return false;
	}
}
