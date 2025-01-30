import {useState} from 'react'

export default function AddMovie(props) {
    const [title, setTitle] = useState('');
    const [year, setYear] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        props.addShow(title, year);
        setTitle('');
        setYear('');
    };    
    
    return (
        <form onSubmit={handleSubmit}>
            <h2>Add new Movie</h2>
            <div className="input-container">
                <label htmlFor="tile">Title</label>
                <input
                    name="title" 
                    type="text" 
                    value={title} 
                    onChange={(e) => setTitle(e.target.value)}>
                </input>
            </div>
            <div className="input-container">
                <label htmlFor="year">Year</label>
                <input
                    name="year" 
                    type="number"
                    value={year}
                    onChange={(e) => setYear(e.target.value)}>
                </input>
            </div>
            <button type="submit" className="btn-submit">Add Movie</button>
        </form>
    )
}