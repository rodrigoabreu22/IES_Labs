import {useState} from 'react'

export default function AddQuote(props) {
    const [quote, setQuote] = useState('');
    const [movie, setMovie] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        props.addQuote(quote, movie);
        setQuote('');
        setMovie('');
    };    
    
    return (
        <form onSubmit={handleSubmit}>
            <h2>Add new Quote</h2>
            <div className="input-container">
                <label htmlFor="movie">Movie ID</label>
                <input
                    name="movie" 
                    type="number" 
                    value={movie} 
                    onChange={(e) => setMovie(e.target.value)}>
                </input>
            </div>
            <div className="input-container">
                <label htmlFor="quote">Quote</label>
                <textarea
                    name="quote" 
                    value={quote}
                    onChange={(e) => setQuote(e.target.value)}
                />
            </div>
            <button type="submit" className="btn-submit">Add Quote</button>
        </form>
    )
}