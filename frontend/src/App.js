import LoginPage from "./components/LoginPage";
import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";
import RegistrationPage from "./components/RegistrationPage";
import ShortenURLPage from "./components/ShortenURLPage";
import AllUrls from "./components/AllUrls";
import Test from "./components/Test";
import { CookiesProvider } from "react-cookie";

function App() {
  return (
  <CookiesProvider>
    <Router>
      <div className="app">
    <Routes>
      <Route path="/" element={<LoginPage/>}/>
      <Route path="/signup" element={<RegistrationPage/>}/>
      <Route path="/urls" element={<ShortenURLPage/>}/>
      <Route path="/myurls" element={<AllUrls/>}/>
      <Route path="test" element={<Test/>}/>
    </Routes>
    </div>
    </Router>
    </CookiesProvider>
    
  );
}

export default App;