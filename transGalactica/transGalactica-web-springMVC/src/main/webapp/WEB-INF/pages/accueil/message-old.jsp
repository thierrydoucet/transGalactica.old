
	<c:forEach var="message" items="${messageToList}">
		<h3>
			${message.titre}
			<c:if test="${ not empty message.datePublication }"> (<spring:message code="transgalactica.common.format.datetime" arguments="${message.datePublication}" />)</c:if>
		</h3>
		<div>
			<c:if test="${ not empty message.image.url }">
				<img style="float: left; margin: 5px;" src="${message.image.url}" alt="${message.image.texteAlternatif}" />
			</c:if>
			<wiki:wikiText privileges="ABSLINK">${message.contenu}</wiki:wikiText>
		</div>
		<div style="clear: both;"></div>
	</c:forEach>
