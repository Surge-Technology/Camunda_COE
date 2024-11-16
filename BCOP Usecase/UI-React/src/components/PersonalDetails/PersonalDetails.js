import React, { useState } from "react";

import { Form, Button, Container, Row, Col } from "react-bootstrap";

import "./PersonalDetails.css";

import Axios from "axios";

import { useHistory } from "react-router-dom";



const PersonalDetails = (props) => {

  const history = useHistory();
  const [isLoading, setIsLoading] = useState(false);

  const [data, setData] = useState({

    firstName: "",

    lastName: "",

    dob: "",
    gender: "",

    age: 0,

    annualIncome: 0,
    // creditScore:0,
    accountType: "",
    phoneNo: 0,
    bpmnProcessId:"Personal_Details"
  });


 // const [firstName, setFirstName] = useState(""); // Initialize it with an empty string

  const {

    age,

    annualIncome,

    firstName,

    lastName,

    dob,
    gender = gender.toString(),

    accountType: selected,
    phoneNo,

   creditScore,

  } = data;

  // const [accountType, setAccountType] = useState("");
  // const [selected, setSelected] = useState('');


  const onSubmit = async (event) => {
    setIsLoading(true);

    event.preventDefault();



    let activeTaskId = sessionStorage.getItem("jobKey");

    console.log("activeTaskId", activeTaskId);

    data.activeTaskId = activeTaskId;

    let processInstanceKey = sessionStorage.getItem("key");

    console.log("processInstanceKey", processInstanceKey);

    const result = await Axios.post(
      `http://localhost:8080/completeUserTask/${processInstanceKey}`,
      data

    );
  
   console.log("resultFromApi" + JSON.stringify(result));
   console.log(firstName)
console.log("id------------"+result.data.id)

const userId=sessionStorage.setItem('clientUserId', result.data.id);
console.log("userId",userId);

//alert("creditScore "+result.data.creditScore);

   
  //  if (result.data && result.data.firstName) {
  //   // Set the firstName in session storage
  //   const firstName = result.data.extractedInfo.firstName;
  //   sessionStorage.setItem("firstName", firstName);
  //   console.log("firstName", firstName); // Log the firstName

  //   // Navigate to the success page and pass firstName as a query parameter
  //   props.history.push(`/success?firstName=${firstName}`);
  // } else {
  //   console.log("firstName not found in result.data");
  // }


    // const localTaskNames = result.data.extractedInfo.localTaskName;
    // const jobKey = result.data.extractedInfo.jobKey;
    sessionStorage.setItem("jobKey", "2312321");
  //  if(creditScore > 700){
  //   props.history.push('/success');
  //  }else if (creditScore < 500){
  //   props.history.push('/registrationReject');
  //  }else{
  //   props.history.push('/Popup');
  //  }
  let baseScore = 300;

  let ageFactor = 10;
  let ageScore = age * ageFactor;
  let incomeScore = annualIncome / 1000;
  let creditScore = baseScore + ageScore + Math.floor(incomeScore);
  // const creditScore = result.data.creditScore;
  console.log("creditScore", creditScore);
  if (creditScore > 700) {

    history.push("/success");

  } else if (creditScore >= 500 && creditScore <= 700) {

    history.push("/Popup");

  } else {

    history.push("/registrationReject");

  }

  //props.history.push('happyPath')





    setIsLoading(false);

  };



  const onInputChangeInt = (e) => {

    setData({ ...data, [e.target.name]: parseInt(e.target.value) || 0 });

  };

  const onInputChange = (e) => {

    setData({ ...data, [e.target.name]: e.target.value });


  };

  const handleGenderChange = (event) => {
    setData({ ...data, gender: event.target.value });
  };

  const handleAccountTypeChange = (event) => {
    setData({ ...data, accountType: event.target.value });
  };


  const handleChange = event => {
    console.log(event.target.value);


  };



  return (
    <div className="personForm">
      <div className="container">
        <br />

        {isLoading && (
          <div className="overlay">
            <div className="loading-spinner"></div>
          </div>
        )}
        <center>

          <h2>Personal Details</h2>

        </center>
        <br />


        <form className="personalform" onSubmit={onSubmit}>

          <div className="form-group row">

            <label htmlFor="firstName" className="col-sm-2 col-form-label">

              <strong>First Name</strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="firstName"

                placeholder="First Name"

                name="firstName"

                value={firstName}

                onChange={(e) => onInputChange(e)}

              ></input>

            </div>

          </div>



          <div className="form-group row">

            <label htmlFor="lastName" className="col-sm-2 col-form-label " >

              <strong>Last Name</strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="lastName"

                placeholder="Last Name"

                name="lastName"

                value={lastName}

                onChange={(e) => onInputChange(e)}

              ></input>

            </div>

          </div>



          <div className="form-group row">

            <label htmlFor="age" className="col-sm-2 col-form-label">

              <strong > Age <span className="star">*</span></strong>

            </label>

            <div className="col-sm-8">

            <input
  type="text"
  className="form-control"
  id="age"
  placeholder="Age"
  name="age"
  value={age}
  onChange={(e) => {
    // Use regular expression to allow only three-digit numbers
    const inputAge = e.target.value.replace(/\D/g, '').slice(0, 3); // Remove non-digit characters and limit to 3 digits
    onInputChangeInt({ target: { name: 'age', value: inputAge } }); // Call your existing onChange handler
  }}
  required
/>


            </div>

          </div>



          <div className="form-group row">

            <label htmlFor="dob" className="col-sm-2 col-form-label">

              <strong>DOB</strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="dob"

                placeholder="Date of Birth"

                name="dob"

                value={dob}

                onChange={(e) => onInputChange(e)}

              ></input>

            </div>

          </div>


          {/* <div className="form-group row">

            <label htmlFor="phoneNo" className="col-sm-2 col-form-label">

              <strong>Phone Number</strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="phoneNo"

                placeholder="Phone Number"

                name="phoneNo"

                value={phoneNo}

                onChange={(e) => onInputChangeInt(e)}

              ></input>

            </div>

          </div> */}

          <div className="form-group row">

            <label htmlFor="phoneNo" className="col-sm-2 col-form-label">

              <strong>Phone No</strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="phoneNo"

                placeholder="Enter PhoneNo"

                name="phoneNo"

                value={phoneNo}

                onChange={(e) => onInputChangeInt(e)}

              ></input>

            </div>

          </div>

          <div className="form-group row">
            <label htmlFor="gender" className="col-sm-2 col-form-label">
              <strong>Gender</strong>
            </label>
            <div className="col-sm-8">
              <select
                className="form-control"
                id="gender"
                name="gender"
                value={gender}
                onChange={(e) => handleGenderChange(e)}
              >
                <option value="">Select Gender</option>
                <option value="male">Male</option>
                <option value="female">Female</option>
                <option value="other">Other</option>
              </select>
            </div>
          </div>

          <div className="form-group row">

            <label htmlFor="annualIncome" className="col-sm-2 col-form-label">

              <strong>annualIncome <span className="star" >*</span></strong>

            </label>

            <div className="col-sm-8">

              <input

                type="text"

                className="form-control"

                id="annualIncome"

                placeholder="Annual Income"

                name="annualIncome"

                value={annualIncome}

                onChange={(e) => onInputChangeInt(e)}

              ></input>

            </div>

          </div>

          <div className="form-group row">

            <label htmlFor="accountType" className="col-sm-2 col-form-label">

              <strong>AccountType</strong>

            </label>
            <br /><br />
            <div className="col-sm-8" style={{ display: 'flex', alignItems: 'center' }}>

              <input
                type="radio"
                id="savings"
                name="choose"
                value="saving"
                checked={selected === 'saving'}
                onChange={handleAccountTypeChange}
              />
              <label htmlFor="saving" >Savings</label>
              <span style={{ margin: '0 10px' }}></span>

              <input
                type="radio"
                id="current"
                name="choose"
                value="current"
                onChange={handleChange}
                checked={selected === 'current'}
              />
              <label htmlFor="current">Current</label>
            </div>
          </div>

          <center>

            <Button variant="primary" type="submit">

              Submit

            </Button>

          </center>

        </form>

      </div>
    </div>
  );

};



export default PersonalDetails;