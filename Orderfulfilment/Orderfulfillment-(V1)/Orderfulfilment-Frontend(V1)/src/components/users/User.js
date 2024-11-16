import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const User = () => {
  const navigate = useNavigate();
  const [variableValues, setProductList] = useState([]);

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
    let key = sessionStorage.getItem("processInstanceKey");
  console.log(key);
    try {
      const result = await axios.get(
        `http://localhost:8080/getAssignedTaskByAssignee/OrderFullfillment/murali.muthu@surgetechinc.in/${key}`
      );
      const apiResponse = result.data[0];
      console.log(apiResponse);
      console.log(apiResponse.id,'apiiddat');

      const variable = apiResponse?.variable;
      console.log(variable, "variable");
      if (variable) {
        const productValues = variable[0]?.value || []; // Access the value array inside the variable object
      console.log(productValues, "productValues");
      console.log(apiResponse.id,"idvalue");
      setProductList(productValues);
      setTaskId(apiResponse.id);
      console.log(taskId,'taskId...');


      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const onSubmit = async e => {

    e.preventDefault();

    let stockValue = selectedOption === "Approve" ? "yes" : "no";
    //alert(selectedOption,"selectedOption");

    const stock = {
      stockAvailable: stockValue,
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
            {variableValues.map((product, index) => (
              <tr key={index}>
                <td>{product.product_Name}</td>
                <td>{product.product_Price}</td>
                <td>{product.product_Qnty}</td>
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
