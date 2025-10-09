import React from 'react';
import styles from './Sidebar.module.css';

// Sidebar: App navigation for dashboard and admin pages. Collapsible on mobile.

const Sidebar = ({currentUser, onNavigate, currentPage}) => {
  // Toggle sidebar visibility on small screens
  const w3_open = () => {
    const mySidebar = document.getElementById("mySidebar");
    const overlayBg = document.getElementById("myOverlay");
    if (mySidebar.style.display === 'block') {
      mySidebar.style.display = 'none';
      overlayBg.style.display = "none";
    } else {
      mySidebar.style.display = 'block';
      overlayBg.style.display = "block";
    }
  };

  const w3_close = () => {
    const mySidebar = document.getElementById("mySidebar");
    const overlayBg = document.getElementById("myOverlay");
    mySidebar.style.display = "none";
    overlayBg.style.display = "none";
  };

  // Navigate and close sidebar (mobile UX)
  const handleNavigation = (page) => {
    onNavigate(page);
    w3_close(); // Close sidebar on mobile after navigation
  };

  return (
    <>
      <div id="myOverlay" className="w3-overlay w3-hide-large w3-animate-opacity" onClick={w3_close} style={{ cursor: 'pointer' }}></div>
      <nav className={`w3-sidebar w3-collapse w3-white w3-animate-left ${styles.sidebar}`} id="mySidebar">
        <br />
        <div className="w3-container w3-row">
          <div className="w3-col s4">
            <img src="/w3images/avatar2.png" className="w3-circle w3-margin-right" style={{ width: '46px' }} alt="Avatar" />
          </div>
          <div className="w3-col s8 w3-bar">
            <span> Welcome, 
              <strong>
                {currentUser ? currentUser.firstName || currentUser.email || 'User' : 'Guest'}
              </strong>
            </span>
            <a href="#" className="w3-bar-item w3-button"><i className="fa fa-envelope"></i></a>
            <a href="#" className="w3-bar-item w3-button"><i className="fa fa-user"></i></a>
            <a href="#" className="w3-bar-item w3-button"><i className="fa fa-cog"></i></a>
          </div>
        </div>
        <hr />
        <div className="w3-container">
          <h5>Dashboard</h5>
        </div>
        <div className="w3-bar-block">
          <a href="#" className="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black" onClick={w3_close} title="close menu">
            <i className="fa fa-remove fa-fw"></i> Close Menu
          </a>
          <a 
            href="#" 
            className={`w3-bar-item w3-button w3-padding ${currentPage === 'dashboard' ? 'w3-blue' : ''}`}
            onClick={() => handleNavigation('dashboard')}
          >
            <i className="fa fa-home fa-fw"></i> Index
          </a>
          <a 
            href="#" 
            className={`w3-bar-item w3-button w3-padding ${currentPage === 'users' ? 'w3-blue' : ''}`}
            onClick={() => handleNavigation('users')}
          >
            <i className="fa fa-lock"></i> ADMIN Information
          </a>
          <a 
            href="#" 
            className={`w3-bar-item w3-button w3-padding ${currentPage === 'income' ? 'w3-blue' : ''}`}
            onClick={() => handleNavigation('income')}
          >
            <i className="fa fa-money"></i> Income
          </a>
        </div>
      </nav>
    </>
  );
};

export default Sidebar;