import React, { useEffect, useState } from "react";
import styles from "./AdminPage.module.css";

import {
  getPendingExpenses,
  getPendingIncomes,
  approveExpense,
  approveIncome,
  rejectExpense,
  rejectIncome
} from "../services/AdminService";

const AdminPage = () => {
  const [pendingExpenses, setPendingExpenses] = useState([]);
  const [pendingIncomes, setPendingIncomes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showRejectModal, setShowRejectModal] = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [selectedType, setSelectedType] = useState("");
  const [rejectionReason, setRejectionReason] = useState("");

  useEffect(() => {
    fetchPendingRequests();
  }, []);

  const fetchPendingRequests = () => {

    setLoading(true);

    Promise.all([
      getPendingExpenses(),
      getPendingIncomes()
    ])
      .then(([expenseResponse, incomeResponse]) => {

        setPendingExpenses(expenseResponse.data);

        setPendingIncomes(incomeResponse.data);

      })
      .catch((error) => {

        console.error("Error loading pending requests:", error);

      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleApproveExpense = (expenseId) => {
    approveExpense(expenseId)
      .then(() => {
        fetchPendingRequests();
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const handleApproveIncome = (incomeId) => {
    approveIncome(incomeId)
      .then(() => {
        fetchPendingRequests();
      })
      .catch((error) => {
        console.error(error);
      });
  };

  const openExpenseRejectModal = (expenseId) => {
    setSelectedId(expenseId);
    setSelectedType("expense");
    setRejectionReason("");
    setShowRejectModal(true);
  };

  const openIncomeRejectModal = (incomeId) => {
    setSelectedId(incomeId);
    setSelectedType("income");
    setRejectionReason("");
    setShowRejectModal(true);
  };

  const closeRejectModal = () => {
    setShowRejectModal(false);
    setSelectedId(null);
    setSelectedType("");
    setRejectionReason("");
  };

  const handleReject = () => {
    if (rejectionReason.trim() === "") {
      alert("Please enter a rejection reason.");
      return;
    }

    if (selectedType === "expense") {
      rejectExpense(selectedId, rejectionReason)
        .then(() => {
          fetchPendingRequests();
          closeRejectModal();
        })
        .catch(console.error);
    }

    if (selectedType === "income") {
      rejectIncome(selectedId, rejectionReason)
        .then(() => {
          fetchPendingRequests();
          closeRejectModal();
        })
        .catch(console.error);
    }
  };

  return (
    <div className={styles.container}>
      {/* ================= PAGE HEADER ================= */}
      <div className={styles.pageHeader}>
        <h2>Admin Approval Dashboard</h2>
      </div>
      {/* ================= LOADING ================= */}
      {loading && (
        <div className={styles.tableContainer}>
          <h3>Loading pending requests...</h3>
        </div>
      )}

      {/* ================= EXPENSE REQUESTS ================= */}

      {!loading && (
      <div className={styles.tableContainer}>
        <h3>Pending Expense Requests</h3>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>User</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Date</th>
              <th>Approve</th>
              <th>Reject</th>
            </tr>
          </thead>

          <tbody>
            {pendingExpenses.length > 0 ? (
              pendingExpenses.map((expense, index) => (
                <tr key={expense.id}>
                  <td>{index + 1}</td>
                  <td>{expense.title}</td>
                  <td>
                    {expense.userFirstName} {expense.userLastName}
                  </td>
                  <td>
                    {expense.categoryName}
                  </td>
                  <td>
                    ₹ {Number(expense.amount).toLocaleString()}
                  </td>

                  <td>

                    {new Intl.DateTimeFormat("en-US", {
                      month: "short",
                      day: "numeric",
                      year: "numeric",
                      timeZone: "UTC"
                    }).format(new Date(expense.date))}
                  </td>
                  <td>

                    <button
                      className={styles.approveButton}
                      onClick={() =>
                         handleApproveExpense(expense.id)
                      }
                    >
                      <i className="fas fa-check"></i>
                    </button>
                  </td>
                  <td>
                    <button
                      className={styles.rejectButton}
                      onClick={() =>
                        openExpenseRejectModal(expense.id)
                      }
                    >
                      <i className="fas fa-times"></i>
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8">
                  <div className={styles.emptyMessage}>
                    No pending expense requests.
                  </div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
      )}

            {/* ================= INCOME REQUESTS ================= */}
      <div className={styles.tableContainer}>
        <h3>Pending Income Requests</h3>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>User</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Date</th>
              <th>Approve</th>
              <th>Reject</th>
            </tr>
          </thead>
          <tbody>

            {pendingIncomes.length > 0 ? (
              pendingIncomes.map((income, index) => (
                <tr key={income.id}>
                  <td>{index + 1}</td>
                  <td>{income.source}</td>
                  <td>
                    {income.userFirstName} {income.userLastName}
                  </td>
                  <td>
                    {income.categoryName}
                  </td>
                  <td>
                    ₹ {Number(income.amount).toLocaleString()}
                  </td>
                  <td>
                    {new Intl.DateTimeFormat("en-US", {
                      month: "short",
                      day: "numeric",
                      year: "numeric",
                      timeZone: "UTC"
                    }).format(new Date(income.date))}
                  </td>
                  <td>
                    <button
                      className={styles.approveButton}
                      onClick={() => handleApproveIncome(income.id)}
                      title="Approve Income"
                    >
                      <i className="fas fa-check"></i>
                    </button>
                  </td>
                  <td>
                    <button
                      className={styles.rejectButton}
                      onClick={() => openIncomeRejectModal(income.id)}
                      title="Reject Income"
                    >
                      <i className="fas fa-times"></i>
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8">
                  <div className={styles.emptyMessage}>
                    No pending income requests.
                  </div>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

              {/* ================= REJECTION MODAL ================= */}
      {showRejectModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modalContainer}>
            <h3>
              Reject Request
            </h3>
            <textarea
              className={styles.reasonInput}
              placeholder="Enter rejection reason..."
              value={rejectionReason}
              onChange={(e) =>
                setRejectionReason(e.target.value)
              }
              rows="5"
            />
            <div className={styles.modalButtons}>
              <button
                className={styles.cancelButton}
                onClick={closeRejectModal}
              >
                Cancel
              </button>

              <button
                className={styles.rejectConfirmButton}
                onClick={handleReject}
              >
                Reject
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
export default AdminPage;
