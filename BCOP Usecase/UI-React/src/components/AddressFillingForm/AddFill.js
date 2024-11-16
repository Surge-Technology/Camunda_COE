import React, { useState } from "react";

import { Form, Button, Container, Row, Col } from "react-bootstrap";

import './AddressFillForm';

import Axios from "axios";



const AddFill = () => {


    const [data, setData] = useState({

        address: '',
        landmark:'',
        city:'',
        state:''

           })



    const { address,landmark,city,state } = data;



    const onSubmit = (e) => {

        e.preventDefault();



        let processInstanceKey = sessionStorage.getItem("key");

        console.log("dataaaa", processInstanceKey);



        const result = Axios.post(`http://localhost:8080/completeTaskWithInstanceId/${processInstanceKey}`, data)

        console.log('resulttt' + JSON.stringify(result));



    }



    const onInputChangeInt = (e) => {

        setData({ ...data, [e.target.name]: parseInt(e.target.value) || 0 });



    };



    const onInputChange = (e) => {

        setData({ ...data, [e.target.name]: e.target.value });

    };





    return (

        <div className="container">

            <center>

                <h2>Personal Details</h2>

            </center>

            <form className="personalform" onSubmit={onSubmit}>

                <div className="form-group row">

                    <label htmlFor="address" className="col-sm-2 col-form-label">

                        <strong>Address</strong>

                    </label>

                    <div className="col-sm-8">

                        <input

                            type="text"

                            className="form-control"

                            id="address"

                            placeholder="Address"

                            name="address"

                            value={address}

                            onChange={(e) => onInputChange(e)}

                        ></input>

                    </div>

                </div>



                <div className="form-group row">

                    <label htmlFor="landmark" className="col-sm-2 col-form-label">

                        <strong>Landmark</strong>

                    </label>

                    <div className="col-sm-8">

                        <input

                            type="text"

                            className="form-control"

                            id="landmark"

                            placeholder="Landmark"

                            name="landmark"

                            value={landmark}

                            onChange={(e) => onInputChange(e)}

                        ></input>

                    </div>

                </div>







                <div className="form-group row">

                    <label htmlFor="city" className="col-sm-2 col-form-label">

                        <strong>city</strong>

                    </label>

                    <div className="col-sm-8">

                        <input

                            type="text"

                            className="form-control"

                            id="city"

                            placeholder="City"

                            name="city"

                            value={city}

                            onChange={onInputChangeInt}

                            required

                        ></input>

                    </div>

                </div>

                <div className="form-group row">

                    <label htmlFor="state" className="col-sm-2 col-form-label">

                        <strong>State</strong>

                    </label>

                    <div className="col-sm-8">

                        <input

                            type="text"

                            className="form-control"

                            id="state"

                            placeholder="State"

                            name="state"

                            value={state}

                            onChange={(e) => onInputChange(e)}

                        ></input>

                    </div>

                </div>


                <center>

                    <Button variant="primary" type="submit">

                        Submit

                    </Button>

                </center>

            </form>

        </div>

    );

};



export default AddFill;