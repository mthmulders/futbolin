<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="icon" required="true" type="java.lang.String" %>
<%@ attribute name="target" required="true" type="java.lang.String" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<li class="nav-item">
    <a class="nav-link ${mvcHelper.applicationPath eq mvc.uri(target) ? 'active' : ''}" href="${mvc.uri(target)}">
        <svg class="bi d-block mx-auto mb-1" width="24" height="24" fill="currentColor">
            <use xlink:href="${pageContext.request.contextPath}/webjars/bootstrap-icons/1.4.1/bootstrap-icons.svg#${icon}"></use>
        </svg>
        ${title}
    </a>
</li>