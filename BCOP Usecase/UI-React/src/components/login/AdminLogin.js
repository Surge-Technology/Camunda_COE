import React, { useState } from 'react'
import './adminLogin.css'
import Axios from 'axios';
const initialLoginState = {
    email: "",
    password: "",
    error: ""
};
const AdminLogin = (props) => {
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
                const response = await Axios.post(`http://localhost:8080/login?email=${login.email}&password=${login.password}`);

                const email = login.email;
                sessionStorage.setItem('email', email);
                console.log("email", email)
                // alert(response.data);

                if (response.data === "Login successful!") {
                    if (login.email === "Manager") {
                        props.history.push("ApproverForm");
                    }
                    else if (login.email === "Supervisior") {
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
        <div>

            <section class="login-block">
                <br /><br />
                <center>
                <div class="container1">
                    <div class="row ">
                        <div class="col login-sec">
                            <h2 class="text-center">ADMIN PORTAL</h2>
                            <form class="login-form">
                                <div class="form-group">
                                    <label for="exampleInputEmail1" class="text-uppercase">Username</label>
                                    <input 
                                    type="text"
                                    name="email"
                  className="form-control"
                  placeholder="UserName"
                  value={login.email}
                  onChange={onChangeLoginHandler}/>

                                </div>
                                <div class="form-group">
                                    <label for="exampleInputPassword1" class="text-uppercase">Password</label>
                                    <input 
                                    // type="password" class="form-control" placeholder=""
                                    name="password"
                                    className="form-control"
                                    placeholder="Password"
                                    type="password"
                                    value={login.password}
                                    onChange={onChangeLoginHandler}
                                     />
                                </div>


                                <div class="center">

                                    <center>
                                        <button type="button"
                className="btn btn-primary btn-block"
                onClick={handleLogin} class="btn btn-login ">Submit</button>
                                    </center>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
                </center>
            </section>
        </div>
    )
}

export default AdminLogin
