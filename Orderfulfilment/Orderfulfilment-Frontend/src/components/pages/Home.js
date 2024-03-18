

import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link,  } from "react-router-dom";
import Edituser from '../users/Edituser';

const Home = () => {
  const [user, setUser] = useState([]);

  //[] --> dependency value i.e if u not gave [] it will run infinite times
  useEffect(() => {
   loadUser();
  
  }, []);

  //loadUser --> jo bhi users hai ismai aa k load hote hai. async --> jab tak ek req khatam nhi hoti tab tak dusri req nhi enter kregi
  const loadUser = async () => {
   
    const result = await axios.get("http://localhost:8080/getAllProductDeatils");
    console.log(result,"asd");
    setUser(result.data.reverse());
  };


  const deleteUser = async id => {

   
   
     await axios.delete(`http://localhost:8080/deleteProductByID/${id}`);
     alert("Are you sure you want to delete");
    
    loadUser();
   
  };

  const onEdit = async product_ID =>{
    //const respData= await axios.get(`http://localhost:8080/getProductDetailsById/${product_ID}`);
    //console.log("respData....:",respData.data);
    // alert(respData.data[0].product_Name);
    // alert(respData.data[0].product_ID);

  
    
    setUser();

    //setUser(respData.data);
  };

  

 

  
  

  return (
    <div className="container">
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
                    <td>{user.product_Name}</td>
                    <td>{user.product_Price}</td>
                    <td>{user.stock}</td>
                   
                    <td>
                       {/* <Link className="btn btn-succcess m-2">Edit</Link> */}
                        <Link className="btn btn-success m-2" onClick={()=>onEdit(user.product_ID)}  to={`/user/edit/${user.product_ID}`} >Edit</Link>
                        <Link className="btn btn-danger "onClick={() => deleteUser(user.product_ID)}>Delete</Link>

                        {/* <Link className="btn"><button type="button" class="btn btn-danger">Delete</button></Link> */}
                    </td>
                </tr>

            ))}
        </tbody>
       
     
      </table>

     
    </div>
  );
};

export default Home;
