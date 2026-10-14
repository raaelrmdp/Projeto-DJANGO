package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import dao.CategoriaDAO;
import model.Categorias;

@WebServlet("/categorias/atualizar")
public class AtualizarCategoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            // Carrega a categoria existente para preservar o campo "ativo"
            Categorias categoria = categoriaDAO.localizarCategoria(id);

            if (categoria == null) {
                request.setAttribute("erro", "Categoria não encontrada.");
                request.getRequestDispatcher("/WEB-INF/views/erro.jsp")
                       .forward(request, response);
                return;
            }

            categoria.setNome(request.getParameter("nome"));
            categoria.setDescricao(request.getParameter("descricao"));

            // UPDATE (antes chamava salvar(), que fazia INSERT e duplicava a categoria)
            categoriaDAO.alterar(categoria);

            response.sendRedirect(request.getContextPath() + "/categorias");
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Erro ao atualizar categoria", e);
        }
    }
}
