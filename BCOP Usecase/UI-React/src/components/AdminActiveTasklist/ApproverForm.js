import Axios from 'axios';
import React from 'react';
import { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import Sidebar from '../sidebar/sideBar';
import Header from '../sidebar/header';
const AdminActiveTasklistDetail = (props) => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    loadUser();
  }, []);

  const loadUser = async () => {
    try {
      const email=sessionStorage.getItem('email');
      console.log(" Login Email from sessionstorage ",email)
      const response = await Axios.get(`http://localhost:8080/assignedTaskList?userName=demo`);
     // alert("email User Name"+email);
      setUsers(response.data);
      console.log("resssss"+JSON.stringify(response.data));
      
    } catch (error) {
      console.error("Error fetching user data:", error);
    }
  }
  const handleViewClick = (userId) => {
    // Set the userId in sessionStorage when "View" is clicked
    sessionStorage.setItem('userId', userId);
    console.log(userId);
    
  }

  return (

 <div className="approver-list-container">
     

      <div className="main-content">
        
        <Header />
        

        <div className="page-content">      
      <table className="table table-striped table-lg">
        <thead className="thead-dark">
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Name</th>
            <th scope="col">Assignee</th>
            <th scope="col">Creation Date</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? ( // Add a check for the array length
            users.map((user) => (
               <tr key={user.id}>

                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.assignee}</td>
                <td>{user.creationTimes}</td>
                <td>
                  <Link
                    className="btn btn-success w-50"
                    to={`/ActiveTaskList/${user.id}`}
                    onClick={() => handleViewClick(user.id)} // Call the function to set userId in sessionStorage
                  >
                    View
                  </Link>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5">No data found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
    </div>
    </div>
  )
}

export default AdminActiveTasklistDetail;
