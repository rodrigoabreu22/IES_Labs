export default function Quote(props) {
    return (
        <div className="post-card">
            <h2 className="post-title">Quote {props.id}</h2>
            <p className="post-body">Movie: {props.movie.title}</p>
            <p className="post-body">Quote: {props.quote}</p>
            <button 
                className="btn-delete" 
                onClick={() => props.deleteQuote(props.id)}
            >Delete</button>
        </div>
    )   
}