<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<!doctype html>
<html class="h-100">

<head>
    <meta charset="utf-8">
    <title>Futbolín / ${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/4.4.1-1/css/bootstrap.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sticky-footer.css" rel="stylesheet">
</head>

<body class="d-flex flex-column h-100">
<header>
    <div class="container">
        <nav class="navbar navbar-expand-md navbar-light fixed-navbar bg-light rounded">
            <a class="navbar-brand" href="/">Futbolín</a>
            <div class="collapse navbar-collapse justify-content-md-center">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active"><a class="nav-link" href="/app/home">Dashboard</a></li>
                </ul>

                <c:if test="${pageContext.request.userPrincipal != null}">
                    <div class="md-0">
                        <div class="dropdown">
                            <button class="btn btn-secondary dropdown-toggle" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="userDropdownMenuButton">
                                ${pageContext.request.userPrincipal.name}
                            </button>
                            <div class="dropdown-menu" aria-labelledby="userDropdownMenuButton">
                                <a class="dropdown-item" href="/app/logout">Log off</a>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </nav>
    </div>
</header>

<main role="main" class="flex-shrink-0">
    <div class="container">
        <jsp:doBody />
    </div>
</main>

<footer class="container footer py-3 mt-auto">
    <div class="text-muted">
        &copy; 2019 &mdash; 2020 Maarten Mulders &mdash;
        <a href="https://github.com/mthmulders/futbolin" target="_blank">Source code</a> &mdash;
        <a href="/app/static/privacy">Privacy Policy</a>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/webjars/jquery/3.4.0/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/popper.js/1.14.3/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/bootstrap/4.4.1-1/js/bootstrap.min.js"></script>
</body>

</html>