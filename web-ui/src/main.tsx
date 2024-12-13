import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

import './css/additional-styles/theme.css'
import './css/additional-styles/custom-fonts.css'
import './css/additional-styles/utility-patterns.css'

const root = document.getElementById("root");

createRoot(root!).render(
  <StrictMode>
      <App />
  </StrictMode>,
)
