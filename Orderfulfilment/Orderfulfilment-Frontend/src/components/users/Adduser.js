import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import React from "react";

const Adduser = () => {
  let navigate = useNavigate();

  const [user, setuser] = useState({
    product_ID: "",
    product_Name: "",
    product_Price: 0,
    product_Qnty: 0,
    stock: 0,
  });

  const close = () => {
    navigate("/home");
  };

  const { product_ID, product_Name, product_Price, product_Qnty, stock } = user;

  //destructor-->e.target.value get kr k user mai set krega
  const onInputChange = (e) => {
    setuser({ ...user, [e.target.name]: e.target.value });
  };

  const onInputChangeStock = (e) => {
    setuser({ ...user, [e.target.name]: parseInt(e.target.value) || 0 });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/createProductByAdmin", user);
    //api hit krne k baad vo kaha jayega says history.push
    navigate("/home");
  };

  return (
    <div className="container">
      <div className="w-75 mx-auto shadow p-5">
        <h2 className="text-center mb-4">Add A Product</h2>
        <form onSubmit={(e) => onSubmit(e)}>
          {/* <div class="form-group row">
            <label for="inputEmail3" class="col-sm-2 col-form-label"> Email</label>
            <div class="col-sm-10">
              <input type="email" class="form-control" id="inputEmail3" placeholder="Email"/>
            </div>
          </div> */}

          <div className="form-group row">
            <label  class="col-sm-2 col-form-label">Enter Product ID</label>
            <div class="col-sm-10">
            <input
              type="text"
              className="form-control"
              placeholder="Enter Product ID"
              name="product_ID"
              value={product_ID}
              onChange={(e) => onInputChange(e)}
            /></div>
          </div>
          <div className="form-group row">
            <label class="col-sm-2 col-form-label">Enter Product Name</label>
            <div class="col-sm-10">
            <input
              type="text"
              className="form-control form-control"
              placeholder="Enter Your Product Name"
              name="product_Name"
              value={product_Name}
              onChange={(e) => onInputChange(e)}
            /></div>
          </div>
          <div className="form-group row">
            <label class="col-sm-2 col-form-label">Enter Price</label>
            <div class="col-sm-5">
            <input
              type="text"
              className="form-control form-control m-1"
              placeholder="Enter Product Price"
              name="product_Price"
              value={product_Price}
              onChange={(e) => onInputChangeStock(e)}
            /></div>
          </div>
          <div className="form-group row">
            <label class="col-sm-2 col-form-label">Enter Quantity</label>
            <div class="col-sm-5">
            <input
              type="number"
              className="form-control form-control"
              placeholder="Enter Quantity"
              name="product_Qnty"
              value={product_Qnty}
              onChange={(e) => onInputChangeStock(e)}
            /></div>
          </div>
          <div className="form-group row">
            <label class="col-sm-2 col-form-label">Enter Stock</label>
            <div class="col-sm-5">
            <input
              type="text"
              className="form-control form-control m-1"
              placeholder="Enter Stock"
              name="stock"
              value={stock}
              onChange={(e) => onInputChangeStock(e)}
            /></div>
          </div>
          <center>
            <button className="btn btn-info m-2">Add Product</button>
            <button className="btn btn-secondary" onClick={() => close()}>
              Close
            </button>
          </center>
        </form>

        <div></div>
      </div>
    </div>
  );
};

export default Adduser;
