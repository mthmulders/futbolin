<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="functions" tagdir="/WEB-INF/tags/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<!doctype html>
<html class="h-100">

<head>
    <meta charset="utf-8">
    <title>Futbolín / ${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/5.0.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap-icons/1.5.0/font/bootstrap-icons.css">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon/favicon.ico">
    <%-- Apple iOS devices and Google Android Devices--%>
    <link rel="apple-touch-icon-precomposed" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-152-183228.png">
    <%-- IE 10 Metro Tile Icon --%>
    <meta name="msapplication-TileColor" content="#ffffff" />
    <meta name="msapplication-TileImage" content="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-144-183228.png">
    <%-- Everything Else --%>
    <link rel="apple-touch-icon-precomposed" sizes="152x152" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-152-183228.png">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-144-183228.png">
    <link rel="apple-touch-icon-precomposed" sizes="120x120" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-120-183228.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-114-183228.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-72-183228.png">
    <link rel="apple-touch-icon-precomposed" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-57-183228.png">
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon/football-soccer-ball-32-183228.png" sizes="32x32">
</head>

<body class="flex-column d-flex h-100 bg-light">

    <div class="container">

        <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">

            <a class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none" href="${pageContext.request.contextPath}">
                <span class="fs-4">Futbolín</span>
            </a>

            <ul class="nav nav-pills">

                <c:if test="${pageContext.request.userPrincipal != null}">
                <functions:menu-item icon="speedometer" target="HomeController#show" title="Dashboard" />

                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="user-dropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        <svg class="bi d-block mx-auto mb-1" width="24" height="24" fill="currentColor">
                            <use xlink:href="${pageContext.request.contextPath}/webjars/bootstrap-icons/1.5.0/bootstrap-icons.svg#person-circle"></use>
                        </svg>
                        ${pageContext.request.userPrincipal.name}
                    </a>

                    <ul class="dropdown-menu" aria-labelledby="user-dropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/app/logout">Log off</a></li>
                    </ul>
                </li>
                </c:if>

            </ul>

        </header>

    </div>

    <main role="main" class="container flex-shrink-0">
        <jsp:doBody />
    </main>

    <footer class="footer mt-auto py-3 bg-gradient border-top">
        <div class="container">
            <span class="text-muted">
                &copy; 2019 &mdash; 2021 Maarten Mulders &mdash;
                <a href="https://github.com/mthmulders/futbolin" target="_blank">Source code</a> &mdash;
                <a href="/app/static/privacy">Privacy Policy</a>
            </span>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/webjars/bootstrap/5.0.1/js/bootstrap.bundle.min.js"></script>
</body>

</html>
