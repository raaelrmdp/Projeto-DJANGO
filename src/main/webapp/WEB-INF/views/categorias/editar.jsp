<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Editar categoria"/>
<c:set var="menuAtivo" value="categorias"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<a class="voltar" href="${ctx}/categorias">Voltar para categorias</a>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Editar categoria</h1>
    </div>
</div>

<form class="form-cartao" action="${ctx}/categorias/atualizar" method="post">

    <input type="hidden" name="id" value="${categoria.id}">

    <div class="campo">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" maxlength="100" value="<c:out value='${categoria.nome}'/>" required autofocus>
    </div>

    <div class="campo">
        <label for="descricao">Descrição</label>
        <input type="text" id="descricao" name="descricao" maxlength="255" value="<c:out value='${categoria.descricao}'/>" aria-describedby="dica-descricao">
        <span class="dica" id="dica-descricao">Opcional. Ajuda a escolher a categoria certa ao abrir um chamado.</span>
    </div>

    <div class="form-acoes">
        <button type="submit" class="btn">Salvar alterações</button>
        <a class="link-acao" href="${ctx}/categorias">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
