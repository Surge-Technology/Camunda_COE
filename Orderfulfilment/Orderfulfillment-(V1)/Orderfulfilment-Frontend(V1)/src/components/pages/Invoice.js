import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

const Invoice = () => {
  const [loader, setLoader] = useState(false);
  const [invoivedata, setInvoicedata] = useState([]);
  const invoiceRef = useRef(null);

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    const result = await axios.get("http://localhost:8080/getInvoiceDetails/manju");
    console.log(result.data[0]);
    setInvoicedata(result.data[0]);
  };

  const downloadPDF = () => {
    const capture = invoiceRef.current;
  
    setLoader(true);
    html2canvas(capture).then((canvas) => {
      const doc = new jsPDF("p", "mm", "a4");
      const imgData = canvas.toDataURL("image/png");
      const imgProps = doc.getImageProperties(imgData);
  
      const imgWidth = 210;
      const imgHeight = (imgProps.height * imgWidth) / imgProps.width;
  
      doc.addImage(imgData, "PNG", 0, 0, imgWidth, imgHeight);
      setLoader(false);
  
      // Generate a default download filename
      const fileName = "invoice.pdf";
  
      // Create an <a> element with a download attribute
      const downloadLink = document.createElement("a");
      downloadLink.href = doc.output("blob");
      downloadLink.download = fileName;
  
      // Trigger the download
      downloadLink.click();
    });
  };
  
  

  return (
    <div>
      <div className="col-12">
       
        {/* Main content */}
        <div className="invoice p-3 mb-3" ref={invoiceRef}>
          {/* title row */}
          <div className="row">
            
            {/* /.col */}
          </div>
          {/* info row */}
          <div className="row invoice-info">
            
            {/* /.col */}
            <div className="col-8 invoice-col">
              To
              <address>
                <strong>John Doe</strong>
                <br />
                795 Folsom Ave, Suite 600
                <br />
                San Francisco, CA 94107
                <br />
                Phone: (555) 539-1037
                <br />
                Email: john.doe@example.com
              </address>
            </div>
            {/* /.col */}
            <div className="col-sm-4 invoice-col">
              <b>Invoice #007612</b>
              <br />
              <br />
              <b>Order ID:</b> 4F3S8J
              <br />
              <b>Payment Due:</b> 2/22/2014
              <br />
              <b>Account:</b> 968-34567
            </div>
            {/* /.col */}
          </div>
          {/* /.row */}
          {/* Table row */}
          <div className="row">
            <div className="col-12 table-responsive">
              <table className="table table-striped">
                <thead>
                  <tr>
                   
                    <th>Product</th>
                    <th>ID</th>
                    <th>Description</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  
                  <tr>
                   
                    <td>Need for Speed IV</td>
                    <td>247-925-726</td>
                    <td>Wes Anderson umami biodiesel</td>
                    <td>$50.00</td>
                  </tr>
                  <tr>
                   
                    <td>Monsters DVD</td>
                    <td>735-845-642</td>
                    <td>
                      Terry Richardson helvetica tousled street art master
                    </td>
                    <td>$10.70</td>
                  </tr>
                  <tr>
                   
                    <td>Grown Ups Blue Ray</td>
                    <td>422-568-642</td>
                    <td>Tousled lomo letterpress</td>
                    <td>$25.99</td>
                  </tr>
                </tbody>
              </table>
            </div>
            {/* /.col */}
          </div>
          {/* /.row */}
          <div className="row">
            {/* accepted payments column */}
            <div className="col-6">
             
            </div>
            {/* /.col */}
            <div className="col-6">
             
              <div className="table-responsive">
                <table className="table">
                  <tbody>
                    <tr>
                      <th style={{ width: "50%" }}>Subtotal:</th>
                      <td>$250.30</td>
                    </tr>
                    <tr>
                      <th>Tax:</th>
                      <td>$10.34</td>
                    </tr>
                    <tr>
                      <th>Discount:</th>
                      <td>$5.80</td>
                    </tr>
                    <tr>
                      <th>Total:</th>
                      <td>$265.24</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            {/* /.col */}
          </div>
          {/* /.row */}
          {/* this row will not appear when printing */}
          <div className="row no-print">
            <div className="col-12">
              
              
              <button
                type="button"
                className="btn btn-primary float-right"
                style={{ marginRight: 5 }}
                onClick={downloadPDF}
                disabled={!(loader===false)}
              >
                {/* <i className="fas fa-download" /> */}
                {loader?(<span>Downloading</span>):(<span>Download</span>)}
              </button>
            </div>
          </div>
        </div>
        {/* /.invoice */}
      </div>
    </div>
  );
};

export default Invoice;
