<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Entrar - HelpDesk</title>
    <link rel="icon" href="data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32'%3E%3Cpath fill='%23F5C518' d='M4 8a2 2 0 0 1 2-2h20a2 2 0 0 1 2 2v4a4 4 0 0 0 0 8v4a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-4a4 4 0 0 0 0-8z'/%3E%3C/svg%3E">
    <link rel="stylesheet" href="${ctx}/css/estilo.css">
</head>
<body>
<div class="entrada">

    <section class="entrada__vitrine">
        <span class="marca" style="padding:0">
            <svg width="30" height="30" viewBox="0 0 32 32" aria-hidden="true">
                <path fill="#F5C518" d="M4 8a2 2 0 0 1 2-2h20a2 2 0 0 1 2 2v4a4 4 0 0 0 0 8v4a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-4a4 4 0 0 0 0-8z"/>
                <path stroke="#12262F" stroke-width="2" stroke-dasharray="2 2.5" d="M12 10v12"/>
            </svg>
            HelpDesk
        </span>

        <div class="entrada__texto">
            <h1>Peça ajuda. Acompanhe até resolver.</h1>
            <p>Abra um chamado de TI, veja quem está cuidando dele e saiba quando foi resolvido.</p>
        </div>

        <%-- Ilustração: mostra o caminho de um chamado. Não são dados reais. --%>
        <div class="ticket ticket--amostra" aria-hidden="true">
            <div class="ticket__corpo">
                <div class="amostra__linha">
                    <span class="prioridade">Prioridade alta</span>
                    <span class="amostra__local">Sala 204</span>
                </div>
                <p class="amostra__titulo">Projetor não liga</p>
            </div>
            <div class="ticket__canhoto">
                <ol class="etapas">
                    <li class="feita">Aberto</li>
                    <li class="atual">Em atendimento</li>
                    <li>Resolvido</li>
                </ol>
            </div>
        </div>
    </section>

    <section class="entrada__form-area">
        <form class="entrada__form" action="${ctx}/login" method="post">
            <h2>Entrar</h2>
            <p>Use o e-mail e a senha cadastrados.</p>

            <c:if test="${not empty erro}">
                <p class="aviso" role="alert"><c:out value="${erro}"/></p>
            </c:if>

            <div class="campo">
                <label for="txtEmail">E-mail</label>
                <input type="email" id="txtEmail" name="txtEmail" autocomplete="username" required autofocus>
            </div>

            <div class="campo">
                <label for="txtSenha">Senha</label>
                <input type="password" id="txtSenha" name="txtSenha" autocomplete="current-password" required>
            </div>

            <button type="submit" class="btn">Entrar</button>
        </form>
    </section>

</div>
</body>
</html>
