import { useState, useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import AddQuote from './components/addQuote';
import AddMovie from './components/addMovie';
import Movie from './components/movie';
import api from './api';
import './App.css';

export default function App() {
  const [quotes, setQuotes] = useState([]);
  const [shows, setShows] = useState([]);
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

  // Periodically fetch the last 5 quotes
  useEffect(() => {
    const fetchQuotes = async () => {
      try {
        const data = await api.fetchQuotesByMovieId(); // No showId provided to fetch all quotes
        console.log('Fetched quotes:', data); // Debugging
        setQuotes(data);
      } catch (error) {
        console.log('Error fetching quotes:', error);
      }
    };

    // Initial fetch
    fetchQuotes();

    // Set up periodic fetch every 10 seconds
    const intervalId = setInterval(() => {
      fetchQuotes();
    }, 10000);

    // Cleanup interval on component unmount
    return () => clearInterval(intervalId);
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

  return (
      <main>
        <h1>Movie and Quote Management</h1>

        {/* Add new movie */}
        <AddMovie addShow={addShow} />

        {/* Add new quote */}
        <AddQuote addQuote={addQuote} />

        {/* Display Last 5 Quotes */}
        <section>
          <h2>Last 5 Quotes</h2>
          {quotes.length > 0 ? (
              [...quotes]
                  .sort((a, b) => b.id - a.id) // Sort quotes by ID in descending order
                  .slice(0, 5) // Get the last 5 quotes
                  .map((quote) => (
                      <p key={quote.id}>
                        "{quote.quote}" - <em>{quote.movie?.title || 'Unknown Movie'}</em>
                      </p>
                  ))
          ) : (
              <p>No quotes available</p>
          )}
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
      </main>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
