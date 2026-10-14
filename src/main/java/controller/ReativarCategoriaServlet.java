package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;

@WebServlet("/categorias/reativar")
public class ReativarCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            categoriaDAO.reativa(id);
            response.sendRedirect(request.getContextPath() + "/categorias");
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Erro ao reativar categoria", e);
        }
    }
}
