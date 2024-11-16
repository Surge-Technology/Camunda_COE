import React, { useState } from "react";
import { Button } from "react-bootstrap";
import './AdminActiveTasklistDetail.css'
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

const AdminActiveTaskByID = () => {

  const history = useHistory();
  const [SelectOption, setSelectOption] = useState('Approve');
  const [queryText, setQueryText] = useState("");
  const handleOptionChange = (e) => {

    setSelectOption(e.target.value);

  }

  const handleQueryChange = (event) => {
    setQueryText(event.target.value);
  };
  const handleSubmit = (event) => {
    event.preventDefault();
    if (SelectOption === "Approve") {
      history.push('/happyPath')
    }
    else if (SelectOption === "Reject") {
      history.push('/registrationReject')
    }
    else if (SelectOption === "Need Clarification") {
      // history.push('/CustomerQueryPage')
      history.push("/customerQueryPage", { SelectOption, queryText });

    }


  }
  return (
    <div className="ActiveTaskForm">
      <br /><br />
      <div className="container">
        <center>
          <h2>ActiveTask Details</h2>
        </center>
        <br />
        <br />
        <form>
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="inputEmail4">Email</label>
      <input type="email" class="form-control" id="inputEmail4" placeholder="Email"/>
    </div>
    <div class="form-group col-md-6">
      <label for="inputPassword4">Password</label>
      <input type="password" class="form-control" id="inputPassword4" placeholder="Password"/>
    </div>
  </div>
  <div class="form-group">
    <label for="inputAddress">Address</label>
    <input type="text" class="form-control" id="inputAddress" placeholder="1234 Main St"/>
  </div>
  <div class="form-group">
    <label for="inputAddress2">Address 2</label>
    <input type="text" class="form-control" id="inputAddress2" placeholder="Apartment, studio, or floor"/>
  </div>
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="inputCity">City</label>
      <input type="text" class="form-control" id="inputCity"/>
    </div>
    <div class="form-group col-md-4">
      <label for="inputState">State</label>
      <select id="inputState" class="form-control">
        <option selected>Choose...</option>
        <option>...</option>
      </select>
    </div>
    <div class="form-group col-md-2">
      <label for="inputZip">Zip</label>
      <input type="text" class="form-control" id="inputZip"/>
    </div>
  </div>
  <div class="form-group">
    <div class="form-check">
      <input class="form-check-input" type="checkbox" id="gridCheck"/>
      <label class="form-check-label" for="gridCheck">
        Check me out
      </label>
    </div>
  </div>
  <button type="submit" class="btn btn-primary">Sign in</button>
</form>
      </div>
    </div>
  );
};

export default AdminActiveTaskByID;
