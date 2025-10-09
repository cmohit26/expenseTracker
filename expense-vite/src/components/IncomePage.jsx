import React, { useState } from 'react';
import tableStyles from './UsersTable.module.css';

function IncomePage() {
  const [formData, setFormData] = useState({
    detail: '',
    category: '',
    date: '',
    amount: '',
    description: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert('Income submitted');
    setFormData({ detail: '', category: '', date: '', amount: '', description: '' });
  };

  return (
    <div className={tableStyles.container}>
      <div className={tableStyles.panel}>
        <h5 className={tableStyles.tableTitle}><b>All Income Details</b></h5>
        <table className={tableStyles.table}>
          <thead>
            <tr>
              <th>Income Detail</th>
              <th>DATE</th>
              <th>$ AMOUNT</th>
              <th>EDIT</th>
              <th>DELETE</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Salary Credit</td>
              <td>23-04-2025</td>
              <td>Rs. 50,000</td>
              <td>
                <button className={tableStyles.editButton} title="Edit"><i className="fa fa-pencil"></i></button>
              </td>
              <td>
                <button className={tableStyles.deleteButton} title="Delete"><i className="fa fa-trash"></i></button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div className={tableStyles.panel}>
        <form onSubmit={handleSubmit}>
          <div className="w3-section">
            <input className="w3-input w3-border w3-round-large" type="text" placeholder="Income Detail" name="detail" value={formData.detail} onChange={handleChange} />
          </div>
          <div className="w3-section">
            <input className="w3-input w3-border w3-round-large" type="text" placeholder="Category (Mostly Give a Drop Dwon)" name="category" value={formData.category} onChange={handleChange} />
          </div>
          <div className="w3-section">
            <div className="w3-row">
              <div className="w3-col s12">
                <input className="w3-input w3-border w3-round-large" type="date" placeholder="dd-mm-yyyy" name="date" value={formData.date} onChange={handleChange} />
              </div>
            </div>
          </div>
          <div className="w3-section">
            <input className="w3-input w3-border w3-round-large" type="number" placeholder="Amount (Rs)" name="amount" value={formData.amount} onChange={handleChange} />
          </div>
          <div className="w3-section">
            <textarea className="w3-input w3-border w3-round-large" rows="4" placeholder="Income Description ( If Any )" name="description" value={formData.description} onChange={handleChange} />
          </div>
          <button type="submit" className="w3-button w3-blue w3-round-large" style={{ width: '100%' }}>Submit Income</button>
        </form>
      </div>
    </div>
  );
}

export default IncomePage;


