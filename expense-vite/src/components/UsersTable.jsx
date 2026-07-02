import React, { useEffect, useState } from 'react';
import styles from './UsersTable.module.css';
import { listOfUsers, deleteUser, getSortedUsers } from '../services/UserServices';
import { getUserCategories, getAvailableCategories, createCategory, deleteCategory} from "../services/CategoryService";

// UsersTable: Displays users with search, sort trigger, edit/delete actions.
const UsersTable = ({ openEditModal, openSortModal, sortColumn }) => {

  const [users, setUsers] = useState([]);
  const [categories, setCategories] = useState([]);
  const [categoryData, setCategoryData] = useState({
    name: "",
    globalCategory: true,
  });

  const loadCategories = () => {
    getAvailableCategories().then((res) => {
      console.log(res.data);
      setCategories(res.data);
    });
  };

  const handleCategoryChange = (e) => {
    setCategoryData({
      ...categoryData,
      [e.target.name]: e.target.value,
    });
  };

  const handleAddGlobalCategory = (e) => {
    e.preventDefault();
    createCategory({
      name: categoryData.name,
      globalCategory: true,
    })
    .then(() => {
      loadCategories();

      setCategoryData({
        name: "",
        globalCategory: true,
      });
    })
    .catch((err) => {
      console.error(err);
    });
  };

  const loadUsers = () => {
    listOfUsers()
      .then((response) => {
        setUsers(response.data);
      })
      .catch((error) => {
        console.error("❌ Error loading users:", error);
      });
  };

  const loadSortedUsers = (column) => {
    getSortedUsers(column)
      .then((response) => {
        setUsers(response.data);
      })
      .catch((error) => {
        console.error("❌ Error sorting users:", error);
      });
  };

  // 🔥 Load users on first render
  useEffect(() => {
    loadUsers();
    loadCategories();
  }, []);

  // 🔥 React to sorting changes
  useEffect(() => {
    if (sortColumn) {
      loadSortedUsers(sortColumn);
    }
  }, [sortColumn]);

  const handleUserEditClick = (userId) => {
    openEditModal(userId);
  };

  const handleUserDeleteClick = (userId, userName) => {
    const confirmDelete = window.confirm(
      `Are you sure you want to delete user "${userName}"? This action cannot be undone.`
    );

    if (confirmDelete) {
      deleteUser(userId)
        .then(() => {
          alert('User deleted successfully!');
          
          // 🔁 Reload depending on sort state
          if (sortColumn) {
            loadSortedUsers(sortColumn);
          } else {
            loadUsers();
          }
        })
        .catch(error => {
          console.error('Error deleting user:', error);
          alert('Error deleting user. Please try again.');
        });
    }
  };

  const handleCategoryDeleteClick = (categoryId, categoryName) => {
    const confirmDelete = window.confirm(
      `Are you sure you want to delete Category "${categoryName}"? This action cannot be undone.`
    );

    if (confirmDelete) {
      deleteCategory(categoryId)
        .then(() => {
          alert('Category deleted successfully!');
          
          // 🔁 Reload depending on sort state
          if (sortColumn) {
            loadSortedUsers(sortColumn);
          } else {
            loadUsers();
          }
        })
        .catch(error => {
          console.error('Error deleting category:', error);
          alert('Error deleting category. Please try again.');
        });
    }
  };

  return (
  
    <div className={styles.container}>

      {/* Category Table */}
      <div className={styles.pageHeader}>
        <h2></h2>
        <h2>Category Table</h2>
        <h2></h2>
      </div>

      <div className={styles.panel}>

        <h2>Add A Global Category</h2>
        <form onSubmit={handleAddGlobalCategory} className={styles.form}>
          <input
            type="text"
            name="name"
            placeholder="Category Title"
            value={categoryData.name}
            onChange={handleCategoryChange}
            required
          />
          <button type="submit">
            Add Category
          </button>
        </form>

        <div className={styles.rowPadding}>

          <h5
            className={styles.tableTitle}
            style={{ marginTop: "25px" }}
          >
            <b>All Categories</b>
          </h5>

          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Category Name</th>
                <th>Category Type</th>
                <th>Created By</th>
                <th>Edit</th>
                <th>Delete</th>
              </tr>
            </thead>
            <tbody>
              {categories.length > 0 ? (
                categories.map((category) => (
                  <tr key={category.id}>
                    <td>{category.id}</td>
                    <td>{category.name}</td>
                    <td> {category.globalCategory ? "Global" : "Personal"} </td>
                    <td>
                      {category.globalCategory
                        ? "Admin"
                        : `${category.user?.firstName ?? ""} ${category.user?.lastName ?? ""}`}
                    </td>
                    {/* Edit Button */}
                    <td> FIX
                      <button
                        className={styles.editButton}
                        title="Edit Category"
                        onClick={() => handleCategoryEditClick(category.id)}
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                    </td>
                    {/* Delete Button */}
                    <td> BACKEND FIX
                      <button
                        className={styles.deleteButton}
                        title="Delete Category"
                        onClick={() => handleCategoryDeleteClick(category.id, category.name)}
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" style={{ textAlign: "center" }}>
                    No Categories Found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

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
              <input
                type="text"
                id="searchBox"
                name="search"
                placeholder="Search..."
                className={styles.searchInput}
              />
              <button type="submit" className={styles.searchButton}>
                Search
              </button>
            </form>

            <button onClick={openSortModal} className={styles.sortButton}>
              Sort Table &gt;
            </button>

            <h5 className={styles.tableTitle}>
              <b>All Transaction Details</b>
            </h5>

            <table className={styles.table}>
              <thead>
                <tr>
                  <th>sql ID</th>
                  <th>ID</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Date of Birth</th>
                  <th>Age</th>
                  <th>Role</th>
                  <th>Email ID</th>
                  <th>Edit</th>
                  <th>Delete</th>
                </tr>
              </thead>
              <tbody>
                {users.length > 0 ? (  
                  users.map((user, index) => (
                    <tr key={user.id}>
                      <td>{user.id}</td>
                      <td>{index + 1}</td>
                      <td>{user.firstName}</td>
                      <td>{user.lastName}</td>
                      <td>{user.dateOfBirth}</td>
                      <td>{user.age}</td>
                      <td>{user.roles}</td>
                      <td>{user.email}</td>
                      <td>
                        <button
                          onClick={() => handleUserEditClick(user.id)}
                          className={styles.editButton}
                          title="Edit User"
                        >
                          <i className="fas fa-edit"></i>
                        </button>
                      </td>
                      <td>
                        <button
                          onClick={() =>
                            handleUserDeleteClick(user.id, `${user.firstName} ${user.lastName}`)
                          }
                          className={styles.deleteButton}
                          title="Delete User"
                        >
                          <i className="fas fa-trash"></i>
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td> --- </td>
                    <td> --- </td>
                    <td> --- </td>
                    <td> --- </td>
                    <td> NOT LOGGED IN </td>
                    <td> --- </td>
                    <td> --- </td>
                    <td> --- </td>
                    <td> --- </td>
                    <td> --- </td>
                  </tr>
                )}
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