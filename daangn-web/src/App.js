import React, {useState} from 'react';
import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Navbar from './components/navbar';
import MainPage from './pages/MainPage';
import DetailPage from './pages/DetailPage';

import { PortProvider } from './hooks/PortContext';

function App() {
  const [port, setPort] = useState(null);
  return (
    <PortProvider>
    <Router>     
      <div>
        <Navbar />
        <Routes>
          <Route path="/"  element={<MainPage />}/>
          <Route path="/articles/:id" element={<DetailPage />} />
        </Routes>
      </div>
    </Router>
    </PortProvider>
  );
}

export default App;
