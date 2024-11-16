import React, { useState, useEffect } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import './ActivetaskList.css'
import Axios from 'axios';

const ActiveTaskList = (props) => {
    const history = useHistory();
    const [responses, setResponses] = useState([]);
    const [selectedResponse, setSelectedResponse] = useState(null);
    const [selectedAction, setSelectedAction] = useState('');
    const [query, setQuery] = useState('');
    const [showQueryBox, setShowQueryBox] = useState(false);
    const [message, setMessage] = useState('');

    const { userId } = useParams();
    const [storedUserId, setStoredUserId] = useState('');

    const userIdFromStorage = sessionStorage.getItem('userId');


    useEffect(() => {

        const fetchUrl = `http://localhost:8080/getConsolidateVariable/${userIdFromStorage}`;
        fetch(fetchUrl)
            .then(response => response.json())
            .then(data => {
                setResponses(data);
                console.log('Response:', data); // 

            })


        // Set the userId in the component's state
        setStoredUserId(userIdFromStorage);

    }, []);

    const handleActionChange = (event) => {
        setSelectedAction(event.target.value);

        // If the selected action is 'NeedClarification', set query field to be displayed
        if (event.target.value === 'NeedClarification') {
            setQuery('');
        }
    };

    // const handleSubmit = () => {
    // Perform any necessary actions based on the selectedAction
    // if (selectedAction === 'Approve') {
    //     // Navigate to Approverform with 'approved' flow condition
    //     history.push(`/Approverform/${userId}/approved`);
    // } else if (selectedAction === 'Reject') {
    //     // Navigate to Approverform with 'rejected' flow condition
    //     history.push(`/Approverform/${userId}/rejected`);
    // } else if (selectedAction === 'NeedClarification') {
    //     // Navigate to Approverform with 'needClarification' flow condition and pass the query as state
    //     history.push({
    //         pathname: `/Approverform/${userId}/needClarification`,
    //         state: { query: query },
    //     });
    // }
    // };

    // const handleResponseSelect = (response) => {
    //     setSelectedResponse(response);
    // };

    const actionApproverMap = {
        Approve: 'approved',
        Reject: 'rejected',
        NeedClarification: 'needClarification'
    };

    const handleQueryChange = (event) => {
        setQuery(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const approver = actionApproverMap[selectedAction]; // Get the corresponding approver action


        let processInstanceKey = sessionStorage.getItem("key");
        

       
        try {

            const requestData = {
                approver: approver
            };

            if (selectedAction === 'NeedClarification') {
                requestData.query = query; // Include the query in the request data
                // alert("Approver Query Submitted Successfully")
                alert("Query  send to  customer Email")

            }
            else{
                alert("Application Submitted")
            }



const url = `http://localhost:8080/completeTasklocal/${userIdFromStorage}`

         
            const result = await Axios.post(
                url,
                requestData
          
              );
            history.push("/ApproverForm");

            

            setMessage(`${approver.charAt(0).toUpperCase() + approver.slice(1)}!`);

        } catch (error) {
            console.error('Error while making the API request:', error);
            setMessage('An error occurred while processing your request. Please try again later.');
        }

    };


    const handleCancel = () => {
        history.push('/ApproverForm');
    };

    
        const handleDownload = () => {
            
            const fetchUrl = `http://localhost:8080/downloadImage`;
            
            // Make a GET request to download the image
            fetch(fetchUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/octet-stream',
                },
            })
            .then(response => {
                if (response.ok) {
                    return response.blob(); // Convert the response to a Blob
                }
                throw new Error('Network response was not ok.');
            })
            .then(blob => {
                // Create a link element
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'image.docx'; // Specify a default filename
                document.body.appendChild(a);
                a.click(); // Trigger the download
                a.remove(); // Clean up the link element
            })
            .catch(error => {
                console.error('Error downloading the file:', error);
                
            });
        };

    return (

        <div className="container">
            <br />
            

            <div className="w-100 auto shadow p-20">

                <form>
                    <div className='card'>

                        <h2 className="text-center mb-4">Applicant Details</h2>

                        <div className="row">

                            <div className="col-md-12">

                                <div className="form-group">
                                    <label>Id</label>
                                    <input
                                        type="number"
                                        className="form-control"
                                        value={storedUserId}
                                        readOnly
                                    />
                                </div>
                            </div>




                            <div className="form-row">

                                <div className="col-lg-5">


                                    <div className="form-group">

                                        <label>First Name:</label>

                                        <input

                                            type="text"

                                            className="form-control"

                                            value={responses.firstName}

                                            readOnly

                                        />

                                    </div>

                                    <div className="form-group">

                                        <label>Age</label>

                                        <input

                                            type="number"

                                            className="form-control"

                                            value={responses.age}

                                            readOnly

                                        />

                                    </div>

                                </div>









                                <div className="col-md-5">

                                    {/* Second Set of Fields */}

                                    <div className="form-group">

                                        <label>Last Name:</label>

                                        <input

                                            type="text"

                                            className="form-control"

                                            value={responses.lastName}

                                            readOnly

                                        />

                                    </div>







                                    <div className="form-group">

                                        <label>Gender</label>

                                        <input

                                            type="text"

                                            className="form-control"

                                            value={responses.gender}

                                            readOnly

                                        />

                                    </div>

                                </div>



                                <div className="col-md-5">

                                    <div className="form-group">

                                        <label>Account Type</label>

                                        <input

                                            type="text"

                                            className="form-control"

                                            value={responses.accountType}

                                            readOnly

                                        />

                                    </div>

                                    <div className="form-group">

                                        <label>Credit Score</label>

                                        <input

                                            type="number"

                                            className="form-control"

                                            value={responses.creditScore}

                                            readOnly

                                        />

                                    </div>

                                    {/* ... (other fields in the second set) */}

                                </div>





                                <div className="col-md-5">

                                    <div className="form-group">

                                        <label>Annual Income</label>

                                        <input

                                            type="number"

                                            className="form-control"

                                            value={responses.annualIncome}

                                            readOnly

                                        />

                                    </div>

                                    <div className="form-group">

                                        <label>Phone No</label>

                                        <input

                                            type="number"

                                            className="form-control"

                                            value={responses.phoneNo}

                                            readOnly

                                        />

                                    </div>

                                </div>





                                {/* <div className="col-md-12">

                                    <div className="form-group">
                                        <label>Select Action:</label>
                                        <select
                                            className="form-control"
                                            value={selectedAction}
                                            onChange={handleActionChange}
                                        >
                                            <option value="">Select Action</option>
                                            <option value="Approve">Approve</option>
                                            <option value="Reject">Reject</option>
                                            <option value="NeedClarification">Need Clarification</option>
                                        </select>
                                    </div>

                                    {selectedAction === 'NeedClarification' && (
                                        <div className="form-group">
                                            <label>Enter Query:</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                value={query}
                                                onChange={(e) => setQuery(e.target.value)}
                                            />
                                        </div>
                                    )}
                                    <div className="col-md-6">
                                        <div className="form-group">
                                            <label htmlFor="message">Comments</label>
                                            <textarea className="form-control fixed-textarea" id="message" rows="4" onChange={handleQueryChange}></textarea>
                                        </div>
                                    </div>
                                </div> */}
                               


                            </div>
                            <div className="col-md-12 selectAction">
                                    <div className="form-group">
                                        <label>Select Action:</label>
                                        <select
                                            className="form-control"
                                            value={selectedAction}
                                            onChange={handleActionChange}
                                        >
                                            <option value="">Select Action</option>
                                            <option value="Approve">Approve</option>
                                            <option value="Reject">Reject</option>
                                            <option value="NeedClarification">Need Clarification</option>
                                        </select>
                                    </div>

                                    <div className="row">
                                        {/* {selectedAction === 'NeedClarification' && ( */}
                                             <div className="col-md-6">

                                            <div className="form-group">

                                                <label htmlFor="message">Enter Query:</label>
                                               
                                                    <input
                                                      className="form-control fixed-textarea"
                                                      id="message"
                                                      rows="2"
                                                      onChange={(e) => setQuery(e.target.value)}
                                                      value={responses.query}

                                                    />

                                                </div>
                                            </div>

                                        {/* )} */}


                                        <div className="col-md-6">
                                            <div className="form-group">
                                                <label htmlFor="message">Comments</label>
                                                <textarea
                                                    className="form-control fixed-textarea"
                                                    id="message"
                                                    rows="1"
                                                    onChange={handleQueryChange}
                                                    value={responses.comment}
                                                    

                                                ></textarea>
                                                {/* <button onClick={handleDownload}>Download Image</button>*/}
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>

                             
                        </div>




                        {/* <div class="row justify-center"> */}
                        <div className="text-center mt-4">
  <button
    className="btn btn-primary mr-4"
    style={{ width: '150px' }}

    onClick={handleSubmit}
  >
    Submit
  </button>
  <button
    className="btn btn-danger"
    style={{ width: '150px' }}

    onClick={handleCancel}
  >
    Cancel
  </button>
</div>


                            </div>
                    {/* </div> */}
                    
                </form>

            </div>

        </div >

    );

}



export default ActiveTaskList;