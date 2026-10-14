package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;

@WebServlet("/categorias")
public class ListarCategoriasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Tela de administração: lista ativas e inativas
            List<Categorias> categorias = categoriaDAO.listarCategorias();
            request.setAttribute("categorias", categorias);

            request.getRequestDispatcher("/WEB-INF/views/categorias/listar.jsp")
                   .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Erro ao listar categorias", e);
        }
    }
}
