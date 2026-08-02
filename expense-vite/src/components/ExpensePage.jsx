import React, { useEffect, useState } from "react";
import { deleteCategory } from "../services/CategoryService";
import styles from "./ExpensePage.module.css";

// CATEGORY APIs
import {
  getAvailableCategories,
  createCategory
} from "../services/CategoryService";

// EXPENSE APIs
import {
  getExpensesByUser,
  createExpense,
  deleteExpense
} from "../services/ExpenseService";


const ExpensePage = () => {

  // ================= EXPENSE STATE =================
  const [selectedRejectionReason, setSelectedRejectionReason] = useState(null);
  
  const [expenses,setExpenses]=useState([]);
  const [currentPage,setCurrentPage]=useState(0);
  const [totalPages,setTotalPages]=useState(0);
  const [totalExpenses,setTotalExpenses]=useState(0);
  const pageSize=10;

  const [showFilter, setShowFilter] = useState(false);

  const [filters, setFilters] = useState({
    status: "",
    categoryId: "",
    startDate: "",
    endDate: "",
    sort: "DESC"
  });

  const [formData, setFormData] = useState({
    title: "",
    amount: "",
    category: "",
    date: ""
  });

  // ================= CATEGORY STATE =================
  const [categories, setCategories] = useState([]);

  const [categoryData, setCategoryData] = useState({
    name: "",
    globalCategory:false
  });

  // ================= FETCH DATA =================
  useEffect(() => {
    fetchCategories();
    fetchExpenses();
  }, []);

  const fetchCategories = () => {
    getAvailableCategories()
      .then((res) => {
        setCategories(res.data);
      })
      .catch((err) => {
        console.error("Error fetching categories:", err);
      });
  };

  const fetchExpenses = (page=0)=>{
    getExpensesByUser(
      page,
      pageSize,
      filters
    )
    .then((res)=>{
      setExpenses(res.data.content);
      setTotalPages(
          res.data.totalPages
      );
      setTotalExpenses(
          res.data.totalElements
      );
      setCurrentPage(
          res.data.number
      );
    })
    .catch((err)=>{
        console.error(err);
    });
  };

  const handleCategoryDeleteClick = (categoryId) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this category?"
    );

    if (confirmDelete) {
      deleteCategory(categoryId)
        .then(() => {
          fetchCategories(); // Refresh the table
        })
        .catch((err) => {
          console.error("Error deleting category:", err);
        });
    }
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

    let errors = [];
    // Title validation
    if (formData.title.trim().length > 20) {
      errors.push("Expense title cannot exceed 20 characters");
    }
    // Amount validation
    const amount = Number(formData.amount);
    if (amount > 1000000) {
      errors.push("Amount cannot exceed ₹1,000,000");
    }
    // Show all errors together
    if (errors.length > 0) {
      alert(
        "Please fix the following errors:\n\n" +
        errors.map((err, index) => `${index + 1}. ${err}`).join("\n")
      );
      return;
    }

    const expensePayload = {
      title: formData.title,
      amount: formData.amount,
      categoryId: formData.category,
      date: formData.date
    };

    createExpense(expensePayload)
    .then(() => {
      fetchExpenses(); // refresh table

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
    const payload = {
      name: categoryData.name,
      globalCategory:false
    };
    createCategory(payload)
      .then(()=>{
        fetchCategories();
        
        setCategoryData({
          name:"",
          globalCategory:false
        });
      })
      .catch((err)=>{
        console.error(
          "Error adding category:",
          err
        );
      });
    };

    // ================= REJECTION REASON HANDLERS =================
    const showRejectionReason = (reason) => {
      setSelectedRejectionReason(reason);
    };
    const closeRejectionReason = () => {
      setSelectedRejectionReason(null);
    };
    
    // ================= FILTER HANDLERS =================
    const applyFilters = () => {
      fetchExpenses(0);
      setShowFilter(false);
    };
    const cancelFilters = () => {
      setShowFilter(false);
    };

  return (
    <div className={styles.container}>
      
      {/* Page Title */}
      <div className={styles.pageHeader}>
        <h2>Expense Management</h2>
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
            step={100}
            min="0"
            required
          />

          {/* CATEGORY DROPDOWN */}
          <select
            name="category"
            value={formData.category}
            onChange={handleChange}
            required
          >
          <option value="">
            Select Category
          </option>
          { categories.map((cat) => (
              <option
                key={cat.id}
                value={cat.id}
              >
                {cat.name}
              </option>
            ))
          }
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

      {/* Expense Table */}
      <div className={styles.tableContainer}>
        <div className={styles.tableHeader}>
          <h3>Expenses</h3>
          <p>
            Showing {expenses.length > 0 ? currentPage * pageSize + 1 : 0} - {Math.min((currentPage + 1) * pageSize, totalExpenses)} {" "} of {totalExpenses} expenses
          </p>
          <button className={styles.filterButton} onClick={() => setShowFilter(true)}>
              <i className="fas fa-filter"></i> Filter
          </button>
        </div>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Amount</th>
              <th>Category</th>
              <th>Date</th>
              <th>Status</th>
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
                  <td>{exp.categoryName}</td>
                  {/* <td>{exp.date?.substring(0, 10)}</td> */}
                  <td>
                    {new Intl.DateTimeFormat("en-US", {
                      month: "short",
                      day: "numeric",
                      year: "numeric",
                      timeZone: "UTC",
                    }).format(new Date(exp.date))}
                  </td>
                  <td>
                  {
                  exp.status === "PENDING" && 
                  <span className="pendingBadge">
                  🟡 Pending
                  </span>
                  }

                  {
                  exp.status === "APPROVED" &&
                  <span className="approvedBadge">
                  🟢 Approved
                  </span>
                  }

                  {
                    exp.status === "REJECTED" &&

                    <span
                      className={styles.rejectedBadge}
                      onClick={() => showRejectionReason(exp.rejectionReason)}
                      style={{cursor:"pointer"}}
                    >
                      🔴 Rejected
                    </span>
                  }
                  </td>
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
                <td> --- </td>
                <td> --- </td>
                <td> --- </td>
                <td> --- </td>
                <td> --- </td>
                <td> --- </td>
              </tr>
            )}
          </tbody>
        </table>

        {/* Pagination Controls */}
        <div className={styles.pagination}>
          <button disabled={currentPage===0} onClick={()=> fetchExpenses(currentPage-1)}>
            &lt; Previous
          </button>

          {
            Array.from(
            {length:totalPages},
            (_,index)=>index
            )
            .slice(
            Math.max(0,currentPage-2),
            currentPage+3
            )
            .map(page=>(
            <button
            key={page}
            className={
            currentPage===page
            ? styles.activePage
            : ""
            }
            onClick={()=>
            fetchExpenses(page)
            }
            >
            {page+1}
            </button>
            ))
          }

          <button disabled={currentPage===totalPages-1} onClick={()=> fetchExpenses(currentPage+1) }>
          Next &gt;
          </button>

        </div>
      </div>


      {/* ================= CATEGORY FORM ================= */}
      <div className={styles.formContainer}>
        <h3>Add A Personal Category</h3>
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

      {/* Category Table */}
      <div className={styles.tableContainer}>
        <h3>Personal Categories</h3>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>#</th>
              <th>Category</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>
            {/* Only Show Personal Categories Because of the filter */}
            {categories.filter(cat => !cat.globalCategory).length > 0 ? (
            // {categories.length > 0 ? (
              // categories.map((category, index) => (
              categories
                .filter(cat => !cat.globalCategory)
                .map((category, index) => (
                  <tr key={category.id}>
                    <td>{index + 1}</td>
                    <td>{category.name}</td>
                    <td>
                      <button
                        onClick={() => handleCategoryDeleteClick(category.id)}
                        className={styles.deleteButton}
                        title="Delete Category"
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </td>
                  </tr>
                ))
            )  : (
              <tr>
                <td> --- </td>
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

      {
        selectedRejectionReason && (
        <div className={styles.modalOverlay}>
          <div className={styles.reasonModal}>
            <h3>
              Rejection Reason
            </h3>
            <p>
              {selectedRejectionReason}
            </p>
            <button
              onClick={closeRejectionReason}
            >
              Close
            </button>
          </div>
        </div>
        )
      }

      { showFilter && (
        <div className={styles.modalOverlay}>
            <div className={styles.filterModal}>
                <h3>Filter Expenses</h3>
                {/* Status */}
                <div className={styles.filterSection}>
                  <label>Status</label>
                  <select value={filters.status} onChange={(e)=> setFilters({...filters, status:e.target.value})}>
                      <option value="">
                          All
                      </option>
                      <option value="APPROVED">
                          Approved
                      </option>
                      <option value="REJECTED">
                          Rejected
                      </option>
                      <option value="PENDING">
                          Pending
                      </option>
                  </select>
                </div>

                {/* Category */}
                <div className={styles.filterSection}>
                  <label>Category</label>
                  <select value={filters.categoryId} onChange={(e)=> setFilters({ ...filters, categoryId:e.target.value})}>
                      <option value="">
                          All Categories
                      </option>
                      {categories.map(cat=>(
                        <option key={cat.id} value={cat.id} >
                          {cat.name}
                        </option>
                      ))}
                  </select>
                </div>

                {/* Date */}
                <div className={styles.filterSection}>
                    {/* Write code here */}
                </div>
                
                {/* Sort */}
                <div className={styles.filterSection}>
                  <select value={filters.sort} onChange={(e)=> setFilters({...filters,sort:e.target.value})}>
                    <option value="DESC">
                      Newest First
                    </option>
                    <option value="ASC">
                      Oldest First
                    </option>
                  </select>
                </div>

                <div className={styles.modalButtons}>
                  <button type="button" className={styles.cancelButton} onClick={cancelFilters}>
                      Cancel
                  </button>
                  <button type="button" className={styles.applyButton} onClick={applyFilters}>
                      Apply
                  </button>
                </div>

            </div>
        </div>
      )}

    </div>
  );
};

export default ExpensePage;