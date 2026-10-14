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

@WebServlet("/solicitacoes/ver")
public class VerSolicitacaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		Integer id = Paginas.inteiro(request, "id");

		try {
			Solicitacoes s = (id == null) ? null : solicitacaoDAO.buscar(id);

			// Mesma resposta para "nao existe" e "nao e seu": nao revela chamados de outras pessoas
			if (!SolicitacaoRegras.podeVer(usuario, s)) {
				Paginas.erro(request, response, 404, "Chamado não encontrado.");
				return;
			}

			request.setAttribute("solicitacao", s);
			request.setAttribute("interacoes", solicitacaoDAO.listarInteracoes(s.getId()));
			request.setAttribute("acoes", SolicitacaoRegras.acoes(usuario, s));
			request.setAttribute("podeInteragir", SolicitacaoRegras.podeInteragir(usuario, s));

			if (SolicitacaoRegras.pode(SolicitacaoRegras.ATRIBUIR, usuario, s)) {
				request.setAttribute("atendentes", usuarioDAO.listarAtendentes());
			}

			request.getRequestDispatcher("/WEB-INF/views/solicitacoes/ver.jsp").forward(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao carregar o chamado", e);
		}
	}
}
