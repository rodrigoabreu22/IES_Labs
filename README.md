# IES_Individual
Template for individual scripts of IES

# Apontamentos Lab1

# Maven
Estrutura de um projeto Maven:
![image](https://github.com/user-attachments/assets/61e1957b-ef3d-450e-8e2a-696dd5819996)


Criar projeto Maven:
```
$ mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
definir corretamente o:
- groupid: Name of the company, organization, team etc., usually using the reverse URL naming convention
- artifactid: A unique name for the project under groupId

são definidos no ficheiro pom.xml: 

![image](https://github.com/user-attachments/assets/458e25e5-1fa4-4377-b2e0-9a71c83dfc8c)

"The POM contains information about the project and various configuration detail used by Maven to build the project. "

Como compilar e correr um um projeto Maven:
```
$ mvn package
$ mvn exec:java -Dexec.mainClass="{groupId.MainFuncionName}"
$ mvn exec:java -Dexec.mainClass="{groupId.MainFuncionName}" -Dexec.args="arg0 arg1 arg2"
```
Outros comandos úteis:
```
$ mvn clean
$ mvn clean install
```

# Sistema de Logging:
Posso usar como log a biblioteca Log4j2. Com o maven devo adicionar as dependencias ao pom.xml e criar um ficheiro log4j2.xml na pasta resoures.
```
<dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.6.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.6.1</version>
        </dependency>
```
```
<?xml version="1.0" encoding="UTF-8"?>

<Configuration status="debug" name="weatherlogging" packages="">
<!-- Logging Properties -->
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ} %p %m%n</Property>
        <Property name="LOG_ROOT">log</Property>
    </Properties>


    <Appenders>
        <File name="weatherinfo" fileName="${LOG_ROOT}/weather-info.log" append="false">
            <PatternLayout>
                <Pattern>${LOG_PATTERN}</Pattern>
            </PatternLayout>
        </File>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="weatherinfo"/>
        </Root>
    </Loggers>

</Configuration>
```

# Git 
Importante ferramenta de controlo de versões no desenvolvimento de projetos.
Comandos importantes:
```
$ git cclone
$ git pull
$ git push
$ git commit
$ git merge
$ git log --reverse --oneline
```

# Docker
Docker Desktop pode dar problemas em sitemas Linux.
"Docker lets you build, test, and deploy applications quickly
Using Docker, you can quickly deploy and scale applications into any environment and know your code will run."

Docker compose:

Dockerfile example
```
# syntax=docker/dockerfile:1
FROM python:3.10-alpine
WORKDIR /code
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
EXPOSE 5000
COPY . .
CMD ["flask", "run", "--debug"]
```
Explicação
This tells Docker to:

Build an image starting with the Python 3.10 image.
Set the working directory to /code.
Set environment variables used by the flask command.
Install gcc and other dependencies
Copy requirements.txt and install the Python dependencies.
Add metadata to the image to describe that the container is listening on port 5000
Copy the current directory . in the project to the workdir . in the image.
Set the default command for the container to flask run --debug.

.yaml configuration file 
```
services:
  web:
    build: .
    ports:
      - "8000:5000"
  redis:
    image: "redis:alpine"
```
Explicação:
- The web service uses an image that's built from the Dockerfile in the current directory. It then binds the container and the host machine to the exposed port, 8000.
- The redis service uses a public Redis image pulled from the Docker Hub registry.

To run we use:
```
$ docker compose up
```
To stop we use:
```
$ docker compose down
```

Command to check the images running in docker:
```
$ docker image ls
```

Para pré-visualizar os serviços Compose a correr, devemos usar Compose Watch, exemplo:
comm:
```
$ docker compose watch
```
```
services:
  web:
    build: .
    ports:
      - "8000:5000"
    develop:
      watch:
        - action: sync
          path: .
          target: /code
  redis:
    image: "redis:alpine"
```

We should use many compose files in large applications, example:
infra.yaml
```
services:
  redis:
    image: "redis:alpine"
```
compose.yaml we should add the include:
```
include:
   - infra.yaml
services:
  web:
    build: .
    ports:
      - "8000:5000"
    develop:
      watch:
        - action: sync
          path: .
          target: /code
```

comm to run docker in the background:
```
$ docker compose up -d
```
comm to stop the services once they are finished:
```
$ docker compose stop
```
comm to list the active containers:
```
$ docker ps
```

# Como usar o docker num projeto maven
Preciso de criar um Dockerfile na root e depois adicionar o seguinte plugin no ficheiro pom.xml
```
<plugin>
    <!-- Build an executable JAR -->
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <mainClass>{groupId.mainClass}</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```
e depois:
```
$ mvn clean package
```

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



# Apontamentos do Lab 3

# Accessing databases in SpringBoot

The Jakarta Persistence API (JPA) defines a standard interface to manage data over relational
databases; there are several frameworks that implement the JPA specification, such as the Hibernate
framework. JPA offers a specification for ORM (object-relational mapping).
Spring Data uses and enhances the JPA. When you use Spring Data your Java code is independent
from the specific database implementation1

> Usefull link about JPA: https://www.infoworld.com/article/2259807/what-is-jpa-introduction-to-the-java-persistence-api.html


# Create a SpringBoot project using Spring Data JPA

> Usefull tutorial link: https://www.baeldung.com/spring-boot-crud-thymeleaf

Spring Boot makes it easy to create CRUD applications through a layer of standard JPA-based CRUD repositories.
This is how to develop a CRUD web application with Spring Boot and Thymeleaf.

Start by going to Spring Initializer (https://start.spring.io/) and create a project with the following specifications:
![image](https://github.com/user-attachments/assets/a4a3d815-95b1-42c7-8f21-10f6fa63983e)
> Dica do Daniel: Use war for deployment, else we can use jar.

After generating the project the pom.xml file should have this:
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
</dependencies>
```

Now we start the implementation: 

- The Domain Layer

> This layer will include one single class that will be responsible for modeling User entities.

```
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity(name="tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    private String phone;

    // standard constructors / setters / getters / toString
```
> Let’s keep in mind that we’ve annotated the class with the @Entity annotation. Therefore, the JPA implementation, which is Hibernate, in this case, will be able to perform CRUD operations on the domain entities.

- The Repository Layer

Spring Data JPA allows us to implement JPA-based repositories (a fancy name for the DAO pattern implementation) with minimal fuss.
> DAO stands for Data Access Object. DAO Design Pattern is used to separate the data persistence logic in a separate layer. This way, the service remains completely in dark about how the low-level operations to access the database is done. This is known as the principle of Separation of Logic

```
@Repository
public interface UserRepository extends CrudRepository<User, Long> {}
```

- The Controller Layer
  
Thanks to the layer of abstraction that spring-boot-starter-data-jpa places on top of the underlying JPA implementation, we can easily add some CRUD functionality to our web application through a basic web tier.
In our case, a single controller class will suffice for handling GET and POST HTTP requests and then mapping them to calls to our UserRepository implementation.

```
@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        return "redirect:/index";
    }
```

- The View Layer

We need to add the interface and the forms that will be desplayed in the browser.

Add User Form
```
<form action="#" th:action="@{/adduser}" th:object="${user}" method="post">
    <label for="name">Name</label>
    <input type="text" th:field="*{name}" id="name" placeholder="Name">
    <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></span>
    <label for="email">Email</label>
    <input type="text" th:field="*{email}" id="email" placeholder="Email">
    <span th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></span>
    <label for="phone">Phone</label>
    <input type="text" th:field="*{phone}" id="phone" placeholder="Phone">
    <span th:if="${#fields.hasErrors('phone')}" th:errors="*{phone}"></span>

    <input type="submit" value="Add User">
</form>
```

> Notice how we’ve used the @{/adduser} URL expression to specify the form’s action attribute and the ${} variable expressions for embedding dynamic content in the template, such as the values of the name and email fields and the post-validation errors.

Update User Form
```
<form action="#"
      th:action="@{/update/{id}(id=${user.id})}"
      th:object="${user}"
      method="post">
    <label for="name">Name</label>
    <input type="text" th:field="*{name}" id="name" placeholder="Name">
    <span th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></span>
    <label for="email">Email</label>
    <input type="text" th:field="*{email}" id="email" placeholder="Email">
    <span th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></span>
    <label for="phone">Phone</label>
    <input type="text" th:field="*{phone}" id="phone" placeholder="Phone">
    <span th:if="${#fields.hasErrors('phone')}" th:errors="*{phone}"></span>
    <input type="submit" value="Update User">
</form>
```

Index
```
<div th:switch="${users}">
    <h2 th:case="null">No users yet!</h2>
    <div th:case="*">
        <h2>Users</h2>
        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="user : ${users}">
                <td th:text="${user.name}"></td>
                <td th:text="${user.email}"></td>
                <td th:text="${user.phone}"></td>
                <td><a th:href="@{/edit/{id}(id=${user.id})}">Edit</a></td>
                <td><a th:href="@{/delete/{id}(id=${user.id})}">Delete</a></td>
            </tr>
            </tbody>
        </table>
    </div>
    <p><a href="/signup">Add a new user</a></p>
</div>
```

# Multilayer applications: exposing data with REST interface

> Usefull tutorial: https://www.javaguides.net/2018/09/spring-boot-2-jpa-mysql-crud-example.html

We will need an instance of MySQL server (stick with version 5.7) to store Employee
information. This should be runned in a docker container with following command:
```
docker run --name mysql5 -e MYSQL_ROOT_PASSWORD=secret1 -e MYSQL_DATABASE=demo -
e MYSQL_USER=demo -e MYSQL_PASSWORD=secret2 -p 33060:3306 -d mysql/mysqlserver:5.7
```

Create the Spring Boot project with the respective dependencies:
![image](https://github.com/user-attachments/assets/2a899e6a-e25c-46b6-8a14-80b04a202813)

- Entity
- 
We should create an entity, this will be our Employee:
```
@Entity(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
   @Column(name="name",nullable = false)
    private String name;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    //generate getters and setters
}
```
- Repository

Than create our repository:
> A repository is an interface that defines the methods for performing CRUD operations on the Entity. Spring Data JPA will automatically create the implementation for the Repository interface.
> Note that we do not need to add @Repository annotation because Spring Data JPA internally takes care of it.

```
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Employee findByEmail(String email);
}
```

- Create Service Layer
This layer will contain the business logic for the API and will be used to perform CRUD operations using the Repository.

```
public interface EmployeeService {
    Employee createEmployee(Employee emp);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Employee emp);

    void deleteEmployee(Long id);

    Employee findByEmail(String email);
}
```

Now the implementation methods: 
```
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee emp) {
        return employeeRepository.save(emp);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.get();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee emp) {
        Employee existingEmp = employeeRepository.findById(emp.getId()).get();
        existingEmp.setName(emp.getName());
        existingEmp.setEmail(emp.getEmail());
        Employee updatedEmp = employeeRepository.save(existingEmp);
        return updatedEmp;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email);
    }
}
```

- Controller

We’ll now create the REST APIs for creating, retrieving, updating, and deleting a User resource.
> Usefull link for the Spring’s @RequestParam annotation and its attributes.
> This was usefull for Enhance my REST API with a method to search an employee by email (search by email).

```
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee emp){
        Employee savedEmp = employeeService.createEmployee(emp);
        return new ResponseEntity<>(savedEmp, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestParam(required=false) String email){
        List<Employee> employees;
        if (email==null){
            employees = employeeService.getAllEmployees();
        }
        else {
            employees = new ArrayList<>();
            employees.add(employeeService.findByEmail(email));
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id){
        Employee emp = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(emp, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long id,  @RequestBody Employee emp){
        emp.setId(id);
        Employee updatedEmp = employeeService.updateEmployee(emp);
        return new ResponseEntity<>(updatedEmp, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") long id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
```

Now we need to define the database connection properties on resources/application.properties
```
# MySQL
spring.datasource.url=jdbc:mysql://127.0.0.1:33060/demo
spring.datasource.username=demo
spring.datasource.password=secret2
spring.jpa.database-platform=org.hibernate.dialect.MySQL5InnoDBDialect
# Strategy to auto update the schemas (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto = update
```
Now we are ready to run with these commands:
```
$ mvn clean install
$ mvn spring-boot:run
```

To see test the CRUD operations we should use Postman Desktop, with the following methods.

1 - @GetMapping("") - get all the employees in the db. (if it has the following suffix "?email={email_address}" it will return the employee with this email or empty list)
2 - @PostMapping("") -  insert an employee in the db.
3 - @GetMapping("{id}") - returns an employee with a specific id.
4 - @PutMapping("{id}") - updates an employee with a specific id.
5 - @DeleteMapping("{id}") - deletes an employee with a specific id.

Here is some images of the tests I've executed.

> 2

![Captura de ecrã de 2024-10-25 02-13-33](https://github.com/user-attachments/assets/e2b08da5-42a1-45a9-a795-36349a0c265d)

> 1

![Captura de ecrã de 2024-10-25 02-15-00](https://github.com/user-attachments/assets/08df0f26-a7f2-49f6-b208-13e71585a418)
![Captura de ecrã de 2024-10-25 16-11-14](https://github.com/user-attachments/assets/234aa9a3-6e6a-4153-834b-b43c8dc3a401)

> 3

![image](https://github.com/user-attachments/assets/143add2e-f5f3-46a3-9864-63b3f6ac5259)

> 4

![Captura de ecrã de 2024-10-25 02-26-50](https://github.com/user-attachments/assets/8b50d101-33b9-4504-8973-31703d29d07e)

> 5

![image](https://github.com/user-attachments/assets/add0de78-5eb1-4744-aacd-e507a1fabc7f)


# Use a Service Layer
The RestController provides the wiring for
HTTP but requires the Service to answer all requests; the Service holds the business logic and
interacts with the Repository (or other components) as needed.

![image](https://github.com/user-attachments/assets/f268ca1a-28cc-442f-a8e8-18cb506a7f49)

> @Autowired is a widely used annotation in Spring Framework, specifically within Spring Boot, to enable dependency injection. It allows Spring to resolve and inject the dependent beans automatically into another class

```
    @Autowired
    private MovieService movieService;
    @Autowired
    private QuoteService quoteService;
```

# Dockerize a Spring Boot Application
> usefull link: https://spring.io/guides/gs/spring-boot-docker

First we need to run the command ./mvnw install to create the .jar file.

Then create a Dockerfile in the root directory of the project:
```
FROM openjdk:21-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Then build an run the docker image:
```
$ docker build -t shows .
$ docker run --network=host -p 8080:8080 shows
```
### Create a docker-compose.yml file
> We can also create a docker compose file to run the application and the database at the same time. Making it easier to run the application.
```
version: "3.8"

services:
  mysqldb:
    image: mysql:5.7
    restart: unless-stopped
    env_file: ./.env
    environment:
      - MYSQL_ROOT_PASSWORD=$MYSQLDB_ROOT_PASSWORD
      - MYSQL_DATABASE=$MYSQLDB_DATABASE
      - MYSQL_USER=$MYSQLDB_USER
      - MYSQL_PASSWORD=$MYSQLDB_PASSWORD
    ports:
      - $MYSQLDB_LOCAL_PORT:$MYSQLDB_DOCKER_PORT
    volumes:
      - db:/var/lib/mysql
  app:
    depends_on:
      - mysqldb
    build: .
    restart: on-failure
    env_file: ./.env
    ports:
      - $SPRING_LOCAL_PORT:$SPRING_DOCKER_PORT
    environment:
      SPRING_APPLICATION_JSON: '{
        "spring.datasource.url"  : "jdbc:mysql://mysqldb:$MYSQLDB_DOCKER_PORT/$MYSQLDB_DATABASE?useSSL=false",
        "spring.datasource.username" : "$MYSQLDB_USER",
        "spring.datasource.password" : "$MYSQLDB_ROOT_PASSWORD",
        "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.MySQL5InnoDBDialect",
        "spring.jpa.hibernate.ddl-auto" : "update"
      }'
    volumes:
      - .m2:/root/.m2
    stdin_open: true
    tty: true

volumes:
  db:
```

### Create the .env file
> The environment variables are defined in the .env file

```
MYSQLDB_USER=demo
MYSQLDB_ROOT_PASSWORD=secret1
MYSQLDB_DATABASE=demo
MYSQLDB_PASSWORD=secret2
MYSQLDB_LOCAL_PORT=33060
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=6868
SPRING_DOCKER_PORT=8080
```
run:
```
docker-compose up
```

then check it on the web, with the link: http://localhost:8080/api/{endpoint}

![image](https://github.com/user-attachments/assets/08dc5e7f-2978-4319-948f-6d66250b1682)


# Lab4

# Caderno - Lab 4

Seguem aqui uns links úteis para aprender a usar react js.

###
Create react project using vite

```bash
    npm create vite@latest my-react-app --template

Switch to the react-app directory.
```bash
    cd my-react-app
```
Now let's run the project.
 ```bash
 npm run dev
 ```
Then just follow the prompts, selecting 'React' and 'Javascript'. 

Seguem aqui uns links úteis para aprender a usar react js: 
- quick start: https://react.dev/learn
- props: https://react.dev/learn/passing-props-to-a-component
- state: https://react.dev/learn/updating-objects-in-state
- context: https://react.dev/learn/passing-data-deeply-with-context
- simple way to create a tic tac toe game: https://react.dev/learn/tutorial-tic-tac-toe
- useState: https://react.dev/reference/react/useState
- useEffect: https://react.dev/reference/react/useEffect


Usefull guide that demonstrates how to Consume Rest APIs in React: 
https://www.freecodecamp.org/news/how-to-consume-rest-apis-in-react/

# 4.4
Exercício muito útil e que vai servir de base para a iteração 3 do projeto.

usefull link: https://www.dhiwise.com/post/a-step-by-step-guide-to-implementing-react-spring-boot

Este código serviu para usar os endpoints do controller do exercíco 3.3.
```java
const base_url = "http://localhost:8081/api";

const api = {
    fetchShows: async() => {
        const response = await fetch(`${base_url}/shows`);
        return await response.json();
    },

    addShow: async(title, year) => {
        const response = await fetch(base_url+`/shows`, {
          method: 'POST',
          body: JSON.stringify({
              title: title,
              year: year
          }),
           headers: {
              'Content-type': 'application/json; charset=UTF-8',
          },
        })
        return await response.json();
    },

    deleteShow: async (id) => {
        const response = await fetch(`${base_url}/shows/${id}`, {
            method: 'DELETE'
        });
        if (response.status !== 200) {
            throw new Error("Error deleting show.");
        }
    },

    fetchQuotesByMovieId: async (showId = null) => {
        const url = showId ? `${base_url}/quotes?show=${showId}` : `${base_url}/quotes`;
        const response = await fetch(url);
        return await response.json();
    },
    
    fetchRandomQuote: async () => {
        const response = await fetch(`${base_url}/quote`);
        if (response.ok) {
            return await response.json();
        } else {
            throw new Error("Error fetching random quote.");
        }
    },

    addQuote: async (quote, movieId) => {
        const response = await fetch(base_url+`/quotes/show/${movieId}`, {
            method: 'POST',
            body: JSON.stringify({
                quote: quote
            }),
             headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        })
        if (response.ok){
            return await response.json();
        }
        else {
            throw new Error("Error adding a new quote.");
        }
    },

    deleteQuote: async (id) => {
        const response = await fetch(`${base_url}/quotes/${id}`, {
            method: 'DELETE'
        });
        if (response.status === 200) {
            return "Quote deleted successfully";
        } else {
            throw new Error("Error deleting quote.");
        }
    },
    
}

export default api;
```
Controller
```java
@RestController
@RequestMapping("api")
public class MovieQuotesController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private QuoteService quoteService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/quote")
    public ResponseEntity<Quote> getRandomQuote() {
        return new ResponseEntity<>(quoteService.getRandomQuote(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/quotes")
    public ResponseEntity<List<Quote>> getAllQuotes(@RequestParam(required=false) Long show) {
        if (show == null) {
            return new ResponseEntity<>(quoteService.getAllQuotes(), HttpStatus.OK);
        }
        else {
            List<Quote> quotes = quoteService.findAllByMovie(show);
            return new ResponseEntity<>(quotes, HttpStatus.OK);
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/quotes/show/{id}")
        public ResponseEntity<Quote> addQuote(@Valid @RequestBody Quote quote, @PathVariable(value = "id") Long movieId) {
        Movie movie = movieService.getMovieById(movieId);
        quote.setMovie(movie);
        return  new ResponseEntity<>(quoteService.addQuote(quote), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/quotes/{id}")
    public ResponseEntity<Quote> deleteQuote(@PathVariable(value = "id") Long quoteId) {
        quoteService.deleteQuote(quoteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/quotes/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable(value = "id") Long quoteId, @RequestBody Quote quote) {
        Quote updated = quoteService.updateQuote(quoteId, quote);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }



    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/shows")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/shows")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/shows/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable(value = "id") Long movieId) {
        movieService.deleteMovie(movieId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/shows/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable(value = "id") Long movieId, @RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.updateMovie(movieId, movie), HttpStatus.OK);
    }
}
```
> Os crossOrigin não estão lá por acaso: "allows a server to indicate any origins (domain, scheme, or port) other than its own from which a browser should permit loading resources."
Através disto e do seguinte trecho de código conseguimos ter acesso aos endpoints no frontend.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // This applies CORS to the `/api/**` endpoints
                .allowedOrigins("http://localhost:5173") // Allow your React frontend's origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, HTTP authentication)
    }
}
```
Desta forma conseguimos fazer operações CRUD a partir do frontend.

Para correr foi só inserir, na pasta do backend, o comando:
```bash
docker compose up -d
```
E inserir, na pasta do projeto React, o comando:
```bash
npm run dev
```

