import React, { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";

const Reject = () => {
  const [customerName, setCustomerName] = useState("");
  const [accountNumber, setAccountNumber] = useState("");
  const [open, setOpen] = useState(false);

  const handleContinue = () => {
    setOpen(true); // Open the modal when the button is clicked
  };

  const handleModalClose = () => {
    setOpen(false); // Close the modal when the "Close" button is clicked
  };

  return (
    <div className="container congratulations-container">
      <div className="row justify-content-center">
        <div className="col-lg-6">
          <div className="card shadow">
            <div className="card-body text-center">
              <h1 className="mb-4">Congratulations, {customerName}!</h1>
              <p className="mb-4">You are now part of our family!</p>
              <p className="mb-4">Your Account Number: {accountNumber}</p>
              <Button variant="contained" color="primary" onClick={handleContinue}>
                Open Modal
              </Button>
              <Dialog open={open} onClose={handleModalClose}>
                <DialogTitle>Congratulations!</DialogTitle>
                <DialogContent>
                  <DialogContentText>
                    Welcome to our banking family, {customerName}!
                    Your account number is {accountNumber}.
                  </DialogContentText>
                  <Button variant="contained" color="primary" onClick={handleModalClose}>
                    Close
                  </Button>
                </DialogContent>
              </Dialog>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Reject;
