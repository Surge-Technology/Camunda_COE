import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./registration.css";
import axios from "axios";
import { toast } from "react-toastify";

function Registration(props) {
  const [userDetails, setUserDetails] = useState({
    username: "",
    email: "",
    // lastName: "",
    // phoneNo: '',
    password: "",
    
  });

  const [isOtpSent, setIsOtpSent] = useState(false);
  const [isOtpVerified, setIsOtpVerified] = useState(false);
  const [otpData, setOTPData] = useState({ otp: "", timestamp: 0 });
  const [errors, setErrors] = useState({});
  const [showNotification, setShowNotification] = useState(false); // State for notification


  const globalToast = (message) => {
    toast.error(message);
  };

  const successToast = () => {
    toast.success("OTP Verified! Account Created Successfully");
  };

  const onChangeCustomerHandler = (event) => {
    const updatedUserDetails = {
      ...userDetails,
      [event.target.name]: event.target.value,
    };
    setUserDetails(updatedUserDetails);
  };

  const formSubmitHandler = () => {
    const newErrors = validateForm();
    if (Object.keys(newErrors).length === 0) {
      axios
        .post("http://localhost:8080/startWorkflow", userDetails)
        .then((response) => {
          sessionStorage.setItem("key", response.data);
          let key = response.data;
          console.log("key...", key);
          sessionStorage.setItem("key", key)
          globalToast("OTP has been sent to your email.");

          setIsOtpSent(true);
          props.history.push("Otp");

        })
        .catch((error) => {
          console.error("API Error:", error);
          globalToast("Failed to submit form. Please try again later.");
        });
        // alert("Submitted Successfully!!!!")
    } else {
      setShowNotification(true);

      setErrors(newErrors);
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!userDetails.username.trim()) {
      newErrors.username = "User name is required";
    }

    // if (!userDetails.lastName.trim()) {
    //   newErrors.lastName = "Last name is required";
    // }

    // if (!userDetails.email.trim()) {
    //   newErrors.email = "Email address is required";
    // } else if (!/\S+@\S+\.\S+/.test(userDetails.email)) {
    //   newErrors.email = "Invalid email address";
    // }

   

    if (!userDetails.password.trim()) {
      newErrors.password = "Password is required";
    }
    // if (!userDetails.phoneNo.trim()) {
    //   newErrors.phoneNo = "Phone number is required";
    // }

    return newErrors;
  };



  return (

    <div className="container-fluid">

      <div className="card bg-light bg">

        <article className="card-body mx-auto" style={{ width: "40%" }}>
          <br /> <br /> <br />
          <h6 className="display-4 text-center" style={{ textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)' }}>
            Reserve Bank of Australia
          </h6>
          <h5 className="text-center" >Create Account</h5><br />

          <p className="divider-text">
            <span className="bg-light">Enter Details</span>
          </p>
          {showNotification && ( 
        <div className="notification" >
          Please enter all the required details.
        </div>
      )}


          <form>

            <div className="form-group input-group">

              <div className="input-group-prepend">

                <span className="input-group-text">

                  <i className="fa fa-user-plus iconsize"></i>

                </span>

              </div>

              <input

                name="username"

                className="form-control"

                placeholder="User Name"

                type="text"

                value={userDetails.username}

                onChange={onChangeCustomerHandler}
                required
              />


            </div>

            {/* <div className="form-group input-group">

              <div className="input-group-prepend">

                <span className="input-group-text">

                  <i className="fa fa-user-plus iconsize"></i>

                </span>

              </div>

              <input

                name="lastName"

                className="form-control"

                placeholder="Last name"

                type="text"

                value={userDetails.lastName}

                onChange={onChangeCustomerHandler}
                required
              />

            </div> */}
            <div className="form-group input-group">

              <div className="input-group-prepend">

                <span className="input-group-text">

                  <i className="fa fa-envelope iconsize"></i>

                </span>

              </div>



              <input

                name="email"

                className="form-control"

                placeholder="Email address"

                type="email"

                value={userDetails.email}

                onChange={onChangeCustomerHandler}
                required
              />

            </div>

            <div className="form-group input-group">

<div className="input-group-prepend">

  <span className="input-group-text">

    <i className="fa fa-lock iconsize"></i>

  </span>

</div>



<input

  name="password"

  className="form-control"

  placeholder="Enter password"

  type="password"

  value={userDetails.password}

  onChange={onChangeCustomerHandler}
  required
/>

</div>


            {/* <div className="form-group input-group">

              <div className="input-group-prepend">

                <span className="input-group-text">

                  <i className="fa fa-phone iconsize"></i>

                </span>

              </div>



              <select>

                <option selected="" value="+91">

                  +91

                </option>



                <option value="+0">+0</option>



                <option value="+1">+61</option>



                <option value="+44">+44</option>



                <option value="+33">+33</option>

              </select>



              <input

                name="phoneNo"

                className="form-control"

                placeholder="Phone Number"

                type="number"

                value={userDetails.phoneNo}

                onChange={onChangeCustomerHandler}
                required
              />

            </div> */}



           



            {!isOtpSent && (

              <div className="form-group">

                <button

                  type="button"

                  onClick={formSubmitHandler}

                  className="btn btn-primary btn-block"

                >

                  Submit

                </button>

              </div>

            )}



            {isOtpSent && !isOtpVerified && (

              <div>

                <p>OTP has been sent to your email.</p>



                <div className="form-group">

                  <button

                    type="button"

                    onClick={formSubmitHandler}

                    className="btn btn-primary btn-block"

                  >

                    Verify OTP

                  </button>

                </div>

              </div>

            )}



            {isOtpVerified && (

              <div className="form-group">

                <button

                  type="button"

                  onClick={() => {

                    successToast();



                    // Redirect to the login page after OTP is verified



                    props.history.push("login");

                  }}

                  className="btn btn-primary btn-block"

                >

                  Create Account

                </button>

              </div>

            )}



            <p

              className="text-center"

              style={{ color: "black", fontSize: "23px" }}

            >

              Have an account? <Link to="login">Login</Link>

            </p>

          </form>

        </article>

      </div>

    </div>

  );

}



export default Registration;