import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import "./CustomerQueryPage.css";
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import swal from 'sweetalert';

const CustomerQueryPage = () => {

  const location = useLocation();
  const [query, setQuery] = useState("");
  const [comment, setComment] = useState('');
  const[initialStateData,setState]=useState("");
  const queryData = location.state ? location.state.queryData : '';
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    // Get the URL search parameters
    const searchParams = new URLSearchParams(window.location.href);
    const decodedUrl = decodeURIComponent(searchParams);
    const urlObj = new URL(decodedUrl);

    const pId = urlObj.searchParams.get('pId');
    //alert("PID " + pId);

    if (pId) {
      console.log('pId:', pId);

      const fetchData = async () => {
        try {
          const response = await axios.post(`http://localhost:8080/customerReply?processInstanceKey=${pId}`);
          console.log('Fetched Data:', response.data);
          console.log('Query for showing  ' + response.data.query);
          sessionStorage.setItem('query', response.data.query);

          console.log('Customer Name:--- ' + response.data.username);
          sessionStorage.setItem('customerName', response.data.username);

        } catch (error) {
          console.error('Error while fetching data:', error);
        }
      };

      fetchData();
    } else {
      // Handle the case when pId is not present in the URL
      console.log('pId not found in the URL');
    }
  }, []);


  const handleQueryChange = (event) => {
    setQuery(event.target.value);
  };
  const postData = {
    comment: comment,
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (submitted) {
      return; // Prevent multiple submissions
    }
    try {
      const response = await axios.post('http://localhost:8080/completeTaskWithInstanceId/{pId}',
        postData,
        { headers: { 'Content-Type': 'application/json' } }
      );
      // alert("Document submitted");
      console.log("API Response:", response);
      swal({
        icon: "success",
        title: "Document Submitted",
        text: "Your document has been successfully submitted!",
        button: "OK",
      }).then(() => {
        // Close the page/tab when OK is clicked
        window.close(); // Attempts to close the current tab
      });
      

      setSubmitted(true);
    } catch (error) {
      console.error('Error while submitting query:', error);
    }
 //   alert(" Your Document submitted")
  };

  
  
  const ContractFileHandleChange = async (e) => {
    // alert("balaji")
    e.preventDefault();  // Prevent default form action
    try {
        let formData = new FormData();
        formData.append('image', e.target.files[0]);

        const response = await axios.post('http://localhost:8080/files/fileimage/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });

        setState((prevState) => ({
            ...prevState,
            contractImageName: response.data.imageName,
            contractData: e
        }));

        alert("File uploaded successfully");
        console.log("API Response:", response);

    } catch (err) {
        console.error('Error while uploading the file:', err);
        
    }
};



  const retrievedQueryData = sessionStorage.getItem('query');
  console.log(retrievedQueryData);

  const applicantName = sessionStorage.getItem('customerName');
  console.log('applicantName', applicantName);
  console.log("postdata" + comment)

  return (
    <div className='background-image'>
      <div className="container d-flex justify-content-center align-items-center vh-100">
        <div className="card p-5">
          <center> <h1 className="mb-4">Query from RBA</h1></center>
          <div className="row">
            <div className="col-md-6">

              {/* <div className="customer-message-box">
              <p>
                Hii, This message from Reserve Bank of Australia giving a some message to you, submit the required mentioned below files.
              </p>
            </div> */}

              {/* <h3 className="mb-4">Application Status : Inprogress<span style={{ margin: '0 10px' }}>   
            </span>  </h3>  */}
              <p><strong>Application Status:</strong>
                <input
                  type="text"
                  className="form-control"
                  id="applicationStatus"
                  name="applicationStatus"
                  value="Inprogress"
                  readOnly
                /></p>

              <div className="form-group">
                <label htmlFor="message">Query</label>
                <div className="form-group">
                  <textarea className="form-control fixed-textarea" id="message" rows="4" onChange={handleQueryChange}>
                    {retrievedQueryData}

                  </textarea>
                </div>
              </div>



              {/* <h4>Query :{AdminQuery}</h4> */}


            </div>

            <div className="col-md-6">
              <p>
                <strong>Customer Name :</strong>

                <input
                  type="text"
                  className="form-control"
                  id="customerName"
                  name="customerName"
                  value={applicantName}
                  readOnly
                />

                {/* <span><strong> {applicantName} </strong>
                </span> */}
                </p>

              <div className="col-md-6">
                <form >

                  <div className="form-group">
                    <label htmlFor="message">Comments</label>
                    <div className="form-group">

                      <textarea className="form-control fixed-textarea" id="comment" rows="4" value={comment} onChange={(e) => setComment(e.target.value)}></textarea>
                    </div>
                  </div>
                  <div className="form-group">
                    <label htmlFor="document"   >Upload Document <span className='star'> *</span></label>
                    <input type="file" className="form-control-file" id="document" required  onChange={ContractFileHandleChange} />
                  </div>
                </form>
              </div>
             
            </div>
          </div>
        <center>  <button type="submit" className="btn btn-primary" style={{width:100}} onClick={handleSubmit}>Submit</button>
</center>
{submitted && (
  <div className="col-12 text-white p-5 bg-black">
    <p className="text-center">Form has already been submitted. Thank you!</p>
  </div>
)}


        </div>
      </div>
    </div>
  );
};

export default CustomerQueryPage;
