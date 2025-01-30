import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import Canva from './Challenge2.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Canva />
  </StrictMode>,
)
