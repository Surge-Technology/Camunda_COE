import React from 'react'
import Header from '../header/header';
import { useState } from 'react';

// Initial state for the login form
const initialLoginState ={
  email: '',
  password:'',
 // error:''
};

// Define the Login component
export default function AdminLogin (props)  {
// State to manage login form data
  const[login,setLogin] = useState(initialLoginState);

   // Function to handle changes in login form input fields
  function onChangeLoginHandler(event){
    const updatedLogin ={
      ...login,
      [event.target.name]: event.target.value
    };
    setLogin(updatedLogin);
  }

   // Function to handle login form submission
  function handleLogin(){
    //Validate if email and password are provided
    if(!login.email){
      // setLogin({...login,error:'Please enter email'});
    }else if (!login.password) {
      // setLogin({ ...login, error: "Please enter password" });
    } else{
      props.history.push('AdminActiveTasklistDetail');
    }

  }

  

  return (
    <div className='container-fluid back'>
       {/* Display the Header component with the message */}
      <Header message="Admin Login" />

      {/* Create the login form */}
      <div className="container" style={{ marginTop: "5%", width: "50%", height: "50%" }}>
        <div className="card bg-light">
          <article className="card-body mx-auto" style={{ width: "50%"}}>
            {/* Display the "Enter Credentials" divider */}
            <p className="divider-text">
              <span className="bg-light">Enter Credentials</span>
            </p>

            <form>
              {/* Input field for email */}
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
                  placeholder="Email"
                  type="email"
                  value={login.email}
                  onChange={onChangeLoginHandler}
                />
              </div>

              {/* Input field for password */}
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

              {/* Button to trigger the login */}
              <button
                type="button"
                className="btn btn-primary btn-block"
                onClick={handleLogin}
              >
                Login
              </button>

              {/* Display the error message, if any */}
              <div className="text-center" style={{ color: "red" }}>
                {/* {login.error} */}
              </div>
              </form>
        </article>
        </div>
        </div>
    </div>
  )
}


