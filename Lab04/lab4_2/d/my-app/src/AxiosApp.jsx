import {useState, useEffect} from 'react';
import ReactDOM from 'react-dom/client';
import AddPost from './components/addPost';
import Post from './components/post';
import './App.css'

export default function App() {
  const [posts, setPosts] = useState([]);

  const client = axios.create(
    {
        baseURL: 'https://jsonplaceholder.typicode.com'
    }
  )
  
  const fetchPosts = async() => {
    try {
      const response = await client.get('?_limit=4')
      setPosts(response.data);
    } catch (error) {
      console.log(error);
    }
  }
 
 useEffect(() => {
      fetchPosts()
   }, []);
   
  const addPost = async(title, body) => {
    try{
    const response = await client.post('', {
        title,
        body
    })
    setPosts((prevPosts) => [response.data, ...prevPosts])
    } catch(error){
      console.log(error);
    }
  };
   
  const deletePost = async(id) => {
    try{
    const response = await client.delete('${id}');
    setPosts(posts.filter((post) => {
        return post.id != id;
    }))
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