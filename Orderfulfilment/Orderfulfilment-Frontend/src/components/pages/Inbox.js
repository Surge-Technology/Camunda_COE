import React from "react";
import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Inbox = () => {
  
  let navigate = useNavigate();

  const [users, setUsers] = useState([]);

  useEffect(() => {
    loadUser();
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
        `http://localhost:8080/getAssignedTaskByAssignee/OrderFullfillment/murali.muthu@surgetechinc.in/${key}`
      );
      setUsers(response.data);
      console.log(response.data)
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div className="container">
      <h2>Inbox</h2>
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
                <Link
                  className="btn btn-info"
                  to={`/user/${user.id}?data=${(JSON.stringify(users)
                  )}`}
                >
                  View
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Inbox;
