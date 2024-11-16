import React, { useState, useEffect } from 'react';
import './Otp.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import axios from 'axios';
import { toast } from "react-toastify";
import { useHistory } from 'react-router-dom';
//import Loader from 'react-loader-spinner'


const Otp = (props) => {
  const [otpValue, setOTPValue] = useState('');
  const [isValidOTP, setIsValidOTP] = useState(null); // null means the validation is pending
  const [isLoading, setIsLoading] = useState(false);

  const history = useHistory();

  const handleVerifyOTP = () => {
    setIsLoading(true);
    let processInstanceKey = sessionStorage.getItem("key");

    console.log("processInstanceKey", processInstanceKey); 
    axios.get(`http://localhost:8080/enterOTP/${otpValue}/${processInstanceKey}`)
      .then((response) => {
        const isValidOTP = response.data.OTP;
        if (isValidOTP) {
          alert("Email verified successfully");

          // Get localTaskNames and jobKey from the response
          //const localTaskNames = response.data.extractedInfo.localTaskName;
          //console.log("localTaskNames", localTaskNames)
          //alert("localTaskNames--" + localTaskNames)
          //const jobKey = response.data.extractedInfo.jobKey;
          //sessionStorage.setItem("jobKey", jobKey);
          sessionStorage.setItem("jobKey", "1234");
          //console.log("Jobkey...", jobKey);
          //props.history.push('/' + localTaskNames);
          props.history.push('/addressFillForm');

        } else {
          alert("Invalid IPIN. Please try again.");
          setOTPValue("");
        }
        setIsLoading(false);
      })
      .catch((error) => {
        console.error('Error fetching OTP data:', error);
        alert("Error verifying IPIN. Please try again later.");
        setIsLoading(false);
      });
  };

  function globalToast(message) {

    return toast.error(message);

  }
  const handleCancel = () => {
    // setOTPValue('');
    // setIsValidOTP(null);
    props.history.push("/"); // Redirect to the home page

  };
  return (
    <div className="container p-7">
      <div className="row">
        <div className="col-md-3" />
        <div className="col-md-6 mt-3">
          <div className="bg-white p-5 rounded-3 shadow-sm border">
            <div>
              <p className="text-center text-success" style={{ fontSize: '5.5rem' }}>
                <i className="fa-solid fa-envelope-circle-check" />
              </p>
              <p className="text-center text-center h5">IPIN  Verification</p>
              <p className="text-muted text-center">Please check your email</p>
              {/* <div className="row pt-4 pb-2">
                {isLoading ? (
                  <div className="col-12 text-center">
                    Loading OTP...
                  </div>
                ) : (
                  otpValue.split('').map((digit, index) => (
                    <div key={index} className="col-2">
                      <input
                        className="otp-letter-input"
                        type="text"
                        value={digit}
                        readOnly
                      />
                    </div>
                  ))
                )}
              </div> */}

              <div className="row pt-5">
                <div className="col-12 text-center">
                  <input
                    className="otp-letter-input"
                    type="text"
                    value={otpValue.replace(/./g, '*')}
                    // value={otpValue}
                    onChange={(e) => setOTPValue(e.target.value)}
                    maxLength="6"
                    placeholder="Enter IPIN"
                  />
                </div>
              </div>
              <div className="row pt-5">
                <div className="col-6">
                  <button className="btn btn-outline-success w-100" onClick={handleCancel}>Cancel</button>
                </div>
                {/* <div className="col-6">
                  <button className="btn btn-success w-100" onClick={handleVerifyOTP}>Verify</button>
                </div> */}
                <div className="col-6">
                  <button className="btn btn-success w-100" onClick={handleVerifyOTP} disabled={isLoading}>
                    {isLoading ? 'Verifying...' : 'Verify '}
                  </button>
                </div>



              </div>
            </div>
          </div>
        </div>
      </div>
      {isLoading && (
        <div className="overlay">
          <div className="loading-spinner"></div>
        </div>
      )}
    </div>
  );
};

export default Otp;
