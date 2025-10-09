import React, { useState, useEffect } from 'react';
// App: Root component controlling navigation, auth state, and modals.
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import UsersTable from './components/UsersTable';
import UserEditModal from './components/UserEditModal';
import UsersSortModal from './components/UsersSortModal';
import HomeDashboard from './components/HomeDashboard';
import IncomePage from './components/IncomePage';
import AuthPage from './components/AuthPage';
import { getCurrentUser } from './services/UserServices';  // Import the API call
import { logoutUser } from './services/UserServices';  // Ensure correct import

function App() {
  const [currentPage, setCurrentPage] = useState(() => {
    // Try to restore last visited page, default to 'auth'
    return localStorage.getItem('currentPage') || 'auth';
  });
  const [currentUser, setCurrentUser] = useState(null); // State to hold logged-in user info
  const [isEditModalOpen, setEditModalOpen] = useState(false);
  const [isSortModalOpen, setSortModalOpen] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [tableKey, setTableKey] = useState(0); // For forcing table refresh

  // Keep current user in sync when changing pages (except auth)
  useEffect(() => {
    if (currentPage !== 'auth') {
      getCurrentUser()
        .then((response) => {
          setCurrentUser(response.data);
        })
        .catch(() => {
          setCurrentUser(null);
        });
    } else {
      setCurrentUser(null);  // Make sure user is null on auth page
    }
  }, [currentPage]);


  // Switch current page and persist it in localStorage
  const handleNavigation = (page) => {
    setCurrentPage(page);
    localStorage.setItem('currentPage', page);
    if (page === 'auth') {
      setCurrentUser(null);  // Clear user on logout
    }
  };

  // Open edit modal for selected user
  const openEditModal = (userId) => {
    setSelectedUserId(userId);
    setEditModalOpen(true);
  };
  
  const closeEditModal = () => {
    setEditModalOpen(false);
    setSelectedUserId(null);
  };
  
  const openSortModal = () => setSortModalOpen(true);
  const closeSortModal = () => setSortModalOpen(false);

  // Force table refresh by changing the key
  const handleUserUpdated = () => {
    setTableKey(prev => prev + 1);
  };

  // Navigate to server-side sort (placeholder)
  const sortTableBy = (column) => {
    console.log('Sort by:', column);
    window.location.href = `/sortUsersBy/${column}`;
  };

  const renderCurrentPage = () => {
    switch (currentPage) {
      case 'auth':
        return <AuthPage onNavigateTo={handleNavigation} />;
      case 'dashboard':
        return <HomeDashboard />;
      case 'users':
        return (
          <UsersTable
            key={tableKey}
            openEditModal={openEditModal}
            openSortModal={openSortModal}
          />
        );
      case 'income':
        return <IncomePage />;
      default:
        return <HomeDashboard />;
    }
  };

  return (
    <div className="w3-light-grey">
      {/* Top Bar extracted to Header */}
      <Header
        currentUser={currentUser}
        onMenuClick={() => document.getElementById('mySidebar').style.display = 'block'}
        onLoginClick={() => handleNavigation('auth')}
        onLogoutClick={async () => {
          try {
            await logoutUser();
            handleNavigation('auth');
          } catch (error) {
            console.error('Logout failed:', error);
          }
        }}
      />

      {/* Sidebar */}
      <Sidebar 
        currentUser={currentUser}
        onNavigate={handleNavigation} 
        currentPage={currentPage} />

      {/* Main Content */}
      <div className="w3-main" style={{ marginLeft: '300px', marginTop: '56px', minHeight: '100vh' }}>
        {renderCurrentPage()}

        <UserEditModal
          isOpen={isEditModalOpen}
          onClose={closeEditModal}
          userId={selectedUserId}
          onUserUpdated={handleUserUpdated}
        />

        <UsersSortModal isOpen={isSortModalOpen} onClose={closeSortModal} sortTableBy={sortTableBy} />
      </div>
    </div>
  );
}

export default App;
