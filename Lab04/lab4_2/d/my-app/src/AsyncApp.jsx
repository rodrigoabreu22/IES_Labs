import {useState, useEffect} from 'react';
import ReactDOM from 'react-dom/client';
import AddPost from './components/addPost';
import Post from './components/post';
import './App.css'

export default function App() {
  const [posts, setPosts] = useState([]);
  
  const fetchPosts = async() => {
    try {
      const response = await fetch(
         'https://jsonplaceholder.typicode.com/posts?_limit=4'
      );
      const data = await response.json();
      setPosts(data);
    } catch (error) {
      console.log(error);
    }
  }
 
 useEffect(() => {
      fetchPosts()
   }, []);
   
  const addPost = async(title, body) => {
    try{
    const response = await fetch('https://jsonplaceholder.typicode.com/posts', {
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
    const data = await response.json();
    setPosts((prevPosts) => [data, ...prevPosts])
    } catch(error){
      console.log(error);
    }
  };
   
  const deletePost = async(id) => {
    try{
    const response = await fetch(`https://jsonplaceholder.typicode.com/posts/${id}`, {
      method: 'DELETE'
    })
    if(response.status === 200) {
      setPosts(
        posts.filter((post) => {
          return post.id !== id;
        })
      )
    }
  } catch (error){
    console.log(error);
  }
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