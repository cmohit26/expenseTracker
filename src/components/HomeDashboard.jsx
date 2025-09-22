import React from 'react';
import styles from './HomeDashboard.module.css';

// HomeDashboard: Landing page with summary widgets and recent activity preview.
const HomeDashboard = () => {
  return (
    <div className={styles.dashboard}>
      {/* Page Title */}
      <div className={styles.pageHeader}>
        <h2></h2>
        <h2>Index Page (Preview of the WEBSITE)</h2>
        <h2></h2>
      </div>

      {/* Summary Cards */}
      <div className={styles.summaryCards}>
        <div className={`${styles.card} ${styles.redCard}`}>
          <div className={styles.cardHeader}>
            <span className={styles.cardLabel}>Month</span>
            <span className={styles.cardValue}>Dec</span>
          </div>
          <div className={styles.cardIcon}>
            <i className="fas fa-comment"></i>
          </div>
        </div>

        <div className={`${styles.card} ${styles.blueCard}`}>
          <div className={styles.cardHeader}>
            <span className={styles.cardLabel}>Transactions</span>
            <span className={styles.cardValue}>6083</span>
          </div>
          <div className={styles.cardIcon}>
            <i className="fas fa-eye"></i>
          </div>
        </div>

        <div className={`${styles.card} ${styles.greenCard}`}>
          <div className={styles.cardHeader}>
            <span className={styles.cardLabel}>Categories</span>
            <span className={styles.cardValue}>6</span>
          </div>
          <div className={styles.cardIcon}>
            <i className="fas fa-share"></i>
          </div>
        </div>
      </div>

      {/* Recent Transactions */}
      <div className={styles.recentTransactions}>
        <h3>Recent 10 Transaction Details</h3>
        <div className={styles.transactionList}>
          {[1, 2, 3].map((item) => (
            <div key={item} className={styles.transactionItem}>
              <div className={styles.transactionIcon}>
                <i className="fas fa-flag" style={{ color: '#2196F3' }}></i>
              </div>
              <div className={styles.transactionDetails}>
                <span className={styles.expenseDetail}>"Expense Detail"</span>
                <span className={styles.date}>"DATE"</span>
                <span className={styles.amount}>"$ AMOUNT"</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Expenses Categories Visual */}
      <div className={styles.expenseCategories}>
        <h3>Expenses Categories (Visual)</h3>
        <div className={styles.categoryBars}>
          <div className={styles.categoryBar}>
            <span className={styles.categoryName}>Food</span>
            <div className={styles.barContainer}>
              <div className={`${styles.bar} ${styles.greenBar}`} style={{ width: '79%' }}></div>
              <span className={styles.percentage}>79%</span>
            </div>
          </div>
          
          <div className={styles.categoryBar}>
            <span className={styles.categoryName}>Clothes</span>
            <div className={styles.barContainer}>
              <div className={`${styles.bar} ${styles.orangeBar}`} style={{ width: '31.3%' }}></div>
              <span className={styles.percentage}>31.3%</span>
            </div>
          </div>
          
          <div className={styles.categoryBar}>
            <span className={styles.categoryName}>Clothes</span>
            <div className={styles.barContainer}>
              <div className={`${styles.bar} ${styles.orangeBar}`} style={{ width: '23.34%' }}></div>
              <span className={styles.percentage}>23.34%</span>
            </div>
          </div>
          
          <div className={styles.categoryBar}>
            <span className={styles.categoryName}>Stationary</span>
            <div className={styles.barContainer}>
              <div className={`${styles.bar} ${styles.redBar}`} style={{ width: '3.43%' }}></div>
              <span className={styles.percentage}>3.43%</span>
            </div>
          </div>
        </div>
      </div>

      {/* Expenses Categories List */}
      <div className={styles.expenseCategoriesList}>
        <h3>Expenses Categories (Highest on TOP)</h3>
        <div className={styles.categoryList}>
          <div className={styles.categoryItem}>
            <span className={styles.categoryName}>Food (GET DATA)</span>
            <span className={styles.categoryPercentage}>79.00%</span>
          </div>
          <div className={styles.categoryItem}>
            <span className={styles.categoryName}>Travel</span>
            <span className={styles.categoryPercentage}>31.3%</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomeDashboard;


