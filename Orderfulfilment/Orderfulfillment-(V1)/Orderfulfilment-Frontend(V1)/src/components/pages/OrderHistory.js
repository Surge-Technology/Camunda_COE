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
    let key = sessionStorage.getItem("processInstanceKey");//2251799824119672
    try {
      const response = await axios.get(`http://localhost:8080/orderHistory/murali.muthu@surgetechinc.in/OrderFullfillment/${key}`);
      const ordersData = response.data.map(order => {
        const { id, creationTime, taskState,totalPrice } = order;
        const productNames = order.variables[0].value.map(product => product.product_Name);
        return { id, creationTime, taskState, productNames,totalPrice };
      });
      setOrders(ordersData);
    } catch (error) {
      console.error("Error fetching order history:", error);
    }
  };

  const CancelOrder = async() =>{
    const cancel = await axios.get('http://localhost:8080/orderCancelled');
    console.log(cancel.data[0]);
    alert('Are you sure you want to cancel the order')
    navigate("/shop")
  }

  return (
    <div>
      <h1 className="pt-2 pb-3 px-6 border-bottom border-secondary-light">Order History</h1><br/>
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
          {orders.map(order => (
            <tr key={order.id}>
              <td >{order.id}</td>
              <td>{order.creationTime}</td>
              <td>{order.productNames.join(", ")}</td>
              <td>{order.totalPrice}</td>
              <td className="py-2 px-3"><span className="badge bg-success">{order.taskState}</span></td>
              <td><button className="btn btn-danger btn-sm" onClick={() => CancelOrder()} >Cancel Order</button></td>
              {/* onClick={() => CancelOrder()} */}
            </tr>
          ))}
        </tbody>
      </table> 
      
    </div>
  );
};

export default OrderHistory;
