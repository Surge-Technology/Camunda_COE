import React, { useState, useEffect } from "react";

import axios from "axios";

import { Link } from "react-router-dom";

import "./Popup.css";

const Shop = () => {
  const [users, setUsers] = useState([]);

  const [selectedProduct, setSelectedProduct] = useState(null);

  const [quantity, setQuantity] = useState();

  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    const result = await axios.get(
      "http://localhost:8080/getAllProductDeatils"
    );

    setUsers(result.data);

    let data = sessionStorage.getItem("mySessionStorageData");

    data = JSON.parse(data);

    console.log(data, "shoppagedata");
  };

  const handleClick = (product) => {
    setSelectedProduct(product);

    setIsOpen(true);

    setQuantity(); // Reset quantity to default (1) when a new product is selected
  };

  const handleQuantityChange = (event) => {
    setQuantity(parseInt(event.target.value));
  };

  const addToCart = () => {
    let data = sessionStorage.getItem("mySessionStorageData");

    data = JSON.parse(data);

    const loggedInUserId = data;

    const username = loggedInUserId.username;

    console.log(username, "Username");

    const jsonData = {
      cartid: username + "_cart",

      userid: username,

      product_ID: selectedProduct.product_ID,

      product_Qnty: quantity,
    };

    const config = {
      headers: {
        "Content-Type": "application/json",
      },
    };

    axios

      .post("http://localhost:8080/addToCart", jsonData, config)

      .then((response) => {
        const cartData = response.data;

        const url = `/Cart?cartData=${JSON.stringify(
          cartData
        )}&quantity=${quantity}&selectedProduct.product_ID=${
          selectedProduct.product_ID
        }`;

        window.location.href = url; // Navigate to the Cart page

        console.log("Response:", response.data);
      })

      .catch((error) => {
        console.error("Error:", error);
      });
  };

  return (
    <div className="container">
      <center>
        <h1>Product List Page</h1>
      </center>

      {isOpen && (
        <div className="popup">
          <div className="popup-inner">
            <center>
              <h3>Product Details</h3>

              {selectedProduct && (
                <>
                  <p>Product Name: {selectedProduct.product_Name}</p>

                  <p>
                    Product Quantity:
                    <input
                      className="productquantity"
                      type="number"
                      width={10}
                      value={quantity}
                      onChange={handleQuantityChange}
                      min="1"
                    />
                  </p>

                  <div className="popup-buttons">
                    <Link
                      className="btn btn-primary"
                      style={{ width: "30%", margin: "0 10px" }}
                      onClick={addToCart}
                    >
                      Add
                    </Link>

                    <button
                      className="btn btn-primary btn-danger"
                      onClick={() => setIsOpen(false)}
                    >
                      Close
                    </button>
                  </div>
                </>
              )}
            </center>
          </div>
        </div>
      )}

      <table className="table">
        <thead>
          <tr className="text-light bg-dark">
            <th scope="col">Product Id</th>

            <th scope="col">Product Name</th>

            <th scope="col">Product Price</th>

            <th scope="col">Actions</th>
          </tr>
        </thead>

        <tbody>
          {users.map((user) => (
            <tr key={user.product_ID}>
              <td className="py-2 px-5">{user.product_ID}</td>

              <td>{user.product_Name}</td>

              <td>{user.product_Price}</td>

              <td>
                <button
                  className="btn btn-success m-2"
                  onClick={() => handleClick(user)}
                >
                  Add
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Shop;
