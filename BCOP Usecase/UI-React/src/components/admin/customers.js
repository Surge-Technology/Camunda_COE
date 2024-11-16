// import React, { useEffect, useState } from "react";
// import axios from "axios";
// import auth from "../../service/adminauth";
// import { API_BASE_URL } from "../../service/service";
// export default function Customers() {
//   const [state, setState] = useState({
//     customers: []
//   });

//   useEffect(() => {
//     getCustomer();
//   }, [state.customers]);

//   function getCustomer() {
//     console.log();
//     axios
//       .get(API_BASE_URL + "admin/allcustomers", {
//         headers: auth.getHeader()
//       })
//       .then(data => {
//         setState({ ...state, customers: data.data });
//       })
//       .catch(console.error());
//   }

//   function renderTable() {
//     if (state.customers !== null) {
//       return (
//         <tbody>
//           {state.customers.map((item, index) => {
//             return (
//               <tr key={item.customerId}>
//                 <td>{index + 1}</td>
//                 <td>{item.customerId}</td>
//                 <td>{item.customerName}</td>
//                 <td>{item.email}</td>
//                 <td>{item.mobile}</td>
//                 <td>{item.user.userName}</td>
//                 <td>{item.user.lockStatus}</td>
//               </tr>
//             );
//           })}
//         </tbody>
//       );
//     }
//   }

//   return (
//     <div className="container-fluid back">
//       <div
//         className="jumbotron"
//         style={{ padding: "10px", position: "fixed", width: "100%" }}
//       >
//         <div
//           className="display-4 font-italic text-center"
//           style={{ position: "relative", right: "6%" }}
//         >
//           All Customers
//         </div>
//       </div>
//       <div className="conatiner" style={{ paddingTop: "8%" }}>
//         <table className="table table-hover">
//           <thead>
//             <tr>
//               <th>#</th>
//               <th>Cust ID</th>
//               <th>Name</th>
//               <th>Mail</th>
//               <th>Mobile</th>
//               <th>User Name</th>
//               <th>Status</th>
//             </tr>
//           </thead>
//           {renderTable()}
//         </table>
//       </div>
//     </div>
//   );
// }
