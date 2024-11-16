import React from "react";
import { Link, NavLink,useLocation } from "react-router-dom";
import {useNavigate} from "react-router-dom";
 
 
const Navbar = () => {
 
  let navigate = useNavigate();
  const location = useLocation();
 
  const showNavbar = location.pathname !== "/" && !location.pathname.includes("/payment");
 
  const onLogout = () => {
    navigate("/")
  }
  // let data = sessionStorage.getItem("mySessionStorageData");
 
  //   data = JSON.parse(data);
 
  //   const loggedInUserId = data;
 
  //   const username = loggedInUserId.username;
 
  //   console.log(username, "Username");
  let username = null;
  if (showNavbar) {
    let data = sessionStorage.getItem("mySessionStorageData");
    if (data) {
      data = JSON.parse(data);
      username = data.username;
      console.log(username, "Username");
    }
  }
   
 
  if (!showNavbar) {
 
    return null; // Return null to hide the navbar on the login page
 
  }
 
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container">
      <Link >
      {username === 'admin' ? 'Inbox' : 'Shop'}
    </Link>
     {/*  <Link className="navbar-brand" exact to="/inbox">
              InboxXX
              </Link>
        <Link className="navbar-brand" exact to="/home">
          Product List
        </Link> */}
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav mr-auto">
          {username === 'Admin' ? (
            <>
              <li className="nav-item">
                <NavLink className="nav-link" exact to="/inbox">
                  Inbox
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" exact to="/home">
                  Product List
                </NavLink>
              </li>
            </>
          ) : (
            <>
              <li className="nav-item">
                <NavLink className="nav-link" exact to="/shop">
                  Shop
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" exact to="/CartPage">
                  Cart Page
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" exact to="/orderhistory">
                  Order History
                </NavLink>
              </li>
              <div></div>
            </>
          )}
          </ul>
 
         
        </div>
        <form class="d-flex">
        <div className="right-corner "style={{ textAlign: 'end',fontSize:20 }}>
         <span>{username}</span>
      </div>
 
       <button className="btn btn-danger ml-4" onClick={onLogout} style={{ marginLeft: '15px' }}>Logout </button>
     
   
    </form>
       
      </div>
    </nav>
  );
};
 
export default Navbar;