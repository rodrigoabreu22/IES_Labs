export default function Movie(props) {
    return (
        <div className="post-card">
            <h2 className="post-title">Movie</h2>
            <p className="post-body">Title: {props.title}</p>
            <p className="post-body">Year: {props.year}</p>
            <p className="post-body">Id: {props.id}</p>
            <button 
                className="btn-delete" 
                onClick={() => props.deleteShow(props.id)}
            >Delete</button>
        </div>
    )   
}