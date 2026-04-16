import React, { useEffect, useState } from "react";
import styles from "./ExpensePage.module.css";

// CATEGORY APIs
import {
  getAllCategories,
  createCategory
} from "../services/CategoryService";

// EXPENSE APIs
import {
  getRecentExpenses,
  createExpense,
  deleteExpense
} from "../services/ExpenseService";


const ExpensePage = () => {

  // ================= EXPENSE STATE =================
  const [expenses, setExpenses] = useState([]);

  const [formData, setFormData] = useState({
    title: "",
    amount: "",
    category: "",
    date: ""
  });

  // ================= CATEGORY STATE =================
  const [categories, setCategories] = useState([]);

  const [categoryData, setCategoryData] = useState({
    name: ""
  });

  // ================= FETCH DATA =================
  useEffect(() => {
    fetchCategories();
    fetchRecentExpenses();
  }, []);

  const fetchCategories = () => {
    getAllCategories()
      .then((res) => {
        setCategories(res.data);
      })
      .catch((err) => {
        console.error("Error fetching categories:", err);
      });
  };

  const fetchRecentExpenses = () => {
    getRecentExpenses()
      .then((res) => {
        setExpenses(res.data);
      })
      .catch((err) => {
        console.error("Error fetching expenses:", err);
      });
  };

  // ================= EXPENSE HANDLERS =================
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmitExpense = (e) => {
    e.preventDefault();

    const expensePayload = {
      title: formData.title,
      amount: formData.amount,
      categoryId: formData.category,
      date: formData.date
    };

    createExpense(expensePayload)
      .then(() => {
        fetchRecentExpenses(); // refresh table

        setFormData({
          title: "",
          amount: "",
          category: "",
          date: ""
        });
      })
      .catch((err) => {
        console.error("Error adding expense:", err);
      });
  };

  const handleExpenseDeleteClick = (expenseId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this expense?"
    );

    if (confirmDelete) {
      deleteExpense(expenseId)
        .then(() => {
          fetchRecentExpenses(); // refresh table
        })
        .catch((error) => {
          console.error("Error deleting expense:", error);
        });
    }
  };

  // ================= CATEGORY HANDLERS =================
  const handleCategoryChange = (e) => {
    setCategoryData({
      ...categoryData,
      [e.target.name]: e.target.value
    });
  };

  const handleAddCategory = (e) => {
    e.preventDefault();

    createCategory(categoryData)
      .then(() => {
        fetchCategories();
        setCategoryData({ name: "" });
      })
      .catch((err) => {
        console.error("Error adding category:", err);
      });
  };

  return (
    <div className={styles.container}>

      {/* Page Title */}
      <div className={styles.pageHeader}>
        <h2>Expense Management</h2>
      </div>

      {/* Expense Table */}
      <div className={styles.tableContainer}>
        <h3>Recent Expenses</h3>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Amount</th>
              <th>Category</th>
              <th>Date</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>
            {expenses.length > 0 ? (
              expenses.map((exp, index) => (
                <tr key={exp.id}>
                  <td>{index + 1}</td>
                  <td>{exp.title}</td>
                  <td>₹ {Number(exp.amount).toLocaleString()}</td>
                  <td>{exp.category?.name || exp.category}</td>
                  <td>{exp.date?.substring(0, 10)}</td>
                  <td>
                    <button
                      onClick={() => handleExpenseDeleteClick(exp.id)}
                      className={styles.deleteButton}
                      title="Delete Expense"
                    >
                      <i className="fas fa-trash"></i>
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6">No expenses found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* ================= EXPENSE FORM ================= */}
      <div className={styles.formContainer}>
        <h3>Add New Expense</h3>
        <form onSubmit={handleSubmitExpense} className={styles.form}>

          <input
            type="text"
            name="title"
            placeholder="Expense Title"
            value={formData.title}
            onChange={handleChange}
            required
          />

          <input
            type="number"
            name="amount"
            placeholder="Amount"
            value={formData.amount}
            onChange={handleChange}
            required
          />

          {/* CATEGORY DROPDOWN */}
          <select
            name="category"
            value={formData.category}
            onChange={handleChange}
            required
          >
            <option value="">Select Category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>

          <input
            type="date"
            name="date"
            value={formData.date}
            onChange={handleChange}
            required
          />

          <button type="submit">Add Expense</button>
        </form>
      </div>

      {/* ================= CATEGORY FORM ================= */}
      <div className={styles.formContainer}>
        <h3>Add New Category</h3>
        <form onSubmit={handleAddCategory} className={styles.form}>

          <input
            type="text"
            name="name"
            placeholder="Category Title"
            value={categoryData.name}
            onChange={handleCategoryChange}
            required
          />

          <button type="submit">Add Category</button>
        </form>
      </div>

    </div>
  );
};

export default ExpensePage;