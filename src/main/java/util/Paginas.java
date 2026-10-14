package util;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Pequenos utilitarios para os servlets. */
public class Paginas {

	private Paginas() {
	}

	/** Mostra a pagina de erro amigavel com uma mensagem e o status HTTP indicado. */
	public static void erro(HttpServletRequest request, HttpServletResponse response, int status, String mensagem)
			throws ServletException, IOException {
		request.setAttribute("erro", mensagem);
		response.setStatus(status);
		request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
	}

	/** Le um parametro inteiro; devolve null se faltar ou nao for numero. */
	public static Integer inteiro(HttpServletRequest request, String nome) {
		String valor = request.getParameter(nome);
		if (valor == null) return null;
		try {
			return Integer.valueOf(valor.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/** Le um parametro de texto sem espacos nas pontas ("" se faltar). */
	public static String texto(HttpServletRequest request, String nome) {
		String valor = request.getParameter(nome);
		return valor == null ? "" : valor.trim();
	}
}
