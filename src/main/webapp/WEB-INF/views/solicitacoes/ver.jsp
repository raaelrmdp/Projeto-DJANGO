<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="paginaTitulo" value="Chamado ${solicitacao.id}"/>
<c:set var="menuAtivo" value="solicitacoes"/>
<%@ include file="/WEB-INF/views/includes/cabecalho.jspf" %>

<a class="voltar" href="${ctx}/solicitacoes">Voltar para solicitações</a>

<div class="pagina-topo">
    <div class="pagina-topo__texto">
        <h1><c:out value="${solicitacao.titulo}"/></h1>
        <p>Chamado #<c:out value="${solicitacao.id}"/>, aberto por <c:out value="${solicitacao.usuario_nome}"/> em <fmt:formatDate value="${solicitacao.data_abertura}" pattern="dd/MM/yyyy 'às' HH:mm"/></p>
    </div>
    <div class="pagina-topo__selos">
        <span class="pill pill--status-${solicitacao.statusClasse}"><c:out value="${solicitacao.statusRotulo}"/></span>
        <span class="pill pill--prio-${solicitacao.prioridadeClasse}">Prioridade <c:out value="${solicitacao.prioridadeRotulo}"/></span>
    </div>
</div>

<div class="chamado">

    <div class="chamado__principal">

        <section class="cartao">
            <h2>Descrição</h2>
            <p class="texto-corrido"><c:out value="${solicitacao.descricao}"/></p>
        </section>

        <section class="cartao" id="conversa">
            <h2>Conversa</h2>

            <c:choose>
                <c:when test="${empty interacoes}">
                    <p class="suave">Nenhuma mensagem ainda.</p>
                </c:when>
                <c:otherwise>
                    <ol class="conversa">
                        <c:forEach var="i" items="${interacoes}">
                            <li class="mensagem ${i.usuario_id == sessionScope.usuarioLogado.id ? 'mensagem--minha' : ''}">
                                <div class="mensagem__topo">
                                    <strong><c:out value="${i.usuario_nome}"/></strong>
                                    <span class="mensagem__perfil"><c:out value="${i.perfilRotulo}"/></span>
                                    <time class="mensagem__hora"><fmt:formatDate value="${i.data_interacao}" pattern="dd/MM/yyyy HH:mm"/></time>
                                </div>
                                <p class="texto-corrido"><c:out value="${i.mensagem}"/></p>
                            </li>
                        </c:forEach>
                    </ol>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${podeInteragir}">
                    <form class="conversa__form" action="${ctx}/solicitacoes/interagir" method="post">
                        <input type="hidden" name="id" value="${solicitacao.id}">
                        <div class="campo">
                            <label for="mensagem">Nova mensagem</label>
                            <textarea id="mensagem" name="mensagem" rows="3" maxlength="2000" required></textarea>
                        </div>
                        <button type="submit" class="btn">Enviar mensagem</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p class="aviso aviso--neutro">Este chamado está encerrado e não aceita novas mensagens.</p>
                </c:otherwise>
            </c:choose>
        </section>
    </div>

    <aside class="chamado__lateral">

        <section class="cartao">
            <h2>Detalhes</h2>
            <dl class="detalhes">
                <div><dt>Categoria</dt><dd><c:out value="${solicitacao.categoria_nome}"/></dd></div>
                <div>
                    <dt>Local</dt>
                    <dd>
                        <c:choose>
                            <c:when test="${empty solicitacao.local_ocorrencia}"><span class="suave">Não informado</span></c:when>
                            <c:otherwise><c:out value="${solicitacao.local_ocorrencia}"/></c:otherwise>
                        </c:choose>
                    </dd>
                </div>
                <div><dt>Solicitante</dt><dd><c:out value="${solicitacao.usuario_nome}"/></dd></div>
                <div>
                    <dt>Técnico</dt>
                    <dd>
                        <c:choose>
                            <c:when test="${empty solicitacao.tecnico_nome}"><span class="suave">Sem técnico</span></c:when>
                            <c:otherwise><c:out value="${solicitacao.tecnico_nome}"/></c:otherwise>
                        </c:choose>
                    </dd>
                </div>
                <div><dt>Aberto em</dt><dd><fmt:formatDate value="${solicitacao.data_abertura}" pattern="dd/MM/yyyy HH:mm"/></dd></div>
                <c:if test="${not empty solicitacao.data_fechamento}">
                    <div><dt>Encerrado em</dt><dd><fmt:formatDate value="${solicitacao.data_fechamento}" pattern="dd/MM/yyyy HH:mm"/></dd></div>
                </c:if>
            </dl>
        </section>

        <c:if test="${acoes.assumir or acoes.atribuir or acoes.resolver or acoes.fechar or acoes.reabrir or acoes.cancelar}">
            <section class="cartao">
                <h2>Ações</h2>

                <c:if test="${acoes.fechar or acoes.reabrir}">
                    <p class="suave acoes__texto">Este chamado foi marcado como resolvido. O problema acabou?</p>
                </c:if>

                <div class="acoes">
                    <c:if test="${acoes.assumir}">
                        <form action="${ctx}/solicitacoes/acao" method="post">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="assumir">
                            <button type="submit" class="btn">Assumir chamado</button>
                        </form>
                    </c:if>

                    <c:if test="${acoes.resolver}">
                        <form action="${ctx}/solicitacoes/acao" method="post">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="resolver">
                            <button type="submit" class="btn">Marcar como resolvido</button>
                        </form>
                    </c:if>

                    <c:if test="${acoes.fechar}">
                        <form action="${ctx}/solicitacoes/acao" method="post">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="fechar">
                            <button type="submit" class="btn">Sim, fechar chamado</button>
                        </form>
                    </c:if>

                    <c:if test="${acoes.reabrir}">
                        <form action="${ctx}/solicitacoes/acao" method="post">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="reabrir">
                            <button type="submit" class="btn btn--suave">Não, reabrir</button>
                        </form>
                    </c:if>

                    <c:if test="${acoes.atribuir}">
                        <form class="acoes__atribuir" action="${ctx}/solicitacoes/acao" method="post">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="atribuir">
                            <div class="campo">
                                <label for="tecnico">Atribuir a</label>
                                <select id="tecnico" name="tecnico" required>
                                    <option value="">Escolha quem vai atender</option>
                                    <c:forEach var="a" items="${atendentes}">
                                        <option value="${a.id}" ${a.id == solicitacao.tecnico_id ? 'selected' : ''}><c:out value="${a.nome}"/> (<c:out value="${a.perfilRotulo}"/>)</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" class="btn btn--suave">Atribuir</button>
                        </form>
                    </c:if>

                    <c:if test="${acoes.cancelar}">
                        <form action="${ctx}/solicitacoes/acao" method="post"
                              onsubmit="return confirm('Cancelar este chamado? Essa ação não pode ser desfeita.');">
                            <input type="hidden" name="id" value="${solicitacao.id}">
                            <input type="hidden" name="acao" value="cancelar">
                            <button type="submit" class="link-acao link-acao--perigo">Cancelar chamado</button>
                        </form>
                    </c:if>
                </div>
            </section>
        </c:if>

    </aside>
</div>

<%@ include file="/WEB-INF/views/includes/rodape.jspf" %>
