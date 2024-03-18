import React, { useState, useEffect } from "react";
import { FaCreditCard } from 'react-icons/fa';

import {FaAmazonPay} from 'react-icons/fa';

import axios from "axios";

import { useNavigate } from "react-router-dom";

const queryString = window.location.search;

const urlParams = new URLSearchParams(queryString);

let value = "";

function Checkout() {
  let navigate = useNavigate();

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    let data = sessionStorage.getItem("mySessionStorageData");

    data = JSON.parse(data);

    console.log(data, "cartItemdata");
  };

  const cartDataParam = urlParams.get("cartData");

  const quantityParam = urlParams.get("quantity");

  const selectedProductParam = urlParams.get("selectedProduct.product_ID");

  if (cartDataParam) {
    const cartData = JSON.parse(decodeURIComponent(cartDataParam));

    if (cartData.length > 0 && cartData[0].productList) {
      const productList1 = cartData[0].productList;

      // Iterate over the product list and create JSON data

      const jsonData1 = productList1.map((product) => ({
        product_ID: product.product_ID,

        product_Name: product.product_Name,

        product_Price: product.product_Price,

        product_Qnty: quantityParam,
      }));

      // Use the jsonData for your API call

      console.log(jsonData1, "itemdata");

      value = jsonData1;
    }
  }

  const [formData, setFormData] = useState({
    name: "",

    cardNumber: "",

    validUpto: "",

    cvv: "",
  });

  const [isPaymentDone, setIsPaymentDone] = useState(false);

  const [productList, setProductList] = useState([]);

  useEffect(() => {
    fetchProductList();
  }, []);

  const fetchProductList = async () => {
    try {
      const response = await axios.get("http://localhost:8080/productList");

      setProductList(response.data);
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const handleChange = (event) => {
    setFormData((prevFormData) => ({
      ...prevFormData,

      [event.target.name]: event.target.value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (event.target.checkValidity()) {
      await performPayment();
    } else {
      alert("Please fill in all the required fields.");
    }
  };

  const performPayment = async () => {
    const jsonData = {
      messageName: "PaymentReceived",

      correlationKey: "54321",
    };

    try {
      const response1 = await axios.post(
        "http://localhost:8080/paymentProcess",

        jsonData
      );

      console.log("Data from API 1:", response1.data);

      console.log(value, "value");

      const response2 = await axios.post(
        "http://localhost:8080/submitProductList",

        value // Pass the value as the request data
      );

      console.log("Data from API 2:", response2.data);

      const processInstanceKey = response2.data.processInstanceKey;

      sessionStorage.setItem("processInstanceKey", processInstanceKey);

      setIsPaymentDone(true);

      setFormData({
        name: "",

        cardNumber: "",

        validUpto: "",

        cvv: "",
      });
    } catch (error) {
      console.error("Error:", error);
    }
  };

  // const resetForm = () => {

  //   setFormData({

  //     name: "",

  //     cardNumber: "",

  //     validUpto: "",

  //     cvv: "",

  //   });

  // };

  const closeModal = () => {
    setIsPaymentDone(false);

    const processInstanceKey = sessionStorage.getItem("processInstanceKey");

    navigate(`/inbox?processInstanceKey=${processInstanceKey}`);
  };

  const [formValues, setFormValues] = useState({
    username: "",

    cardNumber: "",

    expirationMonth: "",

    expirationYear: "",

    cvv: "",
  });

  const [errors, setErrors] = useState({});

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    setFormValues((prevState) => ({
      ...prevState,

      [name]: value,
    }));
  };

  // const handleSubmit = async e => {

  //   e.preventDefault();

  //   try {

  //     const response = await axios.post('http://localhost:8080/paymentProcess', formValues);

  //     console.log('Data from API:', response.data);

  //     // Reset form values

  //     setFormValues({

  //       username: '',

  //       cardNumber: '',

  //       expirationMonth: '',

  //       expirationYear: '',

  //       cvv: ''

  //     });

  //     setErrors({});

  //   } catch (error) {

  //     console.error('Error while calling API:', error);

  //   }

  // };

  return (
    <div className="container py-5">
      <div className="col-lg-6 mx-auto">
        <div className="card">
          <div className="card-header">
            <div className="bg-white shadow-sm pt-4 pl-2 pr-2 pb-2">
              <ul
                role="tablist"
                className="nav bg-light nav-pills rounded nav-fill mb-3"
              >
                <li className="nav-item">
                  <a
                    data-toggle="pill"
                    href="#credit-card"
                    className="nav-link active"
                  >
                  <FaAmazonPay className="mr-4" />
                    <i className="fas fa-credit-card mr-2"></i> Payment
                  </a>
                </li>
              </ul>
            </div>

            <form role="form" onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="username">
                  <h6>Card Owner</h6>
                </label>

                <input
                  type="text"
                  name="username"
                  placeholder="Card Owner Name"
                  required
                  className={`form-control ${
                    errors.username ? "is-invalid" : ""
                  }`}
                  value={formValues.username}
                  onChange={handleInputChange}
                />

                {errors.username && (
                  <div className="invalid-feedback">{errors.username}</div>
                )}
              </div>

              <div className="form-group">
                <label htmlFor="cardNumber">
                  <h6>Card number</h6>
                </label>

                <div className="input-group">
                  <input
                    type="password"
                    className="form-control"
                    id="inputCardNumber"
                    name="cardNumber"
                    placeholder="****************"
                    // style={inputStyle}

                    required
                    pattern="[0-9]{16}"
                    value={formValues.cardNumber}
                    onChange={handleInputChange}
                  />

                  <div className="input-group-append">

              <span className="input-group-text text-muted">

                {/* <i className="fab fa-cc-visa mx-1"></i> */}

                <FaCreditCard className="mr-4" />




<i className="FaCalendar"></i>

              </span>

            </div>
                </div>

                {errors.cardNumber && (
                  <div className="invalid-feedback">{errors.cardNumber}</div>
                )}
              </div>

              <div className="row">
                <div className="col-sm-8">
                  <div className="form-group">
                    <label>
                      <span className="hidden-xs">
                        <h6>Expiration Date</h6>
                      </span>
                    </label>

                    <div className="input-group">
                      <input
                        type="number"
                        placeholder="MM"
                        name="expirationMonth"
                        className={`form-control ${
                          errors.expirationMonth ? "is-invalid" : ""
                        }`}


                        required
                        pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
                        value={formValues.expirationMonth}
                        onChange={handleInputChange}
                      />

                      <input
                        type="number"
                        placeholder="YY"
                        name="expirationYear"
                        pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
                        className={`form-control ${
                          errors.expirationYear ? "is-invalid" : ""
                        }`}
                        required

                        value={formValues.expirationYear}
                        onChange={handleInputChange}
                      />
                    </div>

                    {errors.expirationMonth && (
                      <div className="invalid-feedback">
                        {errors.expirationMonth}
                      </div>
                    )}

                    {errors.expirationYear && (
                      <div className="invalid-feedback">
                        {errors.expirationYear}
                      </div>
                    )}
                  </div>
                </div>

                <div className="col-sm-4">
                  <div className="form-group mb-4">
                    <label
                      data-toggle="tooltip"
                      title="Three digit CV code on the back of your card"
                    >
                      <h6>
                        CVV <i className="fa fa-question-circle d-inline"></i>
                      </h6>
                    </label>

                    <input
                      type="text"
                      name="cvv"
                      required
                      className={`form-control ${
                        errors.cvv ? "is-invalid" : ""
                      }`}
                      pattern="[0-9]{3}"
                      value={formValues.cvv}
                      onChange={handleInputChange}
                    />

                    {errors.cvv && (
                      <div className="invalid-feedback">{errors.cvv}</div>
                    )}
                  </div>
                </div>
              </div>

              <div className="card-footer">
                <center>
                  <button className="subscribe btn btn-primary btn-block shadow-sm">
                    Confirm Payment
                  </button>
                </center>
              </div>
            </form>
          </div>
        </div>
      </div>

      {isPaymentDone && (
        <div
          style={{
            position: "fixed",

            top: 0,

            left: 0,

            right: 0,

            bottom: 0,

            display: "flex",

            justifyContent: "center",

            alignItems: "center",

            backgroundColor: "rgba(0, 0, 0, 0.5)",
          }}
        >
          <div
            style={{
              background: "#fff",

              padding: "20px",

              borderRadius: "5px",
            }}
          >
            <h2>Payment Successful!</h2>

            <center>
              {" "}
              <p> Thank you for your payment </p>{" "}
            </center>

            <center>
              {" "}
              <button onClick={closeModal}>Close</button>
            </center>
          </div>
        </div>
      )}
    </div>
  );
}

export default Checkout;
