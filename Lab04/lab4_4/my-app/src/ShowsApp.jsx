import { useState, useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import AddQuote from './components/addQuote';
import AddMovie from './components/addMovie';
import Quote from './components/quote';
import Movie from './components/movie';
import api from './api';
import './App.css';

export default function App() {
  const [quotes, setQuotes] = useState([]);
  const [shows, setShows] = useState([]);
  const [randomQuote, setRandomQuote] = useState(null);
  const [showId, setShowId] = useState('');

  // Fetch all shows on component mount
  useEffect(() => {
    const fetchShows = async () => {
      try {
        const data = await api.fetchShows();
        setShows(data);
      } catch (error) {
        console.log(error);
      }
    };
    fetchShows();
  }, []);

  // Fetch all quotes on component mount
  useEffect(() => {
    const fetchQuotes = async () => {
      try {
        const data = await api.fetchAllQuotes();
        setQuotes(data);
      } catch (error) {
        console.log(error);
      }
    };
    fetchQuotes();
  }, []);

  // Add new movie
  const addShow = async (title, year) => {
    try {
      const newShow = await api.addShow(title, year);
      setShows((prevShows) => [newShow, ...prevShows]);
    } catch (error) {
      console.log(error);
    }
  };

  // Add new quote
  const addQuote = async (quote, movieId) => {
    try {
      const newQuote = await api.addQuote(quote, movieId);
      setQuotes((prevQuotes) => [newQuote, ...prevQuotes]);
    } catch (error) {
      console.log(error);
    }
  };

  // Delete a show
  const deleteShow = async (id) => {
    try {
      await api.deleteShow(id);
      setShows((prevShows) => prevShows.filter((show) => show.id !== id));
    } catch (error) {
      console.log(error);
    }
  };

  // Delete a quote
  const deleteQuote = async (id) => {
    try {
      await api.deleteQuote(id);
      setQuotes((prevQuotes) => prevQuotes.filter((quote) => quote.id !== id));
    } catch (error) {
      console.log(error);
    }
  };

  // Fetch random quote
  const fetchRandomQuote = async () => {
    try {
      const random = await api.fetchRandomQuote();
      setRandomQuote(random);
    } catch (error) {
      console.log(error);
    }
  };

  // Fetch quotes by movie ID
  const fetchQuotesByMovieId = async () => {
    try {
      const data = await api.fetchQuotesByMovieId(showId);
      setQuotes(data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <main>
      <h1>Movie and Quote Management</h1>
      
      {/* Add new movie */}
      <AddMovie addShow={addShow} />

      {/* Add new quote */}
      <AddQuote addQuote={addQuote} />

      {/* Experiment with endpoints */}
      <section>
        <h2>Experiment with Endpoints</h2>
        
        {/* Fetch random quote */}
        <button onClick={fetchRandomQuote}>Fetch Random Quote</button>
        {randomQuote && (
          <p>
            Random Quote: "{randomQuote.quote}" - {randomQuote.movie.title}
          </p>
        )}
        
        {/* Fetch quotes by movie ID */}
        <input
          type="text"
          placeholder="Enter Movie ID"
          value={showId}
          onChange={(e) => setShowId(e.target.value)}
        />
        <button onClick={fetchQuotesByMovieId}>Fetch Quotes by Movie ID</button>
      </section>

      {/* Display Movies */}
      <section className="posts-container">
        <h2>Movies</h2>
        {shows.map((show) => (
          <Movie
            key={show.id}
            id={show.id}
            title={show.title}
            year={show.year}
            deleteShow={() => deleteShow(show.id)}
          />
        ))}
      </section>

      {/* Display Quotes */}
      <section className="posts-container">
        <h2>Quotes</h2>
        {quotes.map((quote) => (
          <Quote
            key={quote.id}
            id={quote.id}
            quote={quote.quote}
            movie={quote.movie}
            deleteQuote={() => deleteQuote(quote.id)}
          />
        ))}
      </section>
    </main>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
