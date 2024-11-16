import React, { useState } from "react";
import { Link } from "react-router-dom";
import Header from "../header/header";
import axios from "axios"; 

const initialLoginState = {
  email: "",
  password: "",
  error: ""
};

export default function Login(props) {
  const [login, setLogin] = useState(initialLoginState);
  const [showOTPModal, setShowOTPModal] = useState(false);
  function onChangeLoginHandler(event) {
    const updatedLogin = {
      ...login,
      [event.target.name]: event.target.value
    };
    setLogin(updatedLogin);
  }
  async function handleLogin() {
    if (!login.email) {
      setLogin({ ...login, error: "Please enter email" });
    } else if (!login.password) {
      setLogin({ ...login, error: "Please enter password" });
    } else {
    try {
        const response = await axios.post(`http://localhost:8080/login?email=${login.email}&password=${login.password}`);

        const email = login.email;
        sessionStorage.setItem('email', email);
        console.log("email",email)
        // alert(response.data);

        if (response.data === "Login successful!") {
          if (login.email === "Manager"|| login.email=== "Supervisior") {
            props.history.push("ApproverForm");
          } 
          else if(login.email === 'murali.muthu@surgetechinc.in'){
            props.history.push("ApproverForm");
          }
          else {
            props.history.push("AddressFillForm");
          }
        } else {
          setLogin({ ...login, error: "Login failed" });
        }
      } catch (error) {
        console.error("Login error:", error);
        setLogin({ ...login, error: "An error occurred" });
      }
    }
  }
  return (
    <div className='background-img'>
    <div className="container-fluid back">
      <Header message="Login" />
      <div className="container" style={{ marginTop: "0%", width: "50%" }}>
        <div>
          <article className="card-body mx-auto" style={{ width: "100%" }}>
            <p className="divider-text">
              <span className="bg-light">Enter Credentials</span>
            </p>
            <form>
              <div className="form-group input-group">
                <div className="input-group-prepend">
                  <span className="input-group-text">
                    {" "}
                    <i className="fa fa-envelope iconsize"></i>{" "}
                  </span>
                </div>
                <input
                  name="email"
                  className="form-control"
                  placeholder="UserName"
                  type="email"
                  value={login.email}
                  onChange={onChangeLoginHandler}
                />
             </div>
              <div className="form-group input-group">
                <div className="input-group-prepend">
                  <span className="input-group-text">
                    {" "}
                    <i className="fa fa-lock iconsize"></i>
                  </span>
                </div>
                <input
                  name="password"
                  className="form-control"
                  placeholder="Password"
                  type="password"
                  // value={login.password}
                  onChange={onChangeLoginHandler}
                />
              </div>
              <button
                type="button"
                className="btn btn-primary btn-block"
                onClick={handleLogin}
              >
                Login
              </button>
              <div className="text-center" style={{ color: "red" }}>
                {login.error}
              </div>
              <strong>
                <p className="text-center" style={{ fontSize: "20px" }}>
                  <small>New User?</small>
                  <Link to="/registration">
                    <br /> Create new Account
                  </Link>
                </p>
              </strong>
            </form>
          </article>
        </div>
        <br />        <br />
        {showOTPModal && (
          <div className="modal" tabIndex="-1" role="dialog">
            {/* OTP modal content */}
          </div>
        )}
      </div>
    </div>
    </div>
  );}