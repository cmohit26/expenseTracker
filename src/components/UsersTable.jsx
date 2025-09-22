import React, {useEffect, useState} from 'react';
import styles from './UsersTable.module.css';
import { listOfUsers, deleteUser } from '../services/UserServices';

// UsersTable: Displays users with search, sort trigger, edit/delete actions.
const UsersTable = ({ openEditModal, openSortModal }) => {

  const [users, setUsers] = useState([])
  
  const loadUsers = () => {
  listOfUsers()
    .then((response) => {
      setUsers(response.data);
    })
    .catch((error) => {
      console.error("❌ Error loading users:", error);
    });
  };
  
  useEffect(() => {
    loadUsers();
  }, [])
  
  const handleEditClick = (userId) => {
    openEditModal(userId);
  };
  
  const handleDeleteClick = (userId, userName) => {
    const confirmDelete = window.confirm(`Are you sure you want to delete user "${userName}"? This action cannot be undone.`);
    
    if (confirmDelete) {
      deleteUser(userId)
        .then(() => {
          alert('User deleted successfully!');
          loadUsers();
        })
        .catch(error => {
          console.error('Error deleting user:', error);
          alert('Error deleting user. Please try again.');
        });
    }
  };

  return (
    <div className={styles.container}>

      {/* Page Title */}
      <div className={styles.pageHeader}>
        <h2></h2>
        <h2>Admin Page (Make it only accessible to admins)</h2>
        <h2></h2>
      </div>

      <div className={styles.panel}>
        <div className={styles.rowPadding}>
          <div>
            <form action="/admin/users" method="get" className={styles.searchForm}>
              
              <input type="text" id="searchBox" name="search" placeholder="Search..." className={styles.searchInput} />

              <button type="submit" className={styles.searchButton}>Search</button>

            </form>
            <button onClick={openSortModal} className={styles.sortButton}>
              Sort Table &gt;
            </button>
            <h5 className={styles.tableTitle}><b>All Transaction Details</b></h5>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>sql ID</th>
                  <th>ID</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Role</th>
                  <th>Email ID</th>
                  <th>Edit</th>
                  <th>Delete</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user, index) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{index + 1}</td>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.role}</td>
                    <td>{user.email}</td>
                    <td>
                      <button
                        onClick={() => handleEditClick(user.id)}
                        className={styles.editButton}
                        title="Edit User"
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                    </td>
                    <td>
                      <button
                        onClick={() => handleDeleteClick(user.id, `${user.firstName} ${user.lastName}`)}
                        className={styles.deleteButton}
                        title="Delete User"
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <hr />
    </div>
  );
};

export default UsersTable;


