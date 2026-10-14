package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.SolicitacaoDAO;
import model.Solicitacoes;
import model.Usuario;

@WebServlet("/solicitacoes")
public class ListarSolicitacoesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final List<String> STATUS_VALIDOS =
			Arrays.asList("ABERTA", "EM_ATENDIMENTO", "RESOLVIDA", "FECHADA", "CANCELADA");

	private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");

		String status = request.getParameter("status");
		if (status != null && !STATUS_VALIDOS.contains(status)) {
			status = null;
		}
		boolean somenteMeus = "1".equals(request.getParameter("meus")) && "TECNICO".equals(usuario.getPerfil());

		try {
			List<Solicitacoes> lista = solicitacaoDAO.listar(usuario, status, somenteMeus);

			request.setAttribute("solicitacoes", lista);
			request.setAttribute("filtroStatus", status);
			request.setAttribute("filtroMeus", somenteMeus);

			request.getRequestDispatcher("/WEB-INF/views/solicitacoes/listar.jsp").forward(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao listar solicitações", e);
		}
	}
}
