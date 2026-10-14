package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.SolicitacaoDAO;
import model.Solicitacoes;
import model.Usuario;
import service.SolicitacaoRegras;
import util.Paginas;

@WebServlet("/solicitacoes/interagir")
public class InteragirSolicitacaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		Integer id = Paginas.inteiro(request, "id");
		String mensagem = Paginas.texto(request, "mensagem");

		try {
			Solicitacoes s = (id == null) ? null : solicitacaoDAO.buscar(id);

			if (!SolicitacaoRegras.podeVer(usuario, s)) {
				Paginas.erro(request, response, 404, "Chamado não encontrado.");
				return;
			}
			if (!SolicitacaoRegras.podeInteragir(usuario, s)) {
				Paginas.erro(request, response, 403, "Este chamado está encerrado e não aceita novas mensagens.");
				return;
			}
			if (mensagem.isEmpty() || mensagem.length() > 2000) {
				Paginas.erro(request, response, 400, "Escreva uma mensagem de até 2000 caracteres.");
				return;
			}

			solicitacaoDAO.adicionarInteracao(s.getId(), usuario.getId(), mensagem);
			response.sendRedirect(request.getContextPath() + "/solicitacoes/ver?id=" + s.getId() + "#conversa");

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao enviar a mensagem", e);
		}
	}
}
