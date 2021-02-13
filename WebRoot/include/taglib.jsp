<%
    String path = request.getContextPath();
    String basePath = "//"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
    basePath = basePath + "";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="system" tagdir="/WEB-INF/tags/system" %>