import React, { useState } from 'react';
import { Form, Button, Row, Col, Alert } from 'react-bootstrap';
import './AddressFilingForm.css'; // Import custom CSS for styling
import axios from 'axios';


const AddressFillForm = (props) => {
  const [addressData, setAddressData] = useState({
    address: '',
    bpmnProcessId:'Address_Verification',
  });
  const [agreeToTerms, setAgreeToTerms] = useState(false);
  const [livedMoreThan3Years, setLivedMoreThan3Years] = useState(false);
  const [previousAddress, setPreviousAddress] = useState('');
  const [responseMessage, setResponseMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isLoading, setIsLoading] = useState(false);



  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setIsLoading(true);
      let activeTaskId = sessionStorage.getItem("jobKey");

      console.log("activeTaskId", activeTaskId);
      if (!activeTaskId) {

        throw new Error('taskId not found.');
      }
      addressData.activeTaskId = activeTaskId;
      let processInstanceKey = sessionStorage.getItem("key");

      console.log("processInstanceKey", processInstanceKey);
//alert("Address Form Submitted")
      const response = await axios.post(
        `http://localhost:8080/completeUserTask/${processInstanceKey}`,

        addressData
      );
      console.log("addressData",addressData)
      alert("Address Validated successfully")
      // const jobKey = response.data.extractedInfo.jobKey;
      // const localTaskNames = response.data.extractedInfo.localTaskName;
      // alert(localTaskNames);

      sessionStorage.setItem("jobKey", "123456");
      setResponseMessage("response message");

      props.history.push('/personalDetails');
      setIsLoading(false);
    }
    catch (error) {
      console.error('Error while making the API request:', error);
      setErrorMessage('An error occurred while processing your request. Please try again later.');
      setIsLoading(false);
    }


  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddressData((prevAddressData) => ({
      ...prevAddressData,
      [name]: value.startsWith("30") ? "30 Hoipo Road, SOMERSBY NSW" : value,
    }));
  };

  return (
    <div className='Addform'>
      <br />
      <br />
      {isLoading && (
        <div className="overlay">
          <div className="loading-spinner"></div>
        </div>
      )}
      <div className='container'>
        <br />
        <h2>Enter Address Details</h2>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId='address'>
            <Form.Label>Address <span className='star'> *</span></Form.Label>
            <Form.Control
              type='text'
              placeholder='Enter Address'
              name='address'
              value={addressData.address}
              onChange={handleChange}
              autoComplete="off" 
              required
            />
          </Form.Group>

          <Form.Group controlId='landmark'>
            <Form.Label>Landmark</Form.Label>
            <Form.Control
              type='text'
              placeholder='Enter Landmark'
              name='landmark'
              value={addressData.landmark}
              onChange={handleChange}
              autoComplete="off" 

            />
          </Form.Group>

          <Row>
            <Form.Group as={Col} controlId='city'>
              <Form.Label>City</Form.Label>
              <Form.Control
                type='text'
                placeholder='Enter City'
                name='city'
                value={addressData.city}
                onChange={handleChange}
                autoComplete="off"

              //required
              />
            </Form.Group>

            <Form.Group as={Col} controlId='state'>
              <Form.Label>State</Form.Label>
              <Form.Control
                type='text'
                placeholder='Enter State'
                name='state'
                value={addressData.state}
                onChange={handleChange}
                autoComplete="off"
              //required
              />
            </Form.Group>
          </Row>

          <Row>
            <Form.Group as={Col} controlId='home'>
              <Form.Label>Home Telephone</Form.Label>
              <Form.Control
                type='number'
                placeholder='Enter Telephone Number'
                name='home'
                value={addressData.home}
                onChange={handleChange}
                autoComplete="off"
              //required
              />
            </Form.Group>



            <Form.Group as={Col} controlId='phone'>
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type='number'
                placeholder='Enter Phone Number'
                name='phone'
                value={addressData.phone}
                onChange={handleChange}
                  autoComplete="off" // Add this line to disable auto-suggestions

              //required
              />
            </Form.Group>
          </Row>
          <br />
          <Row>
            <Form.Group controlId='livedMoreThan3Years' className='d-flex align-items-center'>
              <Form.Label className='mr-3'>   Have you lived at this address for more than 3 years?</Form.Label>
              <br />
              <div className='ml-3'>
                <Form.Check
                  inline
                  type='radio'
                  label='Yes'
                  name='livedMoreThan3Years'
                  value='yes'
                  checked={livedMoreThan3Years === 'yes'}
                  onChange={() => setLivedMoreThan3Years('yes')}
                />
                <Form.Check
                  inline
                  type='radio'
                  label='No'
                  name='livedMoreThan3Years'
                  value='no'
                  checked={livedMoreThan3Years === 'no'}
                  onChange={() => setLivedMoreThan3Years('no')}
                />
              </div>
            </Form.Group>
          </Row>
          {livedMoreThan3Years === 'yes' && (
            <Form.Group controlId='previousAddress'>
              <Form.Label>Previous Address</Form.Label>
              <Form.Control
                type='text'
                placeholder='Enter Previous Address'
                value={previousAddress}
                onChange={(e) => setPreviousAddress(e.target.value)}
              />
            </Form.Group>
          )}


          <Form.Group controlId='agreeToTerms'>
            <Form.Check
              type='checkbox'
              label='I agree that my above information is checked with the Issuer or Official Record Holder'
              checked={agreeToTerms}
              onChange={(e) => setAgreeToTerms(e.target.checked)}
            required
            />
          </Form.Group>
          {errorMessage && <Alert variant='danger'>{errorMessage}</Alert>}
          {responseMessage && <Alert variant='success'>{responseMessage}</Alert>}

          <br />
          <center>
            <Button variant='primary' type='submit' style={{ height: '50px' }}>
              Submit
            </Button>
          </center>
        </Form>
      </div>
    </div>
  );
};

export default AddressFillForm;
