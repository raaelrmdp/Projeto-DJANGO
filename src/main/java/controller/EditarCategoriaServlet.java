package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;

@WebServlet("/categorias/editar")
public class EditarCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            Categorias categoria = categoriaDAO.localizarCategoria(id);

            if (categoria == null) {
                request.setAttribute("erro", "Categoria não encontrada.");
                request.getRequestDispatcher("/WEB-INF/views/erro.jsp")
                       .forward(request, response);
                return;
            }

            request.setAttribute("categoria", categoria);
            request.getRequestDispatcher("/WEB-INF/views/categorias/editar.jsp")
                   .forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Erro ao carregar categoria", e);
        }
    }
}
