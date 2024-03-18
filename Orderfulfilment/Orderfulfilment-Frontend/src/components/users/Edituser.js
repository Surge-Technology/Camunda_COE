import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { Link, useNavigate, useParams } from "react-router-dom";
//useParam --> in url we have id to get that id we useparam i.e /edit

const Edituser = () => {
  let navigate = useNavigate();

  const close = () => {
    navigate("/home")
  }
  // const { id } = useParams();
  // console.log(id);

  

  // const params = useParams({
  //   product_ID:"",
  //   product_Name:"",
  //   product_Price:"",
  //   product_Qnty:"",
  //   stock:"",
  // });

  // console.log(params);

  

  
  const[user,setuser] = useState({
    product_ID:"",
    product_Name:"",
    product_Price:0,
    product_Qnty:0,
    stock:0,
});



const{product_ID,product_Name,product_Price,product_Qnty,stock} = user;

   const onInputChange = e =>{
       setuser({...user,[e.target.name]:e.target.value});
       
    };

    const onInputChangeStock = e =>{
      setuser({...user,[e.target.name]:parseInt(e.target.value) || 0});
      
  };

 
    //jo data api se lere vo ismai ana chahiye
    useEffect(() =>{
     
        loadUser();
       
    },[]);
    
    
    const onSubmit = async e => {
        e.preventDefault();
        const result=await axios.post(`http://localhost:8080/editProductDetailsByID`,user);
       
       console.log(result);
        navigate("/home");
    };


    const loadUser = async () =>{
      
        //const result = await axios.get("http://localhost:8080/getAllProductDeatils");

        // const id = setuser(...user.product_ID);
        // alert(id,'id...');
        // {user.map((user)=>(user.product_ID))}
        // const id = user.product_ID;
        // alert('new',id);
         
        
        const productId = window.location.pathname.split("/").pop();
        const respData= await axios.get(`http://localhost:8080/getProductDetailsById/${productId}`);
        
        setuser(respData.data[0]);
        console.log('response id......',respData.data[0].product_ID);


    };

    
    

  return (
    <div className="container">
      <div className="w-75 auto shadow p-5">
        <h2 className="text-center mb-4">Edit A Product</h2>
        <form onSubmit={(e) => onSubmit(e)}>
        <div className="form-group"> 
        
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Enter product id"
              readOnly 
              name="product_ID"
              value={product_ID}
              onChange={(e) => onInputChange(e)}
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Enter your product name"
              name="product_Name"
              value={product_Name}
              onChange={(e) => onInputChange(e)}
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Enter price"
              name="product_Price"
              value={product_Price}
              onChange={(e) => onInputChangeStock(e)}
            />
          </div>

          <div className="form-group">
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Enter quantity"
              name="product_Qnty"
              value={product_Qnty}
              onChange={(e) => onInputChangeStock(e)}
            />
          </div>
          
          <div className="form-group">
            <input
              type="text"
              className="form-control form-control-lg"
              placeholder="Enter stock"
              name="stock"
              value={stock}
              onChange={(e) => onInputChangeStock(e)}
            />
          </div>
          <center>
          <button className="btn btn-primary m-2" >Update Product</button>
          <button className="btn btn-secondary"onClick={() => close()}>Close</button>
          </center>
        </form>
       
      </div>
    </div>
  );
};

export default Edituser;
