<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<!doctype html>
<html class="h-100">

<head>
    <meta charset="utf-8">
    <title>Futbolín / ${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/4.4.1/css/bootstrap.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/sticky-footer.css" rel="stylesheet">
</head>

<body class="d-flex flex-column h-100">
<header>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/app/home">Futbolín</a>
        </div>
    </nav>
</header>

<main role="main" class="flex-shrink-0">
    <div class="container">
        <jsp:doBody />
    </div>
</main>

<footer class="footer py-3 mt-auto">
    <div class="container">
        <span class="text-muted">
            &copy; 2019 &mdash; 2020 Maarten Mulders //
            <a href="https://github.com/mthmulders/futbolin" target="_blank">Source code</a>
        </span>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/webjars/jquery/3.0.0/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/popper.js/1.14.3/popper.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/bootstrap/4.4.1/js/bootstrap.min.js"></script>
</body>

</html>