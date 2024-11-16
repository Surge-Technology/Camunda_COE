import React, { useState } from 'react';
import "./CardDetails.css";

const CardDetails = () => {
  const [formData, setFormData] = useState({
    username: '',
    cardNumber: '',
    expMonth: '',
    expYear: '',
    cvv: '',
  });

  const [errors, setErrors] = useState({});
  const [showPopup, setShowPopup] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    setErrors({
      ...errors,
      [name]: '',
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = {};
    // Perform validation here
    if (formData.username.trim() === '') {
      validationErrors.username = 'Please enter card owner name';
    }
    if (!/^\d{16}$/.test(formData.cardNumber)) {
      validationErrors.cardNumber = 'Invalid card number';
    }
    if (!/^\d{2}$/.test(formData.expMonth) || formData.expMonth < 1 || formData.expMonth > 12) {
      validationErrors.expMonth = 'Invalid expiration month';
    }
    if (!/^\d{2}$/.test(formData.expYear) || formData.expYear < 21) {
      validationErrors.expYear = 'Invalid expiration year';
    }
    if (!/^\d{3}$/.test(formData.cvv)) {
      validationErrors.cvv = 'Invalid CVV';
    }
    if (formData.cvv.length > 3) {
      validationErrors.cvv = 'CVV should be 3 digits';
    }
    if (formData.cardNumber.length !== 16) {
      validationErrors.cardNumber = 'Card number should be 16 digits';
    }

    setErrors(validationErrors);

    // If there are no validation errors, proceed with the payment
    if (Object.keys(validationErrors).length === 0) {
      // For this example, we'll just log the data and show the payment success popup
      console.log('Payment submitted:', formData);
      setShowPopup(true);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '1000px', margin: '0 auto' }}>
      {/* For demo purpose */}
      <div className="row mb-4">
        <div className="col-lg-8 mx-auto text-center">
          <h1 className="display-6">Payment</h1>
        </div>
      </div>
      {/* End */}
      <div className="row">
        <div className="col-lg-6 mx-auto">
          <div className="card">
            <div className="card-header">
              <div className="bg-white shadow-sm pt-4 pl-2 pr-2 pb-2">
                {/* Credit card form tabs */}
                <ul role="tablist" className="nav bg-light nav-pills rounded nav-fill mb-3">
                  <li className="nav-item">
                    <a data-toggle="pill" href="#credit-card" className="nav-link active">
                      <i className="fas fa-credit-card mr-2"></i> Credit Card
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <div className="tab-content">
              <div id="credit-card" className="tab-pane fade show active pt-3">
                <form role="form" onSubmit={handleSubmit}>
                  <div className="form-group">
                    <label htmlFor="username">
                      <h6>Card Owner</h6>
                    </label>
                    <input
                      type="text"
                      name="username"
                      placeholder="Card Owner Name"
                      value={formData.username}
                      onChange={handleChange}
                      required
                      className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                    />
                    {errors.username && <div className="invalid-feedback">{errors.username}</div>}
                  </div>
                  <div className="form-group">
                    <label htmlFor="cardNumber">
                      <h6>Card number</h6>
                    </label>
                    <div className="input-group">
                      <input
                        type="text"
                        name="cardNumber"
                        placeholder="Valid card number"
                        value={formData.cardNumber}
                        onChange={handleChange}
                        required
                        className={`form-control ${errors.cardNumber ? 'is-invalid' : ''}`}
                      />
                      <div className="input-group-append">
                        <span className="input-group-text text-muted">
                          <i className="fab fa-cc-visa mx-1"></i> <i className="fab fa-cc-mastercard mx-1"></i> <i className="fab fa-cc-amex mx-1"></i>
                        </span>
                      </div>
                      {errors.cardNumber && <div className="invalid-feedback">{errors.cardNumber}</div>}
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-8">
                      <div className="form-group">
                        <label>
                          <span className="hidden-xs">
                            <h6>Expiration Date</h6>
                          </span>
                        </label>
                        <div className="input-group">
                          <input
                            type="number"
                            placeholder="MM"
                            name="expMonth"
                            value={formData.expMonth}
                            onChange={handleChange}
                            required
                            className={`form-control ${errors.expMonth ? 'is-invalid' : ''}`}
                          />
                          <input
                            type="number"
                            placeholder="YY"
                            name="expYear"
                            value={formData.expYear}
                            onChange={handleChange}
                            required
                            className={`form-control ${errors.expYear ? 'is-invalid' : ''}`}
                          />
                        </div>
                        {errors.expMonth && <div className="invalid-feedback">{errors.expMonth}</div>}
                        {errors.expYear && <div className="invalid-feedback">{errors.expYear}</div>}
                      </div>
                    </div>
                    <div className="col-sm-4">
                      <div className="form-group mb-4">
                        <label data-toggle="tooltip" title="Three digit CV code on the back of your card">
                          <h6>CVV <i className="fa fa-question-circle d-inline"></i></h6>
                        </label>
                        <input
                          type="text"
                          name="cvv"
                          placeholder="CVV"
                          value={formData.cvv}
                          onChange={handleChange}
                          required
                          className={`form-control ${errors.cvv ? 'is-invalid' : ''}`}
                        />
                        {errors.cvv && <div className="invalid-feedback">{errors.cvv}</div>}
                      </div>
                    </div>
                  </div>
                  <div className="card-footer">
                    <button type="submit" className="subscribe btn btn-primary btn-block shadow-sm">
                      Confirm Payment
                    </button>
                  </div> {showPopup && (
        <div className="payment-success-popup">
          <div className="popup-content">
            <h2>Payment Successful!</h2>
            <p>Thank you for your payment.</p>
            <button onClick={() => setShowPopup(false)}>Close</button>
          </div>
        </div>
      )}
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CardDetails;
