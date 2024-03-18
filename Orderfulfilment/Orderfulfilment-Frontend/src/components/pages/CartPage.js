import React, { useEffect, useState } from "react";
import axios from "axios";

const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    const data = JSON.parse(sessionStorage.getItem("mySessionStorageData"));
    const loggedInUserId = data.username;
    console.log(loggedInUserId)
    const response = await axios.get(`http://localhost:8080/getAddToCartDetail/${loggedInUserId}`);
    const updatedCartItems = response.data.map((product) => ({
      ...product,
      quantity: product.product_Qnty, // Assign product_Qnty to quantity
    }));
    setCartItems(updatedCartItems);
    console.log(JSON.stringify(response.data));
  };

  const removeProduct = async (productId) => {
    const confirmed = window.confirm('Do you want to remove the product?');
    if (confirmed) {
      let data = sessionStorage.getItem("mySessionStorageData");
      data = JSON.parse(data);
      console.log(data, "shoppagedata");

      const loggedInUserId = data;
      const username = loggedInUserId.username;

      console.log(username, "Username");

      const jsonData = {
        cartid: username + "_cart",
        userid: username,
        product_ID: productId,
        product_Qnty: cartItems.find((item) => item.product_ID === productId).quantity,
      };

      try {
        const response = await axios.delete('http://localhost:8080/removeFromCart/', { data: jsonData });
        const updatedCartData = response.data;
        const updatedCartItems = cartItems.map((cartItem) => {
          if (cartItem.product_ID === productId) {
            return {
              ...cartItem,
              productList: cartItem.productList.filter((product) => product.product_ID !== productId),
            };
          }
          return cartItem;
        });
        setCartItems(updatedCartItems);
      } catch (error) {
        console.error('Error:', error);
      }
    }
  };

  return (
    <div className="container">
      <center>
        <h1>Cart</h1>
      </center>

      <table className="table">
        <thead>
          <tr className="rowdata">
            <th>Product Id</th>
            <th>Product Name</th>
            <th>Product Price</th>
            <th>Product Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {cartItems.map((user) => (
            <tr key={user.product_ID}>
              <td>{user.product_ID}</td>
              <td>{user.product_Name}</td>
              <td>{user.product_Price}</td>
              <td>{user.quantity}</td>
              <td>
                <button
                  className="btn btn-success m-2"
                  onClick={() => removeProduct(user.product_ID)}
                >
                  Remove
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <center>
        <button className="btn btn-primary btn-danger">Checkout</button>
      </center>
    </div>
  );
};

export default CartPage;
