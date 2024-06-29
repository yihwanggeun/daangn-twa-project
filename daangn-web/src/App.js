import React from 'react';
import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Navbar from './components/navbar';
import MainPage from './pages/MainPage';
import DetailPage from './pages/DetailPage';

function App() {
  return (
    <Router>     
      <div>
        <Navbar />
        <Routes>
          <Route path="/"  element={<MainPage />}/>
          <Route path="/articles/:id" element={<DetailPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
