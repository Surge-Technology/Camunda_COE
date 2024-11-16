import React from 'react';
import { useHistory } from 'react-router-dom';

const ActiveTaskList = (props) => {
  const history = useHistory();

  const handleOpenButtonClick = () => {
    // Replace '/customer-query' with the actual path of the CustomerQuery page
    props.history.push('customerQueryPage');
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>Customer Inbox</div>
      <table style={styles.table}>
        <thead>
          <tr>
            <th scope="col">Sl.No</th>
            <th scope="col">Active</th>
            <th scope="col">Task list</th>
            <th scope="col">Details</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>1</td>
            <td>Task 1</td>
            <td>
              <button style={styles.button} onClick={handleOpenButtonClick}>
                Open
              </button>
            </td>
          </tr>
          <tr>
            <td>2</td>
            <td>1</td>
            <td>Task 2</td>
            <td>
              <button style={styles.button} onClick={handleOpenButtonClick}>
                Open
              </button>
            </td>
          </tr>
          {/* Add more rows as needed */}
        </tbody>
      </table>
    </div>
  );
};

const styles = {
    container: {
        maxWidth: '1100px',
        margin: '0 auto',
        padding: '20px',
        border: '12px solid #ccc',
        borderRadius: '5px',
        backgroundColor: '#f0f0f0',
        height: '550px', // Increase the height to 500px (or any desired value)
        overflow: 'auto', // Add overflow:auto to add a scrollbar if content exceeds the height
      },
      table: {
        width: '100%',
        height: '600px', // Set the desired height for the table (if fixed height is needed)
        borderCollapse: 'collapse',
      },
  header: {
    fontSize: '24px',
    fontWeight: 'bold',
    marginBottom: '20px',
    color: '#fff',
    backgroundColor: '#007bff',
    padding: '10px',
    textAlign: 'center',
    borderRadius: '5px',
  },
  table: {
    width: '100%',
    borderCollapse: 'collapse',
  },
  button: {
    padding: '6px 12px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
};

export default ActiveTaskList;
