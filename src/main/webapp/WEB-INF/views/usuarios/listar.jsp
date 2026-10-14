<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Usuários"/>
<c:set var="menuAtivo" value="usuarios"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Usuários</h1>
        <p>Quem pode entrar no sistema. Usuários desativados não conseguem fazer login, mas os chamados deles continuam no histórico.</p>
    </div>
    <a class="btn" href="${ctx}/usuarios/nova">Novo usuário</a>
</div>

<c:choose>
    <c:when test="${empty usuarios}">
        <div class="vazio">
            <h2>Nenhum usuário cadastrado</h2>
            <p>Cadastre a primeira pessoa para liberar o acesso.</p>
            <a class="btn" href="${ctx}/usuarios/nova">Criar usuário</a>
        </div>
    </c:when>

    <c:otherwise>
        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                    <tr>
                        <th scope="col">Nome</th>
                        <th scope="col">E-mail</th>
                        <th scope="col">Perfil</th>
                        <th scope="col">Situação</th>
                        <th scope="col"><span class="sr-only">Ações</span></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${usuarios}">
                        <tr class="${u.ativo ? '' : 'is-inativa'}">
                            <td class="tabela__nome">
                                <c:out value="${u.nome}"/>
                                <c:if test="${u.id == sessionScope.usuarioLogado.id}"><span class="tabela__sub">Você</span></c:if>
                            </td>
                            <td data-rotulo="E-mail"><c:out value="${u.email}"/></td>
                            <td data-rotulo="Perfil"><span class="pill pill--perfil-${fn:toLowerCase(u.perfil)}"><c:out value="${u.perfilRotulo}"/></span></td>
                            <td data-rotulo="Situação">
                                <c:choose>
                                    <c:when test="${u.ativo}"><span class="pill pill--ativa">Ativo</span></c:when>
                                    <c:otherwise><span class="pill pill--inativa">Inativo</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="tabela__acoes">
                                <a class="btn btn--suave btn--pequeno" href="${ctx}/usuarios/editar?id=${u.id}">Editar</a>
                                <c:if test="${u.id != sessionScope.usuarioLogado.id}">
                                    <form action="${ctx}/usuarios/situacao" method="post">
                                        <input type="hidden" name="id" value="${u.id}">
                                        <c:choose>
                                            <c:when test="${u.ativo}">
                                                <input type="hidden" name="ativo" value="0">
                                                <button type="submit" class="link-acao link-acao--perigo"
                                                        onclick="return confirm('Desativar este usuário? Ele não poderá mais entrar.');">Desativar</button>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="hidden" name="ativo" value="1">
                                                <button type="submit" class="link-acao">Reativar</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
