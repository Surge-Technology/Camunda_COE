import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
 import swal from "sweetalert";
const CartPage = () => {
  const [cartItems, setCartItems] = useState([]);
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);

 const [productIds,setProductIds]=useState([]);
  // Load user data and fetch cart items on component load
  useEffect(() => {
    loadUser();
  }, []);
 
  const loadUser = async () => {
    try {
      const data = JSON.parse(sessionStorage.getItem("mySessionStorageData")); // Get user data from sessionStorage
      const loggedInUserId = data.username;
      console.log("Logged in User ID:", loggedInUserId); 
      // setProducts(response.data);

      // Fetch cart details from API
      const response = await axios.get(`http://localhost:8080/api/getAddToCartDetail/${loggedInUserId}`);
      console.log("API Response:", response.data); 

      const productIdsArray = response.data.map(item => item.productId);
      setProductIds(productIdsArray);
      console.log("Product IDs:", productIdsArray);

      // Map response data to add the quantity field
      const updatedCartItems = response.data.map((product) => ({
        ...product,
        // productQuantity: product.productQuantity, // Assign product_Qnty to quantity
      }));
       setCartItems(updatedCartItems); // Update the cart items state
    } catch (error) {
      console.error("Error loading cart data:", error);
    }
  };
 
  // Function to remove product from cart
  const removeProduct = async (productId) => {
    const confirmed = window.confirm("Do you want to remove the product?");
    if (confirmed) {
      try {
        const data = JSON.parse(sessionStorage.getItem("mySessionStorageData")); // Fetch user data
        const loggedInUserId = data.username;
 
        const jsonData = {
          // cartid: loggedInUserId + "_cart",
          userid: loggedInUserId,
          product_ID: productId,
          // productQuantity: cartItems.find((item) => item.product_ID === productId).quantity,
        };
        
 
        const response = await axios.delete("http://localhost:8080/api/removeFromCart/", { data: jsonData });
        console.log("Product removed:", response.data);
         loadUser();
 
         } catch (error) {
        console.error("Error removing product:", error);
      }
    }
  };
 
  // Function to handle checkout process
  const goToCheckout = async (productId) => {
    const data = JSON.parse(sessionStorage.getItem("mySessionStorageData")); // Fetch user data
        const loggedInUserId = data.username;
 
    const jsonData = {
      userId: loggedInUserId,
      product_ID: productIds,
      // productQuantity: cartItems.find((item) => item.product_ID === productId).quantity,
    };
   console.log("json Data",jsonData);
   
    try {
      // Post the cart items to the checkout API
      const response2 = await axios.post("http://localhost:8080/api/submitProductList",jsonData );
      console.log("Checkout API Response:", response2.data);
      console.log("Checkout cartItems:", cartItems);
 
      const processInstanceKey = response2.data.processInstanceKey;
      sessionStorage.setItem("processInstanceKey", processInstanceKey);
console.log("finish",processInstanceKey);

      swal({
        title: "Thank You for Your Order",
        text: "An email with your product details has been sent. Please check your email to complete payment for your items. Thank you!",
        icon: "success",
        button: "ok",
      }).then(() => {
        // Navigate to shop page after user closes the popup
        navigate('/shop');
      });     // navigate(`/inbox?processInstanceKey=${processInstanceKey}`);
    } catch (error) {
      console.error("Error submitting product list:", error);
      swal({
        title: "Error!",
        text: "There was an issue with your checkout. Please try again.",
        icon: "error",
        button: "Close",
      });
    }
  };
  let key = sessionStorage.getItem("processInstanceKey");
console.log("------------------>key",key);

 
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
            <tr key={user.productId}>
              <td>{user.productId}</td>
              <td>{user.productName}</td>
              <td>{user.productPrice}</td>
              <td>{user.productQuantity}</td>
             
              <td>
                <button
                  className="btn btn-success m-2"
                  onClick={() => removeProduct(user.productId)}
                >
                  Remove
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
 
      <center>
        <button className="btn btn-primary btn-danger" onClick={goToCheckout}>
          Checkout
        </button>
      </center>
    </div>
  );
};
 
export default CartPage;