# Caderno - Lab 4

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
