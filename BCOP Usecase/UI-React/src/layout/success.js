import Axios from 'axios';

import React, { useState } from 'react'



export default function CongratulationsScreen(props) {

  const [showModal, setShowModal] = useState(false); // State for showing/hiding the modal



  const requestbody = ({});



  const handleModalClose = () => {
  //  alert("success");

    let processInstanceKey = sessionStorage.getItem("key");

    console.log("processInstanceKey", processInstanceKey);

    const result = Axios.post(`http://localhost:8080/completeTaskWithInstanceId/${processInstanceKey}`, requestbody)

    console.log(result)
    // alert(result+"......")

    setShowModal(false); // Close the modal when the "OK" button is clicked
     props.history.push('/')

  };

  return (

    <div>

      <div className='v7-lp-template-desktop-wrapper'>



        <div>

          <div

            style={{

              display: 'none',

              fontSize: '1px',

              color: '#fefefe',

              lineHeight: '1px',

              fontFamily: 'Lato, Helvetica, Arial, sans-serif',

              maxHeight: '0px',

              maxWidth: '0px',

              opacity: 0,

              overflow: 'hidden'

            }}

          >

          </div>

          <table border="0" cellpadding="0" cellspacing="0" width="100%">

            <tr>

              <td bgcolor="#66c7d4" align="center">

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>

                  <tr>

                    <td align="center" valign="top" style={{ padding: '40px 10px 40px 10px' }}></td>

                  </tr>

                </table>

              </td>

            </tr>

            <tr>

              <td bgcolor="#66c7d4" align="center" style={{ padding: '0px 10px 0px 10px' }}>

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>

                  <tr>

                    <td bgcolor="#ffffff" align="center" valign="top" style={{ padding: '40px 20px 20px 20px', borderRadius: '4px 4px 0px 0px', color: '#111111', fontFamily: 'Lato, Helvetica, Arial, sans-serif', fontSize: '48px', fontWeight: 400, letterSpacing: '4px', lineHeight: '48px' }}>

                      <h1 style={{ fontSize: '48px', fontWeight: 400, margin: '2', fontFamily: 'fantasy' }}>Account Confirmation</h1>

                      <img src="https://img.icons8.com/clouds/100/000000/handshake.png" width="125" height="120" style={{ display: 'block', border: '0px' }} />

                    </td>

                  </tr>

                </table>

              </td>

            </tr>

            <tr>

              <td bgcolor="#f4f4f4" align="center" style={{ padding: '0px 10px 0px 10px' }}>

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>

                  <tr>

                    <td bgcolor="#ffffff" align="left" style={{ padding: '20px 30px 4px 30px', color: '#111111', fontFamily: 'Lato, Helvetica, Arial, sans-serif', fontSize: '18px', fontWeight: 400, lineHeight: '25px' }}>

                      <p style={{ margin: '0' }}>

                        {/* Mr.Pratip you are now part of our bank.Just press the button below to see your account no:5678901234 */}

                        <p>We are delighted to inform you that your bank account has been successfully created with our bank. Welcome to our banking family!</p>



                        <h2>Account Details:</h2>

                        <ul>
                        <li>Account Holder:Vijay </li>


                          <li>Account Number: [98765432109]</li>

                          <li>Account Type: [Saving Type]</li>


                        </ul>



                        <p>Please keep the above account details secure as they are essential for accessing your account and conducting transactions.</p>



                        {/* <p>If you have any questions or need assistance with our services, feel free to contact our dedicated customer support team at [Customer Support Contact Information]. We are here to help you at any time.</p> */}



                        <p>Thank you for choosing our bank. We look forward to serving you and ensuring a seamless banking experience.</p>



                        {/* <p>Best regards,<br>[Bank Name] Team</p> */}



                      </p>

                    </td>

                  </tr>

                  <tr>

                    <td bgcolor="#ffffff" align="left">

                      <table width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>

                          <td bgcolor="#ffffff" align="center" style={{ padding: '20px 30px 4px 30px' }}>

                            <table border="0" cellspacing="0" cellpadding="0">

                              <tr>

                                {/* <button className='mt-2' align="center" style={{ borderRadius: '3px', backgroundColor: '#FFA73B' }}>

                                     

                                        Download

                                    </button> &nbsp;&nbsp; */}

                                <button className='mt-2' align="center" style={{ borderRadius: '3px', backgroundColor: '#FFA73B' }} onClick={handleModalClose}>



                                  OK

                                </button>

                              </tr>

                            </table>

                          </td>

                        </tr>

                      </table>

                    </td>

                  </tr>

                  <tr>

                    <td bgcolor="#ffffff" align="left" style={{ padding: '0px 30px 40px 30px', borderRadius: '0px 0px 4px 4px', color: '#666666', fontFamily: 'Lato, Helvetica, Arial, sans-serif', fontSize: '18px', fontWeight: 400, lineHeight: '25px' }}>

                      <p style={{ margin: '0' }}>Cheers,<br />RBA Bank</p>

                    </td>

                  </tr>

                </table>

              </td>
            </tr>
          </table>

        </div>

      </div>

    </div>

  )

}