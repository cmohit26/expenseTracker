import React from 'react';
import styles from './Header.module.css';

// Header: Top navigation bar containing app title, mobile menu, and auth actions.
// Props:
// - currentUser: object | null - currently logged-in user (or null)
// - onMenuClick: () => void - toggles the sidebar on small screens
// - onLoginClick: () => void - navigates to login/auth page
// - onLogoutClick: () => Promise<void> | void - triggers logout flow
function Header({ currentUser, onMenuClick, onLoginClick, onLogoutClick }){
  return (
    <div className={styles.topBar}>
      <button
        className={`${styles.barItem} ${styles.menuButton}`}
        onClick={onMenuClick}
        aria-label="Open navigation menu"
      >
        <i className="fas fa-bars"></i>
        <span className={styles.menuLabel}>Menu</span>
      </button>

      <div className={`${styles.barItem} ${styles.titleLeft}`}>
        <span className={styles.appTitle}>Expense Tracker</span>
      </div>

      <div className={`${styles.barItem} ${styles.actionsRight}`}>
        {currentUser ? (
          <>
            <span className={styles.welcomeText}>
              {`Welcome, ${currentUser.firstName || currentUser.email || 'User'}`}
            </span>
            <button
              className={`${styles.actionButton} ${styles.logout}`}
              onClick={onLogoutClick}
            >
              Logout
            </button>
          </>
        ) : (
          <button
            className={`${styles.actionButton} ${styles.login}`}
            onClick={onLoginClick}
          >
            Login
          </button>
        )}
      </div>
    </div>
  );
}

export default Header;