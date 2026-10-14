package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoriaDAO;
import model.Solicitacoes;
import model.Usuario;
import util.Paginas;

@WebServlet("/solicitacoes/nova")
public class NovaSolicitacaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private CategoriaDAO categoriaDAO = new CategoriaDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		if (!"SOLICITANTE".equals(usuario.getPerfil())) {
			Paginas.erro(request, response, 403, "Somente solicitantes abrem chamados.");
			return;
		}

		try {
			Solicitacoes vazia = new Solicitacoes();
			vazia.setPrioridade("MEDIA");

			request.setAttribute("solicitacao", vazia);
			request.setAttribute("categorias", categoriaDAO.listarAtivas());
			request.getRequestDispatcher("/WEB-INF/views/solicitacoes/formulario.jsp").forward(request, response);

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao carregar o formulário", e);
		}
	}
}
