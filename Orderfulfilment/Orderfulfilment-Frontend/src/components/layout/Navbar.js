import React from "react";
import { Link, NavLink,useLocation } from "react-router-dom";
import {useNavigate} from "react-router-dom";


const Navbar = () => {

  let navigate = useNavigate();
  const location = useLocation();

  const showNavbar = location.pathname !== "/";

  const onLogout = () => {
    navigate("/")
  }

  if (!showNavbar) {

    return null; // Return null to hide the navbar on the login page

  }

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
      <div className="container">
      <Link className="navbar-brand" exact to="/inbox">
              Inbox
              </Link>
        {/* <Link className="navbar-brand" exact to="/home">
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
            <li className="nav-item "></li>

            <li className="nav-item ">
            {/* <NavLink className="nav-link" exact to="/inbox">
              Inbox
              </NavLink> */}
              <NavLink className="nav-link" exact to="/home">
          Product List
        </NavLink>
            </li>

            <li className="nav-item ">
            <NavLink className="nav-link" exact to="/shop">
                Shop
              </NavLink>
            </li>
            <li className="nav-item ">
            <NavLink className="nav-link" exact to="/CartPage">
                Cart Page
              </NavLink>
            </li>

            <li className="nav-item ">
            <NavLink className="nav-link" exact to="/orderhistory">
                Order History
              </NavLink>
            </li>
           
            
          </ul>

          
        </div>
        <form class="d-flex">
      {/* <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search"/> */}
      {/* <button class="btn btn-outline-success" type="submit">Search</button> */}
      <button className="btn btn-danger" onClick={onLogout}>Logout </button>
      
    
    </form>
        {/* <Link className="btn btn-outline-light" to="/user/add">
          Add Product
        </Link> */}
      </div>
    </nav>
  );
};

export default Navbar;
