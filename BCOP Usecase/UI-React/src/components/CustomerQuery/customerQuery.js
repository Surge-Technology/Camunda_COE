import React from 'react'
//import './customerQuery.css'
const customerQuery = () => 
{

  return (
    <div class="rsvp-section" id="rsvp">
  <div class="container">
    <div class="row pt-5 pb-5">
      <div class="col-12 text-center pt-5 pb-5">
        <h2 class="headingbig display-3 pt-3 text-white">Come Join Us</h2>
      </div>
      <div class="col-12 text-white p-5 bg-black">
        <form>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label>Your Name</label>
              <input type="text" class="form-control" id="inputName" placeholder="Your Name"/>
            </div>
            <div class="form-group col-md-6">
              <label for="inputEmail4">Email</label>
              <input type="email" class="form-control" id="inputEmail4" placeholder="Email"/>
            </div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label>Will you attend?</label>
              <select class="form-control">
                <option selected="">Yes, I will be there</option>
                <option>Sorry, I can't come</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label>Meal preference</label>
              <select class="form-control">
                <option selected="">Veg</option>
                <option>Non Veg</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label>Your Message</label>
            <textarea class="form-control" id="inputmessage" placeholder="Your Message"></textarea>
          </div>
          <div class="form-group text-center">
            <button type="submit" class="btn btn-primary" style="width: 150px;">Send</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
 ); 
}

export default customerQuery
