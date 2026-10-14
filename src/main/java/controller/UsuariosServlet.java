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

import dao.UsuarioAdminDAO;
import model.Usuario;
import util.Paginas;

/**
 * Cadastro de usuarios (somente ADMIN).
 *   GET  /usuarios            lista        GET  /usuarios/nova      formulario
 *   GET  /usuarios/editar     formulario   POST /usuarios/salvar    cria ou atualiza
 *   POST /usuarios/situacao   ativa/desativa
 */
@WebServlet(urlPatterns = { "/usuarios", "/usuarios/nova", "/usuarios/editar", "/usuarios/salvar", "/usuarios/situacao" })
public class UsuariosServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final List<String> PERFIS = Arrays.asList("ADMIN", "TECNICO", "SOLICITANTE");
	private static final String VIEWS = "/WEB-INF/views/usuarios/";

	private UsuarioAdminDAO dao = new UsuarioAdminDAO();

	private boolean ehAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		if (logado == null || !"ADMIN".equals(logado.getPerfil())) {
			Paginas.erro(req, resp, 403, "Apenas administradores podem gerenciar usuários.");
			return false;
		}
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ehAdmin(req, resp)) return;
		String rota = req.getServletPath();
		try {
			if (rota.equals("/usuarios/nova")) {
				req.setAttribute("usuarioForm", new Usuario());
				req.getRequestDispatcher(VIEWS + "formulario.jsp").forward(req, resp);
			} else if (rota.equals("/usuarios/editar")) {
				Integer id = Paginas.inteiro(req, "id");
				Usuario u = id == null ? null : dao.localizar(id);
				if (u == null) {
					Paginas.erro(req, resp, 404, "Usuário não encontrado.");
					return;
				}
				req.setAttribute("usuarioForm", u);
				req.getRequestDispatcher(VIEWS + "formulario.jsp").forward(req, resp);
			} else if (rota.equals("/usuarios")) {
				req.setAttribute("usuarios", dao.listar());
				req.getRequestDispatcher(VIEWS + "listar.jsp").forward(req, resp);
			} else {
				resp.sendError(405);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao carregar usuários", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!ehAdmin(req, resp)) return;
		String rota = req.getServletPath();
		try {
			if (rota.equals("/usuarios/salvar")) {
				salvar(req, resp);
			} else if (rota.equals("/usuarios/situacao")) {
				mudarSituacao(req, resp);
			} else {
				resp.sendError(405);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException("Erro ao gravar usuário", e);
		}
	}

	private void salvar(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, ClassNotFoundException, ServletException, IOException {

		Integer id = Paginas.inteiro(req, "id");
		Usuario u = new Usuario();
		u.setId(id == null ? 0 : id);
		u.setNome(Paginas.texto(req, "nome"));
		u.setEmail(Paginas.texto(req, "email").toLowerCase());
		u.setPerfil(Paginas.texto(req, "perfil"));
		u.setSenha(req.getParameter("senha") == null ? "" : req.getParameter("senha"));

		Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		boolean novo = u.getId() == 0;
		String erro = null;

		if (u.getNome().isEmpty() || u.getEmail().isEmpty()) {
			erro = "Informe nome e e-mail.";
		} else if (!u.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
			erro = "E-mail inválido.";
		} else if (!PERFIS.contains(u.getPerfil())) {
			erro = "Escolha um perfil válido.";
		} else if (novo && u.getSenha().isEmpty()) {
			erro = "Informe uma senha para o novo usuário.";
		} else if (!novo && logado.getId() == u.getId() && !"ADMIN".equals(u.getPerfil())) {
			erro = "Você não pode remover o seu próprio perfil de administrador.";
		} else if (dao.emailEmUso(u.getEmail(), u.getId())) {
			erro = "Já existe um usuário com esse e-mail.";
		} else if (!novo && dao.localizar(u.getId()) == null) {
			Paginas.erro(req, resp, 404, "Usuário não encontrado.");
			return;
		}

		if (erro != null) {
			u.setSenha(null);
			req.setAttribute("erro", erro);
			req.setAttribute("usuarioForm", u);
			req.getRequestDispatcher(VIEWS + "formulario.jsp").forward(req, resp);
			return;
		}

		if (novo) dao.inserir(u); else dao.alterar(u);
		resp.sendRedirect(req.getContextPath() + "/usuarios");
	}

	private void mudarSituacao(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, ClassNotFoundException, ServletException, IOException {

		Integer id = Paginas.inteiro(req, "id");
		Usuario alvo = id == null ? null : dao.localizar(id);
		if (alvo == null) {
			Paginas.erro(req, resp, 404, "Usuário não encontrado.");
			return;
		}
		Usuario logado = (Usuario) req.getSession().getAttribute("usuarioLogado");
		boolean ativar = "1".equals(req.getParameter("ativo"));
		if (!ativar && logado.getId() == alvo.getId()) {
			Paginas.erro(req, resp, 400, "Você não pode desativar o seu próprio acesso.");
			return;
		}
		dao.definirAtivo(alvo.getId(), ativar);
		resp.sendRedirect(req.getContextPath() + "/usuarios");
	}
}
