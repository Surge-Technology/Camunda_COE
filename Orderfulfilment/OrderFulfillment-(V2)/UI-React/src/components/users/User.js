import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const User = () => {
  const navigate = useNavigate();
  const [variableValues, setProductList] = useState([]);
  const [dataItems, setDataItems] = useState([]);

  const [selectedOption, setSelectedOption] = useState("");
  const [taskId, setTaskId] = useState("");

  const handleOptionChange = (e) => {
    setSelectedOption(e.target.value);
   // alert(setSelectedOption,"setSelectedOption");
  };

  useEffect(() => {
    loadUser();
  }, []);
  const loadUser = async () => {
    try {
      const data = JSON.parse(sessionStorage.getItem("mySessionStorageData")); // Get user data from sessionStorage
      const loggedInUserId = data.username; // Extract the logged-in user ID
      console.log("Logged in User ID:", loggedInUserId); // Log the user ID
 
      // Fetch cart details from API
      const response = await axios.get(`http://localhost:8080/api/getAddToCartDetail/Alex`);
      console.log("API Response:", response.data); // Log the response data
            // setTaskId(apiResponse.id);

      // Map response data to add the quantity field
      const updatedCartItems = response.data.map((product) => ({
        ...product,
        productQuantity: product.productQuantity, // Assign product_Qnty to quantity
      }));
      setDataItems(updatedCartItems); // Update the cart items state
    } catch (error) {
      console.error("Error loading cart data:", error);
    }
  };
  const loadUser1 = async () => {
  //   let key = sessionStorage.getItem("processInstanceKey");
  // console.log(key);
    try {
      // const result = await axios.get(
      //   `http://localhost:8080/getAssignedTaskByAssignee/OrderFullfillment/murali.muthu@surgetechinc.in/${key}`
      // );
      const result = await axios.get(`http://localhost:8080/api/getAddToCartDetail/Alex`);

      const apiResponse = result.data;
      console.log(apiResponse);
      // console.log(apiResponse.id,'apiiddat');
      // setTaskId(apiResponse.id);
      // const variables = apiResponse?.variable[1];
      // console.log(variables, "variable");
      // if (variables) {
      //   console.log(variables, "variable---");
      //    const productValues = variables?.value|| []; // Access the value array inside the variable object
      // console.log(variables?.value , "productValues");
      // console.log(apiResponse.id,"idvalue");
      //  setProductList(productValues);
      
      // console.log(taskId,'taskId...');


      // }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const onSubmit = async e => {

    e.preventDefault();

    let stockValue = selectedOption === "Approve" ? "yes" : "no";
    //alert(selectedOption,"selectedOption");

    const stock = {
      customerAvailable: stockValue,
    };

    // let stockValue = "yes";
    // if (selectedOption === "Approve") {
    //   stockValue = "yes";
    // } else if (selectedOption === "Reject") {
    //   stockValue = "no";
    // }
  
    // const stock = {
    //   "stockAvailable": stockValue
    // };
    let taskId=  sessionStorage.getItem('taskId');

    const result = await axios.post(
      `http://localhost:8080/completeTaskWithInstanceId/${taskId}`,
      stock
    );
    console.log(stock,'stock');
    console.log(taskId, "taskId");
    console.log(result, "stock");
    navigate("/inbox");

  //   e.preventDefault();
  //   const stock ={
  //    // "stockAvailable":"yes"
  //    stockAvailable: selectedOption === "Approve" ? "yes" : "no"
  // }

  
  //   const result=await axios.post(`http://localhost:8080/completeTaskWithInstanceId/${taskId}`,stock);
  //  console.log(taskId,'taskId');
  //  console.log(result,'stock');
  //   navigate("/inbox");
};

  return (
    <div className="container">
      <div className="w-75 auto shadow p-5 ">
        <h2 className="text-center mb-4">Order Details</h2>
        <table className="table">
          <thead>
            <tr className="bg-dark text-white">
              <th scope="col">Product Name</th>
              <th scope="col">Product Price</th>
              <th scope="col">Quantity</th>
            </tr>
          </thead>
          <tbody>
            {dataItems.map((product, index) => (
              <tr key={index}>
                <td>{product.productName}</td>
                <td>{product.productPrice}</td>
                <td>{product.productQuantity}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className="form-group">
          {/* <label htmlFor="exampleFormControlSelect1"></label> */}
          <select
            className="form-control"
            id="exampleFormControlSelect1"
             value={selectedOption}
             onChange={handleOptionChange}
          >
             <option value="select">select</option>
            <option value="Approve">Approve</option>
            <option value="Reject">Reject</option>
          </select>
        </div>
        <center>
          <button className="btn btn-warning btn-block m-2" onClick={(e) => onSubmit(e)}>Submit</button>
          <button className="btn btn-secondary" onClick={() => navigate("/inbox")}>
            Close
          </button>
        </center>
      </div>
    </div>
  );
};

export default User;
