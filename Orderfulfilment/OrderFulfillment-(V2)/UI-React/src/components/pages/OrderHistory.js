import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const OrderHistory = () => {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    let key = sessionStorage.getItem("processInstanceKey");
    try {
      const response = await axios.get(`http://localhost:8080/orderHistory/murali.muthu@surgetechinc.in/OrderFullfillment/${key}`);
      
      let responseData = response.data;

      // Check if the response is an object (single order) or an array (multiple orders)
      if (!Array.isArray(responseData)) {
        responseData = [responseData]; // Convert single object to array
      }

      const ordersData = responseData.map(order => {
        const { orderId, paymentStatus, productDetail } = order;
        const productNames = productDetail.inputVaribale.map(product => product.product_Name); // Extract product names
        const totalPrice = productDetail.inputVaribale.reduce((total, product) => total + product.product_Price, 0); // Calculate total price

        return { 
          id: orderId, 
          creationTime: new Date().toLocaleString(), 
          taskState: paymentStatus, 
          productNames, 
          totalPrice 
        };
      });

      setOrders(ordersData);
    } catch (error) {
      console.error("Error fetching orders", error);
    }
  };

  const CancelOrder = async () => {
    const isConfirmed = window.confirm('Are you sure you want to cancel the order?');
    if (isConfirmed) {
      try {
        const cancel = await axios.get('http://localhost:8080/orderCancelled');
        console.log(cancel.data[0]);
        navigate("/shop");
      } catch (error) {
        console.error('Error cancelling order:', error);
      }
    }
  };

  return (
    <div>
      <h1 className="pt-2 pb-3 px-6 border-bottom border-secondary-light">Order History</h1>
      <br/>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">ORDER NUMBER</th>
            <th scope="col">DATE</th>
            <th scope="col">ITEMS PURCHASED</th>
            <th scope="col">TOTAL PRICE</th>
            <th scope="col">STATUS</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders.map(order => (
              <tr key={order.id}>
                <td>{order.id}</td>
                <td>{order.creationTime}</td>
                <td>{order.productNames.join(", ")}</td>
                <td>{order.totalPrice}</td>
                <td className="py-2 px-3">
                  <span className="badge bg-success">{order.taskState}</span>
                </td>
                <td>
                  <button className="btn btn-danger btn-sm" onClick={() => CancelOrder()}>
                    Cancel Order
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">No orders found</td>
            </tr>
          )}
        </tbody>
      </table> 
    </div>
  );
};

export default OrderHistory;
