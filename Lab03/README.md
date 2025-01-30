### Respostas Ex1 c)
```txt
1.
O objeto não é instanciado. É um processo de dependency injection, onde a interface UserRepository é injetada no UserController através do construtor.

2.
- save()
- findAll()
- findById(id)
- delete()

A interface CrudRepository e os seus métodos são definidos no módulo Spring Data Commons.
Ao incluir a dependência Spring Data JPA no projeto, consigo ter acesso aos repositórios desta interface e aos seus métodos.

3.
Os dados são guardados na H2 database.

4.
@NotBlank(message = "Email is mandatory") é definido na User class.
```

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

