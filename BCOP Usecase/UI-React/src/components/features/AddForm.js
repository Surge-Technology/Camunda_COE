import React, { useState } from 'react';

const AddressForm = () => {
  const [address, setAddress] = useState({
    street: '',
    city: '',
    state: '',
    postalCode: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddress((prevAddress) => ({
      ...prevAddress,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Perform the API call for address validation
    fetch('http://localhost:8080/validateAddress', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(address),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.valid) {
          // If the address is valid, redirect to the Personal Detail page
          window.location.href = '/PersonalDetails';
        } else {
          // If the address is not valid, display an error message or take appropriate action
          alert('Invalid address. Please check your address details.');
        }
      })
      .catch((error) => {
        console.error('Error validating address:', error);
        // Handle error, e.g., show an error message to the user
      });
  };

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="street">Street:</label>
      <input
        type="text"
        id="street"
        name="street"
        value={address.street}
        onChange={handleChange}
        required
      />
      {/* Other address input fields (city, state, postalCode) */}
      <button type="submit">Submit</button>
    </form>
  );
};

export default AddressForm;
