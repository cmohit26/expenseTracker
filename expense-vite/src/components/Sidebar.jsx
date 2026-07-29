import React from "react";
import styles from "./Sidebar.module.css";

const Sidebar = ({ currentUser, onNavigate, currentPage }) => {
  const isAdmin = currentUser?.roles === "ROLE_ADMIN";

  const w3_close = () => {
    const mySidebar = document.getElementById("mySidebar");
    const overlayBg = document.getElementById("myOverlay");

    mySidebar.style.display = "none";
    overlayBg.style.display = "none";
  };

  const handleNavigation = (page) => {
    onNavigate(page);
    w3_close();
  };

  return (
    <>
      <div
        id="myOverlay"
        className="w3-overlay w3-hide-large w3-animate-opacity"
        onClick={w3_close}
        style={{ cursor: "pointer" }}
      ></div>

      <nav
        className={`w3-sidebar w3-collapse w3-white w3-animate-left ${styles.sidebar}`}
        id="mySidebar"
      >
        <br />

        {/* User Info */}
        <div className={styles.userHeader}>
          <i className={`fa-solid fa-circle-user ${styles.userIcon}`}></i>

          <span className={styles.panelBadge}>
            {isAdmin ? "ADMIN PANEL" : "USER PANEL"}
          </span>

          <p className={styles.welcomeText}>
            Welcome,
            <br />
            <strong>
              {currentUser
                ? currentUser.firstName || currentUser.email
                : "Guest"}
            </strong>
          </p>
        </div>

        <hr />

        <div className="w3-container">
          <h5>{isAdmin ? "Administration" : "Dashboard"}</h5>
        </div>


        <div className="w3-bar-block">

          {/* Mobile close */}
          <a
            href="#"
            className="w3-bar-item w3-button w3-padding-16 w3-hide-large w3-dark-grey w3-hover-black"
            onClick={w3_close}
          >
            <span className={styles.menuIcon}>
              <i className="fa fa-remove"></i>
            </span>
            <span>Close Menu</span>
          </a>


          {/* Dashboard */}
          <a
            href="#"
            className={`w3-bar-item w3-button w3-padding ${
              currentPage === "dashboard" ? "w3-blue" : ""
            }`}
            onClick={() => handleNavigation("dashboard")}
          >
            <span className={styles.menuIcon}>
              <i className="fa-solid fa-chart-line"></i>
            </span>

            <span>Dashboard</span>
          </a>


          {/* Income */}
          <a
            href="#"
            className={`w3-bar-item w3-button w3-padding ${
              currentPage === "income" ? "w3-blue" : ""
            }`}
            onClick={() => handleNavigation("income")}
          >
            <span className={styles.menuIcon}>
              <i className="fa-solid fa-dollar-sign"></i>
            </span>

            <span>Income</span>
          </a>


          {/* Expenses */}
          <a
            href="#"
            className={`w3-bar-item w3-button w3-padding ${
              currentPage === "expense" ? "w3-blue" : ""
            }`}
            onClick={() => handleNavigation("expense")}
          >
            <span className={styles.menuIcon}>
              <i className="fa fa-credit-card"></i>
            </span>

            <span>Expenses</span>
          </a>


          {/* Admin Section */}
          {isAdmin && (
            <div className={styles.adminSection}>

              <div className={styles.adminTitle}>
                Administrator
              </div>


              <a
                href="#"
                className={`w3-bar-item w3-button w3-padding ${
                  currentPage === "users" ? "w3-blue" : ""
                }`}
                onClick={() => handleNavigation("users")}
              >
                <span className={styles.menuIcon}>
                  <i className="fa fa-lock"></i>
                </span>

                <span>Admin Information</span>
              </a>


              <a
                href="#"
                className={`w3-bar-item w3-button w3-padding ${
                  currentPage === "admin" ? "w3-blue" : ""
                }`}
                onClick={() => handleNavigation("admin")}
              >
                <span className={styles.menuIcon}>
                  <i className="fa fa-check-circle"></i>
                </span>

                <span>Admin Approval</span>
              </a>

            </div>
          )}

        </div>
      </nav>
    </>
  );
};

export default Sidebar;
