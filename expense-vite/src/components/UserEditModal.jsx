import React, { useState, useEffect } from 'react';
import { getUserById, updateUser } from '../services/UserServices';

// UserEditModal: Modal for editing a user's profile fields.
const UserEditModal = ({ isOpen, onClose, userId, onUserUpdated }) => {
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    role: 'USER',
    password: ''
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isOpen && userId) {
      loadUserData();
    }
  }, [isOpen, userId]);

  const loadUserData = async () => {
    try {
      setLoading(true);
      const response = await getUserById(userId);
      const user = response.data;
      setUserData({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
        role: user.role || 'USER',
        password: user.password || ''
      });
    } catch (error) {
      console.error('Error loading user data:', error);
      alert('Error loading user data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      await updateUser(userId, userData);
      alert('User updated successfully!');
      onUserUpdated();
      onClose();
    } catch (error) {
      console.error('Error updating user:', error);
      alert('Error updating user. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div id="editModal" className="w3-modal" style={{ display: isOpen ? 'block' : 'none' }}>
      <div className="w3-modal-content w3-animate-top w3-card-4" style={{ maxWidth: '600px', marginTop: '100px' }}>
        <header className="w3-container w3-blue">
          <span onClick={onClose} className="w3-button w3-display-topright">&times;</span>
          <h2>Edit User</h2>
        </header>
        <div className="w3-container">
          {loading ? (
            <div className="w3-center w3-padding">
              <p>Loading...</p>
            </div>
          ) : (
            <form id="editForm" onSubmit={handleSubmit}>
              <label>First Name</label>
              <input 
                className="w3-input w3-border" 
                type="text" 
                name="firstName" 
                value={userData.firstName}
                onChange={handleInputChange}
                required 
              />
              <label>Last Name</label>
              <input 
                className="w3-input w3-border" 
                type="text" 
                name="lastName" 
                value={userData.lastName}
                onChange={handleInputChange}
                required 
              />
              <label>Email</label>
              <input 
                className="w3-input w3-border" 
                type="email" 
                name="email" 
                value={userData.email}
                onChange={handleInputChange}
                required 
              />
              <label>Role</label>
              <select 
                className="w3-select w3-border" 
                name="role" 
                value={userData.role}
                onChange={handleInputChange}
                required
              >
                <option value="Admin">Admin</option>
                <option value="USER">USER</option>
              </select>
              <label>Password</label>
              <input 
                className="w3-input w3-border" 
                type="text" 
                name="password" 
                value={userData.password}
                onChange={handleInputChange}
                required 
              />
              <br />
              <button 
                className="w3-button w3-green" 
                type="submit"
                disabled={loading}
              >
                {loading ? 'Saving...' : 'Save'}
              </button>
              <button 
                className="w3-button w3-red" 
                type="button" 
                onClick={onClose}
                disabled={loading}
              >
                Cancel
              </button>
              <br /><br />
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserEditModal;


