import React from "react";
import { Link } from "react-router-dom";
import "./homepage.css";

import Header from "../header/header";

function HomePage() {
  return (
    <div
      className="container-fluid p-0"
      style={{ height: "100vh", backgroundColor: "#e0fcff" }}
    >
      <Header message="Welcome To Reserve Bank of Australia" />

      <div className="row homepageaction">
        <div className="col-sm-3 offset-3">
          <p>
            <Link
              to="login"
              className="btn btn-success btn-block btn-lg"
              style={{
                textDecoration: "none",
                color: "white"
                //backgroundColor: "#52de97"
              }}
            >
              Log In
              <span style={{ float: "right" }}>
                <i
                  className="fas fa-sign-in-alt"
                  style={{ fontSize: "30px" }}
                ></i>
              </span>
            </Link>
          </p>
        </div>

        <div className="col-sm-3">
          <p>
            <Link
              className="btn btn-success btn-block btn-lg"
              to="registration"
              style={{ textDecoration: "none", color: "white" }}
            >
            Become a  RBA Member
              <span style={{ float: "right" }}>
                <i
                  className="fas fa-user-plus"
                  style={{ fontSize: "30px" }}
                ></i>
              </span>
            </Link>
          </p>
        </div>
      </div>


      <footer className="footer">
        <div className="container-fluid p-0">
          <div className="row text-left">
            <div className="col-md-4">
              <div className="pad">
                <h4 className="text-light">Our History</h4>
                <p className="text-muted">
                  We continue to serve our services to our happy
                  and satisfied customers
                </p>

              </div>
            </div>

            <div className="col-md-4">
              <div className="pad">
                <h4 className="text-light">About Us</h4>
                <p className="text-muted">
                  Global leading service provider in Banking Sector. You are
                  currently viewing our universally used E-Wallet portal
                </p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="pad">
                <h4 className="text-light">Trust</h4>
                <p className="text-muted">
                  Our hard working and tallented team works seamlessly to keep
                  your Information safe and locked. More than a million happy
                  customers and still counting.
                </p>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default HomePage;
