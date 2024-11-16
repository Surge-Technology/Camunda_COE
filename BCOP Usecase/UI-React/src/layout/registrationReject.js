import React, {useState}from 'react'
import sad from '../assets/images/sad.png'
import Axios from 'axios';
export default function RegistrationReject(props) {
  const [showModal, setShowModal] = useState(false); // State for showing/hiding the modal

 const requestbody = ({});

  const handleModalClose = () => {
   // alert("reject");

    let processInstanceKey = sessionStorage.getItem("key");

    console.log("processInstanceKey", processInstanceKey);

    const result = Axios.post(`http://localhost:8080/completeTaskWithInstanceId/${processInstanceKey}`, requestbody)

    console.log(result)

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
                  <td bgcolor="#e87e51" align="center">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>
                      <tr>
                        <td align="center" valign="top" style={{ padding: '40px 10px 40px 10px' }}></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td bgcolor="#e87e51" align="center" style={{ padding: '0px 10px 0px 10px' }}>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>
                      <tr>
                        <td bgcolor="#ffffff" align="center" valign="top" style={{ padding: '40px 20px 20px 20px', borderRadius: '4px 4px 0px 0px', color: '#111111', fontFamily: 'Lato, Helvetica, Arial, sans-serif', fontSize: '48px', fontWeight: 400, letterSpacing: '4px', lineHeight: '48px' }}>
                          <h1 style={{ fontSize: '48px', fontWeight: 400, margin: '2',fontFamily:'fantasy' }}>Sorry !</h1>
                          {/* <img src="https://img.icons8.com/clouds/100/000000/handshake.png" width="125" height="120" style={{ display: 'block', border: '0px' }} /> */}
                          <img src={sad} width="125" height="120" style={{ display: 'block', border: '0px' }} />
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td bgcolor="#f4f4f4" align="center" style={{ padding: '0px 10px 0px 10px' }}>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style={{ maxWidth: '600px' }}>
                      <tr>
                        <td className='para' bgcolor="#ffffff" align="left" style={{ padding: '30px 30px 40px 30px', color: '#141414', fontFamily: ' Calibri', fontSize: '20px', fontWeight: 400, lineHeight: '25px' }}>
                          <p style={{ margin: '0' }}> Mr.Pratip
                          Your application did not meet our internal criteria for account eligibility. 
Please note that this decision has been made after a thorough analyses, and we are unable to proceed with opening the account at this time.we cannot more forward
</p>
                        </td>
                      </tr>
                      <tr>
                        <td bgcolor="#ffffff" align="left">
                          <table  width="100%" border="0" cellspacing="0" cellpadding="0">
                            
                              <td bgcolor="#ffffff" align="center" style={{ padding: '20px 30px 60px 30px' }}>
                                  <tr>
                                    <button className='mt-2' align="center" style={{ borderRadius: '3px', backgroundColor: '#FFA73B' }} onClick={handleModalClose}>
                                     
                                        Close 
                                    </button> 
                                  </tr>
                              </td>
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
