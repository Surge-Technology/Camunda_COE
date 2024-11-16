

import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link,  } from "react-router-dom";
import Edituser from '../users/Edituser';

const Home = () => {
  const [user, setUser] = useState([]);
const [showData,setShowData]=useState();
  useEffect(() => {
    let data = sessionStorage.getItem("mySessionStorageData");
 
    data = JSON.parse(data);
 
    const loggedInUserId = data;
 
    const username = loggedInUserId.username;
 
    console.log(username, "Username");
 
    if (username === "Admin") {
      setShowData(true);
      loadUser(); 
    } else {
      setShowData(false);
      }
  }, []);

  const loadUser = async () => {
   
    const result = await axios.get("http://localhost:8080/api/getAllProductDeatils");
    setUser(result.data.reverse());
  };


  const deleteUser = async id => {

   
   
     await axios.delete(`http://localhost:8080/api/deleteProductByID/${id}`);
     alert("Are you sure you want to delete");
    
    loadUser();
   
  };

  const onEdit = async productId =>{
    console.log("edit.......");
    
    // const respData= await axios.post(`http://localhost:8080/api/editProductDetailsByID`);
    // console.log("respData....:",respData.data);
    // // alert(respData.data[0].productName);
    // alert(respData.data[0].productID);

  
    
    setUser();

    //setUser(respData.data);
  };

  

 

  
  

  return (
    <div className="container">
      {showData ?(
        <table>
      <h1>Admin ProductList Page</h1>
      <Link className="btn btn-warning btn-block float-end m-2" to="/user/add">

          Add Product
        </Link>
       
      <table class="table">
        <thead>
          <tr className="bg-dark text-white">
          
            <th scope="col">Product Name</th>
            <th scope="col">Product Price</th>
            <th scope="col">Stock</th>
            <th scope="col">Actions</th>
            
          </tr>
        </thead>
        <tbody>
            {user.map((user) => (
                <tr>
                    <td>{user.productName}</td>
                    <td>{user.productPrice}</td>
                    <td>{user.stock}</td>
                   
                    <td>
                       {/* <Link className="btn btn-succcess m-2">Edit</Link> */}
                        <Link className="btn btn-success m-2" onClick={()=>onEdit(user.productId)}  to={`/user/edit/${user.productId}`} >Edit</Link>
                        <Link className="btn btn-danger "onClick={() => deleteUser(user.productId)}>Delete</Link>

                        {/* <Link className="btn"><button type="button" class="btn btn-danger">Delete</button></Link> */}
                    </td>
                </tr>

            ))}
        </tbody>
       
     
      </table>
      </table>
 ) : (
  <p>You are not authorized to view this page.</p>
)}
     
    </div>
  );
};

export default Home;
