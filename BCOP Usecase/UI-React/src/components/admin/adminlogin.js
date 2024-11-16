import React, { useState, useEffect } from "react";
import auth from "../../service/adminauth";
import Header from "../header/header";
import { toast } from "react-toastify";
import { API_BASE_URL } from "../../service/service";

export default function AdminLogin(props) {
  const axios = require("axios");
  const [state, setState] = useState({
    customer: {
      user: {
        role: ""
      }
    }
  });

  const [login, setLogin] = useState({
    username: "",
    password: "",
    error: ""
  });

  function onChangeLoginHandler(event) {
    const updatedLogin = {
      ...login,
      [event.target.name]: event.target.value
    };
    setLogin(updatedLogin);
  }

  function apiCall() {
    return axios.get(
      API_BASE_URL + "login/" + login.username + "/" + login.password
    );
  }

  // function handleLogin(e) 
  // {
  //   if (login.username) {
  //     if (login.password) {
  //       apiCall()
  //         .then(data => {
  //           setState({ ...state, customer: data.data });
  //           if (state.customer.user.role === "ROLE_USER") {
  //             setLogin({ ...login, error: "Insufficient Previllages" });
  //           }
  //         })
  //         .catch(error => {
  //           console.log(error.response.data);
  //           setLogin({
  //             ...login,
  //             error: error.response.data
  //           });
  //         });
  //     } else {
  //       setLogin({ ...login, error: "Please enter password" });
  //     }
  //   } else {
  //     setLogin({ ...login, error: "Please enter username" });
  //   }
  // }

  // function redirect() {
  //   //console.log(state.customer);
  //   if (state.customer.user.role === "ROLE_ADMIN") {
  //     //alert("Login Success");
  //     auth.login();
  //     auth.setHeader(login.username, login.password);
  //     toast.success("Login Success");
  //     props.history.push("dashboard", { customer: state.customer });
  //     // return (
  //     //   <Link
  //     //     className="btn btn-success btn-block"
  //     //     style={{ textDecoration: "none" }}
  //     //     to={{
  //     //       pathname: "useraction",
  //     //       state: {
  //     //         customer: state.customer
  //     //       }
  //     //     }}
  //     //   >
  //     //     Continue
  //     //   </Link>
  //     //);
  //   }
  // }
  async function handleLogin() {
    if (login.username && login.password) {
      try {
        const response = await apiCall();
        setState({ ...state, customer: response.data });

        if (response.data.user.role === "ROLE_ADMIN") {
          auth.login();
          auth.setHeader(login.username, login.password);
          toast.success("Login Success");
          redirectToDashboard();
        } else {
          setLogin({ ...login, error: "Insufficient Privileges" });
        }
      } catch (error) {
        console.log(error.response.data);
        setLogin({
          ...login,
          error: error.response.data
        });
      }
    } else {
      setLogin({ ...login, error: "Please enter both username and password" });
    }
  }

  function redirectToDashboard() {
    // props.history.push("dashboard", { customer: state.customer });
props.history.push('AdminActiveTasklistDetail')
  }
  return (
    <div className="container-fluid back">
      <Header message="Admin LoGin" />
      {/* <div className="jumbotron" style={{ padding: "2%" }}>
        <div className="display-4 text-center">Admin Login</div>
      </div> */}
      <div className="container" style={{ marginTop: "6%", width: "50%" }}>
        <div className="card bg-light">
          <article className="card-body mx-auto" style={{ width: "50%" }}>
            <p className="divider-text">
              <span className="bg-light">Enter Credentials</span>
            </p>
            <form>
              <div className="form-group input-group">
                <div className="input-group-prepend">
                  <span className="input-group-text">
                    {" "}
                    <i className="fa fa-user iconsize"></i>{" "}
                  </span>
                </div>
                <input
                  name="username"
                  className="form-control"
                  placeholder="Username"
                  type="text"
                  value={login.username}
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
                  value={login.password}
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
              <br />
              <div className="text-center" style={{ color: "red" }}>
                {login.error}
              </div>
              {/* {redirect()} */}
              <br />
            </form>
          </article>
        </div>
        <br />
        <br />
      </div>
    </div>
  );
}
