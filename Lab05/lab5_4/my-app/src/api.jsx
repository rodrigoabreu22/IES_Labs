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