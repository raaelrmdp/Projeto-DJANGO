<%@ page contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Algo deu errado - HelpDesk</title>
    <link rel="stylesheet" href="${ctx}/css/estilo.css">
</head>
<body>
<div class="pagina-simples">
    <header class="pagina-simples__topo">
        <a class="marca" href="${ctx}/home">HelpDesk</a>
    </header>

    <main class="pagina-simples__centro">
        <div class="pagina-simples__cartao">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    <span class="codigo" aria-hidden="true">404</span>
                    <h1>Página não encontrada</h1>
                    <p>O endereço não existe ou foi movido. Volte ao início e tente de novo.</p>
                </c:when>
                <c:when test="${not empty erro}">
                    <h1>Não foi possível continuar</h1>
                    <p><c:out value="${erro}"/></p>
                </c:when>
                <c:otherwise>
                    <h1>Algo deu errado</h1>
                    <p>Não foi possível concluir a ação. Tente de novo. Se o erro continuar, avise o administrador.</p>
                </c:otherwise>
            </c:choose>
            <a class="btn" href="${ctx}/home">Voltar ao início</a>
        </div>
    </main>
</div>
</body>
</html>
