import React, { useState, useEffect } from 'react';

const EmailOTPVerification = () => {
  const [otpValue, setOTPValue] = useState('');

  useEffect(() => {
    // Delay the GET API call by 5 seconds
    const timer = setTimeout(() => {
      fetchOTPFromAPI();
    }, 5000);

    // Cleanup function to clear the timer when the component unmounts
    return () => clearTimeout(timer);
  }, []);

  const fetchOTPFromAPI = () => {
    fetch('http://localhost:8080/getOTP/{otp}')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setOTPValue(data.OTP); // Save the OTP value in the state
      })
      .catch((error) => {
        console.error('Error fetching OTP data:', error);
        // Handle error, e.g., show an error message to the user
      });
  };

  const handleVerifyOTP = (event) => {
    event.preventDefault();
    // Your verification logic goes here
  };

  return (
    <div className="container">
      <h2>Email OTP Verification</h2>
      <form onSubmit={handleVerifyOTP}>
        <label htmlFor="otp">Enter OTP</label>
        <input
          type="text"
          id="otp"
          placeholder="Enter OTP received in email"
          value={otpValue}
          onChange={(e) => setOTPValue(e.target.value)}
          required
        />

        <button type="submit">Verify OTP</button>
      </form>
    </div>
  );
};

export default EmailOTPVerification;
