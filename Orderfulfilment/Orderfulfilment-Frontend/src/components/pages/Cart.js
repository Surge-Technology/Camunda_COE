import React, { useEffect, useState } from "react";

import axios from "axios";

import { Link } from "react-router-dom";

function Cart() {
  const urlParams = new URLSearchParams(window.location.search);

  const cartData = JSON.parse(urlParams.get("cartData"));

  const quantity = JSON.parse(urlParams.get("quantity"));

  const selectedProductId = JSON.parse(
    urlParams.get("selectedProduct.product_ID")
  );

  const [cartItems, setCartItems] = useState(cartData);

  const productList = cartData.productList;

  const removeProduct = async (productId) => {
    const confirmed = window.confirm("Do you want to remove the product?");

    if (confirmed) {
      let data = sessionStorage.getItem("mySessionStorageData");

      data = JSON.parse(data);

      const loggedInUserId = data;

      const username = loggedInUserId.username;

      console.log(username, "Username");

      const jsonData = {
        cartid: username + "_cart",

        userid: username,

        product_ID: productId,

        product_Qnty: quantity,
      };

      try {
        const response = await axios.delete(
          "http://localhost:8080/removeFromCart/",
          { data: jsonData }
        );

        const updatedCartData = response.data;

        const updatedCartItems = cartItems.map((cartItem) => {
          if (
            cartItem.productList.some(
              (product) => product.product_ID === productId
            )
          ) {
            const updatedProductList = cartItem.productList.filter(
              (product) => product.product_ID !== productId
            );

            return {
              ...cartItem,

              productList: updatedProductList,
            };
          }

          return cartItem;
        });

        setCartItems(updatedCartItems);
      } catch (error) {
        console.error("Error:", error);
      }
    }
  };

  useEffect(() => {
    const updatedCartItems = cartItems.map((cartItem) => {
      const updatedProductList = cartItem.productList.map((product) => {
        if (product.product_ID == selectedProductId) {
          const updatedPrice = product.product_Price * quantity;

          return {
            ...product,

            product_Qnty: quantity,

            product_Price: updatedPrice,
          };
        }

        return product;
      });

      return {
        ...cartItem,

        productList: updatedProductList,
      };
    });

    setCartItems(updatedCartItems);
  }, [quantity, selectedProductId]);

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
          {cartItems.map((cartItem, index) => (
            <React.Fragment key={index}>
              {cartItem.productList.map((product, productIndex) => (
                <tr key={productIndex}>
                  <td>{product.product_ID}</td>

                  <td>{product.product_Name}</td>

                  <td>{product.product_Price}</td>

                  <td>
                    {product.product_ID === selectedProductId
                      ? quantity
                      : product.product_Qnty}
                  </td>

                  <td>
                    <button
                      className="btn btn-primary btn-block"
                      onClick={() => removeProduct(product.product_ID)}
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </React.Fragment>
          ))}
        </tbody>
      </table>

      <center>
        <Link
          className="btn btn-primary btn-danger"
          to={`/checkout?cartData=${encodeURIComponent(
            JSON.stringify(cartItems)
          )}&quantity=${encodeURIComponent(JSON.stringify(quantity))}`}
        >
          Checkout
        </Link>
      </center>
    </div>
  );
}

export default Cart;
