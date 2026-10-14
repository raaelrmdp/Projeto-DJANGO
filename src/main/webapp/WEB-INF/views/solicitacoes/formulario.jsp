<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Nova solicitação"/>
<c:set var="menuAtivo" value="solicitacoes"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<a class="voltar" href="${ctx}/solicitacoes">Voltar para solicitações</a>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1>Nova solicitação</h1>
        <p>Quanto mais claro o relato, mais rápido o atendimento.</p>
    </div>
</div>

<c:choose>
<c:when test="${empty categorias}">
    <div class="vazio">
        <h2>Ainda não há categorias disponíveis</h2>
        <p>Para abrir um chamado é preciso ter ao menos uma categoria ativa. Peça ao administrador para cadastrar.</p>
    </div>
</c:when>
<c:otherwise>

<form class="form-cartao form-cartao--largo" action="${ctx}/solicitacoes/salvar" method="post">

    <c:if test="${not empty erro}">
        <p class="aviso" role="alert"><c:out value="${erro}"/></p>
    </c:if>

    <div class="campo">
        <label for="titulo">Título</label>
        <input type="text" id="titulo" name="titulo" maxlength="150" value="<c:out value='${solicitacao.titulo}'/>" required autofocus>
    </div>

    <div class="campo-linha">
        <div class="campo">
            <label for="categoria">Categoria</label>
            <select id="categoria" name="categoria" required>
                <option value="">Escolha uma categoria</option>
                <c:forEach var="cat" items="${categorias}">
                    <option value="${cat.id}" ${cat.id == solicitacao.categoria_id ? 'selected' : ''}><c:out value="${cat.nome}"/></option>
                </c:forEach>
            </select>
        </div>

        <div class="campo">
            <label for="prioridade">Prioridade</label>
            <select id="prioridade" name="prioridade" required>
                <option value="BAIXA"   ${solicitacao.prioridade == 'BAIXA'   ? 'selected' : ''}>Baixa</option>
                <option value="MEDIA"   ${solicitacao.prioridade == 'MEDIA'   ? 'selected' : ''}>Média</option>
                <option value="ALTA"    ${solicitacao.prioridade == 'ALTA'    ? 'selected' : ''}>Alta</option>
                <option value="URGENTE" ${solicitacao.prioridade == 'URGENTE' ? 'selected' : ''}>Urgente</option>
            </select>
        </div>
    </div>

    <div class="campo">
        <label for="local">Local</label>
        <input type="text" id="local" name="local" maxlength="150" value="<c:out value='${solicitacao.local_ocorrencia}'/>" aria-describedby="dica-local">
        <span class="dica" id="dica-local">Opcional. Sala, laboratório ou setor onde o problema acontece.</span>
    </div>

    <div class="campo">
        <label for="descricao">Descrição</label>
        <textarea id="descricao" name="descricao" rows="6" maxlength="5000" required><c:out value="${solicitacao.descricao}"/></textarea>
    </div>

    <div class="form-acoes">
        <button type="submit" class="btn">Abrir chamado</button>
        <a class="link-acao" href="${ctx}/solicitacoes">Cancelar</a>
    </div>
</form>

</c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
