import React, { useEffect, useState } from "react";
import styles from "./MyRequestsPage.module.css";

import { getMyExpenses } from "../services/ExpenseService";

const MyRequestsPage = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = () => {
    setLoading(true);
    getMyExpenses()
      .then((response) => {
        setRequests(response.data);
      })
      .catch((error) => {
        console.error(
          "Error loading requests:",
          error
        );
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const getStatusBadge = (status) => {
    switch(status){
      case "PENDING":
        return (
          <span className={styles.pendingBadge}>
            🟡 Pending
          </span>
        );
      case "APPROVED":
        return (
          <span className={styles.approvedBadge}>
            🟢 Approved
          </span>
        );
      case "REJECTED":
        return (
          <span className={styles.rejectedBadge}>
            🔴 Rejected
          </span>
        );
      default:
        return status;
    }
  };

  return (
    <div className={styles.container}>
      {/* PAGE HEADER */}
      <div className={styles.pageHeader}>
        <h2>
          My Expense Requests
        </h2>
      </div>
      {loading ? (
        <div className={styles.tableContainer}>
          <h3>
            Loading requests...
          </h3>
        </div>
      ) : (
        <div className={styles.tableContainer}>
          <h3>
            Request History
          </h3>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>#</th>
                <th>Title</th>
                <th>Amount</th>
                <th>Category</th>
                <th>Date</th>
                <th>Status</th>
                <th>Admin Message</th>
              </tr>
            </thead>
            <tbody>
              {requests.length > 0 ? (
                requests.map((expense,index)=>(
                  <tr key={expense.id}>
                    <td>
                      {index + 1}
                    </td>
                    <td>
                      {expense.title}
                    </td>
                    <td>
                      ₹ {Number(
                        expense.amount
                      ).toLocaleString()}
                    </td>
                    <td>
                      {
                        expense.category?.name
                        ||
                        expense.category
                      }
                    </td>
                    <td>
                      {
                        new Intl.DateTimeFormat(
                          "en-US",
                          {
                            month:"short",
                            day:"numeric",
                            year:"numeric",
                            timeZone:"UTC"
                          }
                        ).format(
                          new Date(expense.date)
                        )
                      }
                    </td>
                    <td>
                      {
                        getStatusBadge(
                          expense.status
                        )
                      }
                    </td>
                    <td>
                      {
                        expense.status === "REJECTED"
                        ?
                        (
                          <span className={styles.reason}>
                            {expense.rejectionReason}
                          </span>
                        ):(
                          "-"
                        )
                      }
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7">
                    <div className={styles.emptyMessage}>
                      No expense requests found.
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};


export default MyRequestsPage;