import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import '../pages/Login.css';

// JSON data containing username, password, and role
const users = [
  {
    username: 'admin',
    password: 'admin123',
    role: 'admin'
  },
  {
    username: 'manju',
    password: 'manju123',
    role: 'customer'
  },
  {
    username: 'rakesh',
    password: 'rakesh123',
    role: 'customer'
  }
];

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loggedIn, setLoggedIn] = useState(false);
  const [role, setRole] = useState('');

  const handleLogin = () => {
    // Check if the provided username and password match any user in the JSON data
    const user = users.find(
      (user) => user.username === username && user.password === password
    );

    if (user) {
      setRole(user.role);
      setLoggedIn(true);

      // Store data in sessionStorage
      sessionStorage.setItem('mySessionStorageData', JSON.stringify(user));
    } else {
      alert('Invalid username or password');
    }

    // Retrieve data from sessionStorage based on username
    const userData = users.find((user) => user.username === username);
    if (userData) {
      const storedData = sessionStorage.getItem('mySessionStorageData');
      console.log(JSON.parse(storedData));
    }
  };

  if (loggedIn) {
    // Redirect to different pages based on the user's role
    if (role === 'admin') {
      return <Navigate to="/inbox" />;
    } else if (role === 'customer') {
      return <Navigate to="/shop" />;
    }
  }

  return (
    <div className="login-container">
      <h1 className="login-form">Login Page</h1>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <br />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <br />
      <button className="btn btn-warning btn-block" onClick={handleLogin}>
        Login
      </button>
    </div>
  );
};

export default LoginPage;
