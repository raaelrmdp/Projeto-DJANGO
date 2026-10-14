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

import dao.CategoriaDAO;
import dao.SolicitacaoDAO;
import model.Categorias;
import model.Solicitacoes;
import model.Usuario;
import util.Paginas;

@WebServlet("/solicitacoes/salvar")
public class SalvarSolicitacaoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final List<String> PRIORIDADES = Arrays.asList("BAIXA", "MEDIA", "ALTA", "URGENTE");

	private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
	private CategoriaDAO categoriaDAO = new CategoriaDAO();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
		if (!"SOLICITANTE".equals(usuario.getPerfil())) {
			Paginas.erro(request, response, 403, "Somente solicitantes abrem chamados.");
			return;
		}

		Solicitacoes s = new Solicitacoes();
		s.setTitulo(Paginas.texto(request, "titulo"));
		s.setDescricao(Paginas.texto(request, "descricao"));
		s.setLocal_ocorrencia(Paginas.texto(request, "local"));
		s.setPrioridade(Paginas.texto(request, "prioridade"));
		Integer categoriaId = Paginas.inteiro(request, "categoria");
		s.setCategoria_id(categoriaId == null ? 0 : categoriaId);
		s.setUsuario_id(usuario.getId());

		try {
			List<Categorias> categorias = categoriaDAO.listarAtivas();

			String erro = validar(s, categorias);
			if (erro != null) {
				// Volta ao formulario mantendo o que a pessoa digitou
				request.setAttribute("erro", erro);
				request.setAttribute("solicitacao", s);
				request.setAttribute("categorias", categorias);
				request.getRequestDispatcher("/WEB-INF/views/solicitacoes/formulario.jsp").forward(request, response);
				return;
			}

			int id = solicitacaoDAO.criar(s);
			response.sendRedirect(request.getContextPath() + "/solicitacoes/ver?id=" + id);

		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao salvar solicitação", e);
		}
	}

	private String validar(Solicitacoes s, List<Categorias> categoriasAtivas) {
		if (s.getTitulo().isEmpty()) return "Informe o título do chamado.";
		if (s.getTitulo().length() > 150) return "O título pode ter no máximo 150 caracteres.";
		if (s.getDescricao().isEmpty()) return "Descreva o problema.";
		if (s.getDescricao().length() > 5000) return "A descrição pode ter no máximo 5000 caracteres.";
		if (s.getLocal_ocorrencia().length() > 150) return "O local pode ter no máximo 150 caracteres.";
		if (!PRIORIDADES.contains(s.getPrioridade())) return "Escolha a prioridade.";

		boolean categoriaValida = false;
		for (Categorias c : categoriasAtivas) {
			if (c.getId() == s.getCategoria_id()) {
				categoriaValida = true;
				break;
			}
		}
		if (!categoriaValida) return "Escolha uma categoria.";

		if (s.getLocal_ocorrencia().isEmpty()) s.setLocal_ocorrencia(null);
		return null;
	}
}
