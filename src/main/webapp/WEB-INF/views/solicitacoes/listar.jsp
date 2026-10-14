<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Solicitações"/>
<c:set var="menuAtivo" value="solicitacoes"/>
<c:set var="perfil" value="${sessionScope.usuarioLogado.perfil}"/>
<c:set var="sufixoMeus" value="${filtroMeus ? '&amp;meus=1' : ''}"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Solicitações</h1>
        <p>
            <c:choose>
                <c:when test="${perfil == 'SOLICITANTE'}">Acompanhe os chamados que você abriu.</c:when>
                <c:otherwise>Todos os chamados abertos na instituição.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <c:if test="${perfil == 'SOLICITANTE'}">
        <a class="btn" href="${ctx}/solicitacoes/nova">Nova solicitação</a>
    </c:if>
</div>

<nav class="filtros" aria-label="Filtrar por situação">
    <a class="filtro ${empty filtroStatus ? 'is-ativo' : ''}" href="${ctx}/solicitacoes${filtroMeus ? '?meus=1' : ''}">Todas</a>
    <a class="filtro ${filtroStatus == 'ABERTA' ? 'is-ativo' : ''}" href="${ctx}/solicitacoes?status=ABERTA${sufixoMeus}">Abertas</a>
    <a class="filtro ${filtroStatus == 'EM_ATENDIMENTO' ? 'is-ativo' : ''}" href="${ctx}/solicitacoes?status=EM_ATENDIMENTO${sufixoMeus}">Em atendimento</a>
    <a class="filtro ${filtroStatus == 'RESOLVIDA' ? 'is-ativo' : ''}" href="${ctx}/solicitacoes?status=RESOLVIDA${sufixoMeus}">Resolvidas</a>
    <a class="filtro ${filtroStatus == 'FECHADA' ? 'is-ativo' : ''}" href="${ctx}/solicitacoes?status=FECHADA${sufixoMeus}">Fechadas</a>
    <a class="filtro ${filtroStatus == 'CANCELADA' ? 'is-ativo' : ''}" href="${ctx}/solicitacoes?status=CANCELADA${sufixoMeus}">Canceladas</a>

    <c:if test="${perfil == 'TECNICO'}">
        <span class="filtros__sep" aria-hidden="true"></span>
        <c:choose>
            <c:when test="${filtroMeus}">
                <a class="filtro is-ativo" href="${ctx}/solicitacoes${not empty filtroStatus ? '?status=' : ''}${filtroStatus}">Atribuídas a mim</a>
            </c:when>
            <c:otherwise>
                <a class="filtro" href="${ctx}/solicitacoes?meus=1${not empty filtroStatus ? '&amp;status=' : ''}${filtroStatus}">Atribuídas a mim</a>
            </c:otherwise>
        </c:choose>
    </c:if>
</nav>

<c:choose>
    <c:when test="${empty solicitacoes}">
        <div class="vazio">
            <c:choose>
                <c:when test="${not empty filtroStatus or filtroMeus}">
                    <h2>Nenhum chamado com esse filtro</h2>
                    <p>Tente outra situação ou veja a lista completa.</p>
                    <a class="btn btn--suave" href="${ctx}/solicitacoes">Ver todas</a>
                </c:when>
                <c:when test="${perfil == 'SOLICITANTE'}">
                    <h2>Você ainda não abriu nenhum chamado</h2>
                    <p>Descreva o problema e acompanhe o atendimento por aqui.</p>
                    <a class="btn" href="${ctx}/solicitacoes/nova">Abrir chamado</a>
                </c:when>
                <c:otherwise>
                    <h2>Nenhum chamado por enquanto</h2>
                    <p>Quando alguém abrir um chamado, ele aparece aqui.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>

    <c:otherwise>
        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                    <tr>
                        <th scope="col">Chamado</th>
                        <th scope="col">Prioridade</th>
                        <th scope="col">Situação</th>
                        <c:if test="${perfil != 'SOLICITANTE'}"><th scope="col">Solicitante</th></c:if>
                        <th scope="col">Técnico</th>
                        <th scope="col">Aberto em</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${solicitacoes}">
                        <tr>
                            <td class="tabela__nome">
                                <a class="tabela__link" href="${ctx}/solicitacoes/ver?id=${s.id}"><c:out value="${s.titulo}"/></a>
                                <span class="tabela__sub">#<c:out value="${s.id}"/> em <c:out value="${s.categoria_nome}"/></span>
                            </td>
                            <td data-rotulo="Prioridade"><span class="pill pill--prio-${s.prioridadeClasse}"><c:out value="${s.prioridadeRotulo}"/></span></td>
                            <td data-rotulo="Situação"><span class="pill pill--status-${s.statusClasse}"><c:out value="${s.statusRotulo}"/></span></td>
                            <c:if test="${perfil != 'SOLICITANTE'}">
                                <td data-rotulo="Solicitante"><c:out value="${s.usuario_nome}"/></td>
                            </c:if>
                            <td data-rotulo="Técnico">
                                <c:choose>
                                    <c:when test="${empty s.tecnico_nome}"><span class="suave">Sem técnico</span></c:when>
                                    <c:otherwise><c:out value="${s.tecnico_nome}"/></c:otherwise>
                                </c:choose>
                            </td>
                            <td data-rotulo="Aberto em"><fmt:formatDate value="${s.data_abertura}" pattern="dd/MM/yyyy HH:mm"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
