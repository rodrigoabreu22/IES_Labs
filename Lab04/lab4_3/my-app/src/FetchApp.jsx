import {useState, useEffect} from 'react';
import ReactDOM from 'react-dom/client';
import AddPost from './components/addPost';
import Post from './components/post';
import './App.css'

function App() {
  const [posts, setPosts] = useState([]);
  
  const fetchPosts = () => {
    fetch("https://jsonplaceholder.typicode.com/posts?_limit=4")
      .then((response) => {
        if (!response.ok) {
            throw Error(response.statusText);
         }
        return response.json()})
      .then((data) => {
        console.log(data);
        setPosts(data)})
      .catch((err) => {
          console.log(err.message);
       });
  }
 
 useEffect(() => {
      fetchPosts()
   }, []);
   
  const addPost = (title, body) => {
    fetch('https://jsonplaceholder.typicode.com/posts', {
      method: 'POST',
      body: JSON.stringify({
          title: title,
          body: body,
          userId: Math.random().toString(36).slice(2),
      }),
       headers: {
          'Content-type': 'application/json; charset=UTF-8',
      },
    })
    .then((response) => {
        if (!response.ok) {
            throw Error(response.statusText);
         }
        return response.json()})
    .then((data) => {
        console.log(data);
        setPosts((prevPosts) => [data, ...prevPosts])
    })
    .catch((err) => {
        console.log(err.message);
     });
  };
   
  const deletePost = (id) => {
    fetch(`https://jsonplaceholder.typicode.com/posts/${id}`, {
      method: 'DELETE'
    })
    .then((response) => {
      if(response.status === 200) {
        setPosts(
          posts.filter((post) => {
            return post.id !== id;
          })
        )
      }
    })
  };
   
  return (
    <main>
    <h1>Consuming REST api tutorial</h1>
      <AddPost addPost={addPost}/>
      <section className="posts-container">
      <h2>Posts</h2>
        {posts.map((post) => 
          <Post 
            key={post.id} 
            id={post.id}
            title={post.title} 
            body={post.body} 
            deletePost={deletePost}
          />
        )}
      </section>
   </main>
  )
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />); 