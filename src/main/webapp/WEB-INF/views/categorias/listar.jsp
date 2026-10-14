<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Categorias"/>
<c:set var="menuAtivo" value="categorias"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Categorias</h1>
        <p>Cada chamado pertence a uma categoria. Categorias desativadas deixam de ser oferecidas, mas podem ser reativadas.</p>
    </div>
    <a class="btn" href="${ctx}/categorias/nova">Nova categoria</a>
</div>

<c:choose>
    <c:when test="${empty categorias}">
        <div class="vazio">
            <h2>Nenhuma categoria cadastrada</h2>
            <p>Crie a primeira para poder classificar os chamados.</p>
            <a class="btn" href="${ctx}/categorias/nova">Criar categoria</a>
        </div>
    </c:when>

    <c:otherwise>
        <div class="tabela-wrap">
            <table class="tabela">
                <thead>
                    <tr>
                        <th scope="col">Nome</th>
                        <th scope="col">Descrição</th>
                        <th scope="col">Situação</th>
                        <th scope="col"><span class="sr-only">Ações</span></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cat" items="${categorias}">
                        <tr class="${cat.ativo ? '' : 'is-inativa'}">
                            <td class="tabela__nome"><c:out value="${cat.nome}"/></td>
                            <td class="tabela__desc" data-rotulo="Descrição"><c:out value="${cat.descricao}"/></td>
                            <td data-rotulo="Situação">
                                <c:choose>
                                    <c:when test="${cat.ativo}"><span class="pill pill--ativa">Ativa</span></c:when>
                                    <c:otherwise><span class="pill pill--inativa">Inativa</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td class="tabela__acoes">
                                <a class="btn btn--suave btn--pequeno" href="${ctx}/categorias/editar?id=${cat.id}">Editar</a>
                                <c:choose>
                                    <c:when test="${cat.ativo}">
                                        <a class="link-acao link-acao--perigo"
                                           href="${ctx}/categorias/excluir?id=${cat.id}"
                                           onclick="return confirm('Desativar esta categoria? Você pode reativá-la depois.');">Desativar</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="link-acao" href="${ctx}/categorias/reativar?id=${cat.id}">Reativar</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
