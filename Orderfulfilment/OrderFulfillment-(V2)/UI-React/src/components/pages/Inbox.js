import React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Inbox = () => {
  
  let navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [showTable, setShowTable] = useState(false);

  // useEffect(() => {
  //   loadUser();
  // }, []);
  useEffect(() => {
    let data = sessionStorage.getItem("mySessionStorageData");
 
    data = JSON.parse(data);
 
    const loggedInUserId = data;
 
    const username = loggedInUserId.username;
 
    console.log(username, "Username");
 
    if (username === "Admin") {
      setShowTable(true);
      loadUser(); 
    } else {
      setShowTable(false);
      }
  }, []);
  const loadUser = async () => {
    let key = sessionStorage.getItem("processInstanceKey");
    let data = sessionStorage.getItem("mySessionStorageData");

    data = JSON.parse(data);
    
  
    console.log(data, "cartdata");
  console.log(key,'keyyyyy');
    try {
     // alert(`http://localhost:8080/getAssignedTaskByAssignee/OrderFullfillment/murali.muthu@surgetechinc.in/${key}`);

      
      const response = await axios.get(
        `http://localhost:8080/getAssignedTask/OrderFullfillment/murali.muthu@surgetechinc.in`
      );
      setUsers(response.data);
      console.log(response.data)
    } catch (error) {
      console.error("Error:", error);
    }
  };
  const viewData = (id) => {
    navigate(`/user/${id}`); 
    sessionStorage.setItem('taskId',id);
  let idoftask=  sessionStorage.getItem('taskId');
  console.log(idoftask,"23456");
  
  };
  
  return (
    <div className="container">
      <h2>Inbox</h2>
      {showTable ? (
      <table className="table">
        <thead>
          <tr className="bg-dark text-white">
            <th scope="col">Task ID</th>
            <th scope="col">Task Name</th>
            <th scope="col">Status</th>
            <th scope="col">Process Name</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.name}</td>
              <td>{user.taskState}</td>
              <td>{user.processName}</td>
              <td>
              <button
                    className="btn btn-info"
                    onClick={() => viewData(user.id)} 
                  >
                    View
                  </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
       ) : (
        <p>You are not authorized to view this Page.</p>
      )}
    </div>
  );
};

export default Inbox;
