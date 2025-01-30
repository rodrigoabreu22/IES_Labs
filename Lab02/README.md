# Apontamentos Lab2

# Jetty Server
To use a web server from within my app I should use the Jetty Server. Here's an example: 

```
package org.ies.deti.ua;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class EmbeddedJettyExample {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8680);

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(HelloServlet.class, "/");

        server.start();
        server.join();

    }

    public static class HelloServlet extends HttpServlet
    {
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);

            //Take a look here
            String msg = request.getParameter("msg");

            if (msg != null){
                response.getWriter().printf("<h1>%s</h1>", msg);
            }
            else{
                response.getWriter().println("<h1>New Hello Simple Servlet</h1>");
            }

        }
    }
}
```
Este código dá run a um server no porto 8680, e tem um ServletHandler que trata dos pedidos ao servidor. 
Neste preciso exemplo, se eu usar o url "http://localhost:8680/" uma mensagem default irá ser apresentada na página web. Mas se usar "http://localhost:8680/?msg="{msg}"", o texto que estiver em msg será apresentado.

Desta forma podemos acessar os parâmetros do request:
```
String msg = request.getParameter("msg");
```
Um bom exemplo:

![image](https://github.com/user-attachments/assets/1421f398-985f-4721-8ab3-6cb73c777659)


# Server-side programming and application servers (Tomcat)
Creating a new Jakarta EE application, based on the Web profile. 
![image](https://github.com/user-attachments/assets/ecd27fd6-e3f4-4476-8746-dade582a8491)
> This project will not run in a normal way.

We need to create the docker-compose.yaml file to run the program.
```
version: '3.8'
services:
  tomcat-10-0-11-jdk17:
    image: tomcat:10.0-jdk17
    ports:
      # Expose Tomcat port 8080 (container) on host as port 8888 (host)
      - "8888:8080"
      # Expose Java debugging port 5005 on host as port 5005 (HOST:CONTAINER)
      - "5005:5005"
    volumes:
      # Ensure the WAR file is correctly mapped
      - "./target/:/usr/local/tomcat/webapps"
    environment:
      JAVA_OPTS: "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```
And insert the following commands in the terminal:
```
$ docker clean package
$ docker compose up
```
Than we should insert the following url on the browser to access the web page http://localhost:8888/ies.JakartaWebStarter-1.0-SNAPSHOT/
> this url will provide an error page : http://localhost:8888/

No ex2 deste guião, conseguiamos passar o parâmetro "msg" com uma mensagem para ser apresentada na página, desta forma:
http://localhost:8888/ies.JakartaWebStarter-1.0-SNAPSHOT/hello-servlet?msg="{mensagem}"


# Spring Boot
Spring Boot is useful to get started with minimum effort and create stand-alone, production-grade
applications.

We should use this to create Spring Boot projects for web apps.
> don't forget to add “Spring web” dependency.

How to run:
```
$ mvn install -DskipTests && java -jar target\webapp1-0.0.1-SNAPSHOT.jar
or
$ ./mvnw spring-boot:run
```
> the app will run by default in  http://localhost:8080/ and will display a "white label error" if there is no index page. We should change the port to avoid conflicts in the file application.properties found in the resources page.

# Serving Web Content with Spring MVC
In this url there is a useful tutorial on how to get started with spring boot:
https://spring.io/guides/gs/serving-web-content#scratch

# Building a RESTful Web Service
In this url there is a useful tutorial on how to build a simple RESTful Web Service:
https://spring.io/guides/gs/rest-service

# JSON
Syntax:
- Data is in name/value pairs
- Data is separated by commas
- Curly braces hold objects
- Square brackets hold arrays

Example:
```
"employees":[
    {"firstName":"John", "lastName":"Doe"},
    {"firstName":"Anna", "lastName":"Smith"},
    {"firstName":"Peter", "lastName":"Jones"}
]
```

curl outputs the response body to standard output:
```
$ curl -v http://www.example.com/
```
We can provide the output option to save to a file:
```
curl -o out.json http://www.example.com/index.html
```






