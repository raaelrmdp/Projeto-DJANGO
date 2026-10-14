<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Início"/>
<c:set var="menuAtivo" value="home"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Olá, <c:out value="${sessionScope.usuarioLogado.nome}"/></h1>
        <p>Escolha por onde começar.</p>
    </div>
</div>

<div class="modulos">

    <c:if test="${sessionScope.usuarioLogado.perfil == 'ADMIN'}">
        <article class="ticket">
            <div class="ticket__corpo">
                <svg class="ticket__icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                    <path d="M20.6 13.4 13.4 20.6a2 2 0 0 1-2.8 0L3 13V3h10l7.6 7.6a2 2 0 0 1 0 2.8z"/><circle cx="7.5" cy="7.5" r="1.2"/>
                </svg>
                <h2>Categorias</h2>
                <p>Organize os tipos de chamado: hardware, rede, software e outros.</p>
            </div>
            <a class="ticket__canhoto" href="${ctx}/categorias">Gerenciar categorias</a>
        </article>
    </c:if>

    <article class="ticket">
        <div class="ticket__corpo">
            <svg class="ticket__icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M22 12h-6l-2 3h-4l-2-3H2"/><path d="M5.5 5.1 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.5-6.9A2 2 0 0 0 16.7 4H7.3a2 2 0 0 0-1.8 1.1z"/>
            </svg>
            <h2>Solicitações</h2>
            <p>
                <c:choose>
                    <c:when test="${sessionScope.usuarioLogado.perfil == 'SOLICITANTE'}">Acompanhe o andamento dos seus chamados.</c:when>
                    <c:otherwise>Veja os chamados da instituição e atenda os que estão na fila.</c:otherwise>
                </c:choose>
            </p>
        </div>
        <a class="ticket__canhoto" href="${ctx}/solicitacoes">Ver solicitações</a>
    </article>

    <c:if test="${sessionScope.usuarioLogado.perfil == 'SOLICITANTE'}">
        <article class="ticket">
            <div class="ticket__corpo">
                <svg class="ticket__icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                    <circle cx="12" cy="12" r="9"/><path d="M12 8v8M8 12h8"/>
                </svg>
                <h2>Nova solicitação</h2>
                <p>Algo não está funcionando? Abra um chamado de TI.</p>
            </div>
            <a class="ticket__canhoto" href="${ctx}/solicitacoes/nova">Abrir chamado</a>
        </article>
    </c:if>

    <c:if test="${sessionScope.usuarioLogado.perfil == 'ADMIN'}">
        <article class="ticket">
            <div class="ticket__corpo">
                <svg class="ticket__icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.9M16 3.1a4 4 0 0 1 0 7.8"/>
                </svg>
                <h2>Usuários</h2>
                <p>Cadastre pessoas e ative ou desative acessos.</p>
            </div>
            <a class="ticket__canhoto" href="${ctx}/usuarios">Gerenciar usuários</a>
        </article>
    </c:if>

</div>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
