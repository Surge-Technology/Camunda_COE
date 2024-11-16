// import React, { useState } from "react";
// import "./features.css";
// import { Link } from "react-router-dom";
// //import auth from "../../service/auth";
// import { Button, Modal } from "react-bootstrap";

// //Images
// import balance from "../../assets/images/balance.png";
// import deposit from "../../assets/images/deposit.png";
// import withdraw from "../../assets/images/withdraw.png";
// import transfer from "../../assets/images/transfer.png";
// import transactions from "../../assets/images/transactions.png";

// import home from "../../assets/images/home.png";

// export default function Features(props) {
//   // const [state, setState] = useState({
//   //   customer: {}
//   // });
//   //useState(scrollOnBack);
//   const [modalShow, setModalShow] = useState(false);

//   const myDivToFocus = React.createRef();
//   console.log(props.location.state.from);

//   function handleOnClick() {
//     if (myDivToFocus.current) {
//       myDivToFocus.current.scrollIntoView({
//         behavior: "smooth",
//         block: "nearest"
//       });
//     }
//   }

//   //Modal
//   function MyVerticallyCenteredModal(props) {
//     return (
//       <Modal
//         {...props}
//         //size="lg"
//         aria-labelledby="contained-modal-title-vcenter"
//         centered
//       >
//         <Modal.Header closeButton>
//           <Modal.Title id="contained-modal-title-vcenter">
//             Back To Home !!
//           </Modal.Title>
//         </Modal.Header>
//         <Modal.Body>
//           <p>Confirm Logout ?</p>
//         </Modal.Body>
//         <Modal.Footer>
//           <Button variant="secondary" onClick={props.onHide}>
//             Cancel
//           </Button>
//           <Button variant="success" onClick={logout}>
//             Logout
//           </Button>
//         </Modal.Footer>
//       </Modal>
//     );
//   }

//   function logout() {
//     props.history.push({
//       pathname: "useraction",
//       state: {
//         customer: props.location.state.customer
//       }
//     });
//   }

//   return (
//     <>
//       <div className="container-fluid test">
//         <div className="text-center" id="toptext">
//           <i>
//             <p>
//               <span className="display-4">
//                 Hey {props.location.state.customer.customerName} !!
//               </span>
//             </p>
//             <br />
//             <p style={{ fontSize: "25px" }}>Enjoy Our Services</p>
//           </i>
//         </div>
//       </div>

//       <div className="content">
//         <p
//           className="btn btn-info btn-lg text-center start featurebutton"
//           id="begin"
//           style={{ fontSize: "28px", width: "80vh" }}
//           onClick={handleOnClick}
//         >
//           Lets Get Started
//           <span style={{ fontSize: "28px", float: "right" }}>
//             <i className="fas fa-arrow-circle-right"></i>
//           </span>
//         </p>
//       </div>

//       <div className="back" ref={myDivToFocus} id="back">
//         <div className="container featurebg" id="features" style={{}}>
//           <br />
//           <br />
//           <div className="container">
//             <div className="row" align="center">
//               <div className="col-sm-4">
//                 <Link
//                   to={{
//                     pathname: "/features/checkbalance",
//                     state: {
//                       customer: props.location.state.customer
//                     }
//                   }}
//                 >
//                   <img className="options" src={balance} alt="" />
//                 </Link>
//               </div>

//               <div className="col-sm-4">
//                 <Link
//                   to={{
//                     pathname: "/features/deposit",
//                     state: {
//                       customer: props.location.state.customer
//                     }
//                   }}
//                 >
//                   <img className="options" src={deposit} alt="" />
//                 </Link>
//               </div>

//               <div className="col-sm-4">
//                 <Link
//                   to={{
//                     pathname: "/features/withdraw",
//                     state: {
//                       customer: props.location.state.customer
//                     }
//                   }}
//                 >
//                   <img className="options" src={withdraw} alt="" />
//                 </Link>
//               </div>
//             </div>
//             <br />
//             <div className="row" align="center">
//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Check Balance</h4>
//               </div>

//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Deposit</h4>
//               </div>

//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Withdraw</h4>
//               </div>
//             </div>

//             <div className="row" align="center">
//               <div className="col-sm-4">
//                 <Link
//                   to={{
//                     pathname: "/features/transfer",
//                     state: {
//                       customer: props.location.state.customer
//                     }
//                   }}
//                 >
//                   <img className="options" src={transfer} alt="" />
//                 </Link>
//               </div>

//               <div className="col-sm-4">
//                 <Link
//                   to={{
//                     pathname: "/features/transactionscripts",
//                     state: {
//                       customer: props.location.state.customer
//                     }
//                   }}
//                 >
//                   <img className="options" src={transactions} alt="" />
//                 </Link>
//               </div>

//               <div className="col-sm-4">
//                 <img
//                   className="options"
//                   src={home}
//                   alt=""
//                   onClick={() => setModalShow(true)}
//                 />
//                 <MyVerticallyCenteredModal
//                   show={modalShow}
//                   onHide={() => setModalShow(false)}
//                 />
//               </div>
//             </div>
//             <br />
//             <div className="row" align="center">
//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Fund Transfer</h4>
//               </div>

//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Check Transactions Script</h4>
//               </div>

//               <div className="col-sm-4">
//                 <h4 className="featuretitle">Home</h4>
//               </div>
//             </div>
//           </div>
//           <br />
//           <br />
//         </div>
//       </div>
//     </>
//   );
// }
