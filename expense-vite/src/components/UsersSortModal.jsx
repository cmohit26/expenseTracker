import React from 'react';

// UsersSortModal: Modal to pick a sort column for the users table.
const UsersSortModal = ({ isOpen, onClose, sortTableBy }) => {
  if (!isOpen) return null;

  return (
    <div id="sortModal" className="w3-modal" style={{ display: isOpen ? 'block' : 'none' }}>
      <div className="w3-modal-content w3-animate-top w3-card-4" style={{ maxWidth: '400px', marginTop: '150px' }}>
        <header className="w3-container w3-blue">
          <span onClick={onClose} className="w3-button w3-display-topright">&times;</span>
          <h3>Sort Users</h3>
        </header>
        <div className="w3-container w3-padding">
          <button className="w3-button w3-block w3-border w3-margin-bottom" onClick={() => sortTableBy('firstName')}>Sort by First Name</button>
          <button className="w3-button w3-block w3-border w3-margin-bottom" onClick={() => sortTableBy('lastName')}>Sort by Last Name</button>
          <button className="w3-button w3-block w3-border w3-margin-bottom" onClick={() => sortTableBy('email')}>Sort by Email</button>
          <button className="w3-button w3-block w3-border w3-margin-bottom" onClick={() => sortTableBy('role')}>Sort by Role</button>
          <button className="w3-button w3-block w3-red" onClick={onClose}>Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default UsersSortModal;


