<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:main title="Home">
    <div class="row">
        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
            <h1>Welcome to Futbolín</h1>
            <p class="lead">Some introductory text</p>
        </div>
    </div>
    <div class="row">
        <div class="col-xl-4 col-lg-4 col-md-4">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title">My Teams</h2>
                    <c:choose>
                        <c:when test="${teams.size() > 0}">
                            <p class="card-text">You are a member of these teams:</p>
                            <ul>
                                <c:forEach items="${teams}" var="team">
                                    <li>${team}</li>
                                </c:forEach>
                            </ul>
                            <a href="#" class="btn btn-primary">See all</a>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-primary" role="alert">
                                You are not a member of any team.
                            </div>
                            <a href="#" class="btn btn-primary">Find a team</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-xl-4 col-lg-4 col-md-4">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title">My Tournaments</h2>
                    <c:choose>
                        <c:when test="${tournaments.size() > 0}">
                            <p class="card-text">You are participating in these tournaments:</p>
                            <ul>
                                <c:forEach items="${tournaments}" var="tournament">
                                    <li>${tournament}</li>
                                </c:forEach>
                            </ul>
                            <a href="#" class="btn btn-primary">See all</a>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-primary" role="alert">
                                You are not participating in any tournaments.
                            </div>
                            <a href="#" class="btn btn-primary">Find a tournament</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-xl-4 col-lg-4 col-md-4">
            <div class="card">
                <div class="card-body">
                    <h2 class="card-title">My Matches</h2>
                    <c:choose>
                        <c:when test="${matches.size() > 0}">
                            <p class="card-text">You have the following upcoming matches:</p>
                            <ul>
                                <c:forEach items="${matches}" var="match">
                                    <li>${match}</li>
                                </c:forEach>
                            </ul>
                            <a href="#" class="btn btn-primary">See all</a>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-primary" role="alert">
                                You do not have any upcoming matches.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</layout:main>
