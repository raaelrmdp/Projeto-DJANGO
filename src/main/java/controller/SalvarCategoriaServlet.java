package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CategoriaDAO;
import model.Categorias;

@WebServlet("/categorias/salvar")
public class SalvarCategoriaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CategoriaDAO categoriaDAO = new CategoriaDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Os nomes dos parametros precisam bater com os "name" do formulario.jsp
		Categorias cat = new Categorias();
		cat.setNome(request.getParameter("nome"));
		cat.setDescricao(request.getParameter("descricao"));

		try {
			categoriaDAO.salvar(cat);
			response.sendRedirect(request.getContextPath() + "/categorias");
		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao salvar categoria", e);
		}
	}
}
