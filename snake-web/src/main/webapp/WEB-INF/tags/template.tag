<%@ tag description="General Page Template" pageEncoding="UTF-8" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="subtitle" required="true" type="java.lang.String" %>

<html>
    <head>
        <title>${title}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
    </head>

    <body>
        <div class="container text-center">
            <h1>The Snake Strikes Back </h1>
            <h2>${subtitle}</h2>

            <jsp:doBody/>
        </div>
    </body>
</html>