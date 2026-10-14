<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="editando" value="${usuarioForm.id > 0}"/>
<c:set var="paginaTitulo" value="${editando ? 'Editar usuário' : 'Novo usuário'}"/>
<c:set var="menuAtivo" value="usuarios"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<a class="voltar" href="${ctx}/usuarios">Voltar para usuários</a>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1><c:out value="${paginaTitulo}"/></h1>
    </div>
</div>

<form class="form-cartao" action="${ctx}/usuarios/salvar" method="post">

    <c:if test="${not empty erro}">
        <p class="aviso" role="alert"><c:out value="${erro}"/></p>
    </c:if>

    <c:if test="${editando}"><input type="hidden" name="id" value="${usuarioForm.id}"></c:if>

    <div class="campo">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" maxlength="100" value="<c:out value='${usuarioForm.nome}'/>" required autofocus>
    </div>

    <div class="campo">
        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" maxlength="150" value="<c:out value='${usuarioForm.email}'/>" autocomplete="off" required>
    </div>

    <div class="campo">
        <label for="perfil">Perfil</label>
        <select id="perfil" name="perfil" required aria-describedby="dica-perfil">
            <option value="SOLICITANTE" ${usuarioForm.perfil == 'SOLICITANTE' || empty usuarioForm.perfil ? 'selected' : ''}>Solicitante</option>
            <option value="TECNICO" ${usuarioForm.perfil == 'TECNICO' ? 'selected' : ''}>Técnico</option>
            <option value="ADMIN" ${usuarioForm.perfil == 'ADMIN' ? 'selected' : ''}>Administrador</option>
        </select>
        <span class="dica" id="dica-perfil">Solicitante abre chamados; técnico atende; administrador gerencia tudo.</span>
    </div>

    <div class="campo">
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" maxlength="255" autocomplete="new-password"
               ${editando ? '' : 'required'} aria-describedby="dica-senha">
        <span class="dica" id="dica-senha">
            <c:choose>
                <c:when test="${editando}">Deixe em branco para manter a senha atual.</c:when>
                <c:otherwise>Informe a senha inicial do usuário.</c:otherwise>
            </c:choose>
        </span>
    </div>

    <div class="form-acoes">
        <button type="submit" class="btn">${editando ? 'Salvar alterações' : 'Salvar usuário'}</button>
        <a class="link-acao" href="${ctx}/usuarios">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
