import React, { useEffect, useState } from "react";
import tableStyles from "./UsersTable.module.css";

import {
  addIncome,
  getMyIncomes,
  updateIncome,
  deleteIncome,
} from "../services/IncomeServices"; // adjust path if needed

function IncomePage() {
  const [incomes, setIncomes] = useState([]);

  const [formData, setFormData] = useState({
    amount: "",
    source: "",
    date: "",
  });

  const [editId, setEditId] = useState(null);

  // ✅ Load incomes (IMPORTANT: replace userId with your logged-in user later)
  const loadIncomes = async () => {
    try {
      const res = await getMyIncomes();
      setIncomes([...res.data]); // force fresh reference
    } catch (err) {
      console.error("Error loading incomes:", err);
    }
  };

  useEffect(() => {
    (async () => {
      await loadIncomes();
    })();
  }, []);

  // input handler
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // submit (create or update)
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (editId) {
        await updateIncome(editId, formData);
      } else {
        await addIncome(formData);
      }

      setFormData({ amount: "", source: "", date: "" });
      setEditId(null);

      // 🔥 IMPORTANT: wait for reload
      await loadIncomes();

    } catch (err) {
      console.error("Error saving income:", err);
    }
  };

  // delete
  const handleDelete = async (id) => {
    try {
      await deleteIncome(id);

      // 🔥 wait for backend update
      await loadIncomes();

    } catch (err) {
      console.error("Error deleting income:", err);
    }
  };

  // edit
  const handleEdit = (income) => {
    setFormData({
      amount: income.amount,
      source: income.source,
      date: income.date,
    });
    setEditId(income.id);
  };

  return (
    <div className={tableStyles.container}>
      {/* TABLE */}
      <div className={tableStyles.panel}>
        <h5 className={tableStyles.tableTitle}>
          <b>All Income Details</b>
        </h5>

        <table className={tableStyles.table}>
          <thead>
            <tr>
              <th>Source</th>
              <th>Date</th>
              <th>Amount</th>
              <th>Edit</th>
              <th>Delete</th>
            </tr>
          </thead>

          <tbody>
            {incomes.map((income) => (
              <tr key={income.id}>
                <td>{income.source}</td>
                <td>{income.date}</td>
                <td>Rs. {income.amount}</td>

                <td>
                  <button
                    className={tableStyles.editButton}
                    onClick={() => handleEdit(income)}
                  >
                    <i className="fa fa-pencil"></i>
                  </button>
                </td>

                <td>
                  <button
                    className={tableStyles.deleteButton}
                    onClick={() => handleDelete(income.id)}
                  >
                    <i className="fa fa-trash"></i>
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* FORM */}
      <div className={tableStyles.panel}>
        <form onSubmit={handleSubmit}>
          <div className="w3-section">
            <input
              className="w3-input w3-border w3-round-large"
              type="text"
              placeholder="Income Source (Salary, Freelance, etc)"
              name="source"
              value={formData.source}
              onChange={handleChange}
            />
          </div>

          <div className="w3-section">
            <input
              className="w3-input w3-border w3-round-large"
              type="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
            />
          </div>

          <div className="w3-section">
            <input
              className="w3-input w3-border w3-round-large"
              type="number"
              placeholder="Amount (Rs)"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
            />
          </div>

          <button
            type="submit"
            className="w3-button w3-blue w3-round-large"
            style={{ width: "100%" }}
          >
            {editId ? "Update Income" : "Submit Income"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default IncomePage;