package filter;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter("/*")
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // Garante acentos corretos nos parametros de formularios (POST)
        req.setCharacterEncoding("UTF-8");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        boolean rotaPublica = uri.endsWith("/login")
            || uri.endsWith("/login.jsp")
            || uri.contains("/css/")
            || uri.contains("/js/");

        HttpSession sessao = request.getSession(false);
        boolean logado = (sessao != null && sessao.getAttribute("usuarioLogado") != null);

        if (rotaPublica || logado) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
