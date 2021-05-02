<%@ tag import="java.time.format.DateTimeFormatter"%>
<%@ tag import="java.time.format.FormatStyle"%>
<%@ tag import="java.util.Locale"%>
<%@ tag import="java.time.ZoneId"%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="arg" required="true" type="java.time.Instant" %>

<%
    out.print(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
            .format(arg));
%>