import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
 
const DefaultLayout = () => {
  return (
    <div>
      <Navbar />
      <main>
        <Outlet /> {/* Renders the matched child route component */}
      </main>
    </div>
  );
};
 
export default DefaultLayout;