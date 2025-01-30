Rodrigo Abreu - 113626

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

commando para criar imagem:
```
$ docker build -t {nomeDaImagem} .
```
