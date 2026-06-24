import React, { useEffect, useState } from "react";
import styles from "./DashboardPage.module.css";
import { getDashboardData } from "../services/DashboardService";

function DashboardPage({ onNavigate }) {

  const [dashboard, setDashboard] = useState(null);
  
  // //For Dynamic Monthly Expense Chart
  // const monthlyExpenseData = dashboard?.monthlyExpenses || [];

  // For Dynamic Category Expense Bars
  const categoryExpenseData = [
    { name: "Food", percentage: 79, color: "greenBar" },
    { name: "Clothes", percentage: 31, color: "orangeBar" },
    { name: "Travel", percentage: 20, color: "redBar" },
    { name: "Stationary", percentage: 8, color: "blueBar" },
  ];
  
  
  // Navigate and close sidebar (mobile UX)
  const handleNavigation = (page) => {
    if (onNavigate) onNavigate(page);
  };
  
  useEffect(() => {
    fetchDashboard();
  }, []);
  
  // const [isLoggedIn, setIsLoggedIn] = useState(true);
  
  const fetchDashboard = () => {
    getDashboardData()
    .then((res) => {
      setDashboard(res.data);
    })
    .catch((err) => {
      console.error(err);
      setDashboard(null);
    });
  };
  
  // COODE CHECK
  // Get all available years from API
  const availableYears = React.useMemo(() => {
    return dashboard?.yearlyExpenses?.map(item => item.year) || [];
  }, [dashboard]);
  
  // Selected year
  const [selectedYear, setSelectedYear] = useState(null);
  
  // Default to newest year when data loads
  useEffect(() => {
    if (availableYears.length && selectedYear === null) {
      setSelectedYear(Math.max(...availableYears));
    }
  }, [availableYears, selectedYear]);
  
  // Get months for currently selected year
  const monthlyExpenseData =
  dashboard?.yearlyExpenses?.find(
    item => item.year === selectedYear
  )?.months || [];
  
  const hasAnyExpense = monthlyExpenseData.some(
    d => d.amount > 0
  );
  
  // Build chart data
  ////FOR All MONTH DISPLAYING
  // const chartData = React.useMemo(() => {
    //   const ALL_MONTHS = [
      //     "Jan", "Feb", "Mar", "Apr",
      //     "May", "Jun", "Jul", "Aug",
      //     "Sep", "Oct", "Nov", "Dec"
      //   ];
      
      //   const monthMap = {};
      
      //   monthlyExpenseData.forEach(item => {
                //     monthMap[item.month.toUpperCase()] = item.amount;
                //   });
                
                //   return ALL_MONTHS.map(month => ({
                //     month,
                //     amount: monthMap[month.toUpperCase()] || 0
                //   }));
                // }, [monthlyExpenseData]);
                
                // Build chart data
                
                //FOR Show only active months + 1 month padding on both sides
                const chartData = React.useMemo(() => {
                  const ALL_MONTHS = [
                    "Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug",
                    "Sep", "Oct", "Nov", "Dec"
                  ];
                  
    const monthMap = {};
    
    monthlyExpenseData.forEach(item => {
      monthMap[item.month.toUpperCase()] = item.amount;
    });
    
    // Build all 12 months first
    const completeData = ALL_MONTHS.map(month => ({
      month,
      amount: monthMap[month.toUpperCase()] || 0
    }));
    
    // Find months that actually have expenses
    const activeMonths = completeData.filter(
      m => m.amount > 0
    );
    
    // If no expenses, show all months
    if (!activeMonths.length) {
      return completeData;
    }

    // First month containing expense
    const firstIndex = completeData.findIndex(
      m => m.amount > 0
    );
    
    // Last month containing expense
    const lastIndex =
    completeData.length -
    1 -
    [...completeData]
    .reverse()
    .findIndex(m => m.amount > 0);
    
    // CHANGED: Add one month before and after
    const start = Math.max(0, firstIndex - 1);
    const end = Math.min(
      completeData.length - 1,
      lastIndex + 1
    );
    
    return completeData.slice(start, end + 1);
    
  }, [monthlyExpenseData]);
  
  //console.log("monthlyExpenseData:", monthlyExpenseData);
  console.log("--------------------------");
  console.log(dashboard?.yearlyExpenses);
  
  return (
    <div className={styles.container}>

      {/* Page Title */}
      <div className={styles.pageHeader}>
        <h2>User Dashboard</h2>
      </div>

      {/* Summary Cards (ROW 1) */}
      <div className={styles.summaryCards}>
        <div className={`${styles.card} ${styles.greenCard}`}>
          <span>Total Income</span>
           <h3>{dashboard?.totalIncome ?? "---"}</h3>
        </div>
        <div className={`${styles.card} ${styles.greenCard}`}>
          <span>Categories</span>
          <h3>{dashboard?.totalCategories ?? "---"}</h3>
        </div>
      </div>

      {/* Summary Cards (ROW 2) */}
      <div className={styles.summaryCards}>
        <div className={`${styles.card} ${styles.redCard}`}>
          <span>Total Expenses</span>
          <h3>{dashboard?.totalExpenses ?? "---"}</h3>
        </div>
        <div className={`${styles.card} ${styles.blueCard}`}>
          <span>Current Balance</span>
          <h3>{dashboard?.currentBalance ?? "---"}</h3>
        </div>
      </div>

      {/* QUICK ACTIONS TAB */}
      <div className={styles.expenseCategories}>
          <h3>Quick Actions</h3>
          <div className={styles.buttonRow}>
            <button
              className={styles.actionButton}
              onClick={() => handleNavigation('income')}
              >
              <h4>Add Income</h4>
            </button>
            <button
              className={styles.actionButton}
              onClick={() => handleNavigation("expense")}
              >
              <h4>Add Expense</h4>
            </button>
          </div>
      </div>
      
      <div className={styles.expenseCategories}>
        <h3>Monthly Expenses</h3>

        {dashboard ? (
          <div className={styles.chartLayout}>
            <div className={styles.yearSidebar}>
              {availableYears.map((year) => (
                <button
                key={year}
                onClick={() => setSelectedYear(year)}
                className={
                  selectedYear === year
                  ? styles.activeYear
                  : styles.yearItem
                  }
                  >
                  {year}
                </button>
              ))}
            </div>

            <div className={styles.chartArea}>
              <ResponsiveContainer width="100%" height={350}>
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis/>
                  <Tooltip
                    formatter={(value) => [
                      value ? `₹${value.toLocaleString()}` : "₹0",
                      "Expense",
                    ]}
                  />
                  <Line
                    type="monotone"
                    dataKey="amount"
                    stroke="#f136f4"
                    strokeWidth={3}
                    dot={{ r: 5 }}
                    activeDot={{ r: 8 }}
                    />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>
        ) : (
          <div className={styles.emptyState}>
            <i className="fa-solid fa-lock"></i>
            <h4>Login Required</h4>
            <p>Login to view your monthly expense chart.</p>
          </div>
        )}
      </div>

      {/* Charts Row */}
      <div className={styles.visualRow}>
          <div className={styles.expenseCategories}>
          <h3>Expense Categories</h3>
          {dashboard ? (
            <div className={styles.categoryBars}>
              {categoryExpenseData.map((category) => (
                <div key={category.name} className={styles.categoryBar}>
                  <span className={styles.categoryName}>
                    {category.name}
                  </span>

                  <div className={styles.barContainer}>
                    <div
                      className={`${styles.bar} ${styles[category.color]}`}
                      style={{ width: `${category.percentage}%` }}
                    />

                    <span className={styles.percentage}>
                      {category.percentage}%
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className={styles.emptyState}>
              <i className="fa-solid fa-lock"></i>
              <h4>Login Required</h4>
              <p>Login to view your expense categories breakdown.</p>
            </div>
          )}
        </div>

          <div className={styles.card}>
            <h3>TOP 5 Recent Expenses</h3>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Title</th>
                  <th>Amount</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {dashboard?.recentTransactions?.map((tx, index) => (
                  <tr key={index}>
                    <td>{index + 1}</td>
                    <td>{tx.title}</td>
                    <td>₹ {tx.amount}</td>
                    <td>{tx.date}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
      </div>
      
      


      {/* FUTURE FEATURES */}
      <div className={styles.expenseCategories}>
        <h3><b>Future Features</b></h3>

        <h4><i className="fa-solid fa-bug"></i> Fix the bugs <br/></h4>
        <h5>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </h5>

        <h4><i className="fa-solid fa-search"></i> Search and Filter</h4>
        <h5>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          -- category=Food || from=2026-06-01 || expenses?to=2026-06-30 || minAmount=1000<br/><br/>
        </h5>
        
        <h4><i className="fa-solid fa-sync-alt"></i> Recurring Income / Expenses</h4>
        <h5> 
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          -- Salary : Monthly || Netflix : Yearly <br/><br/>
        </h5>
        
        <h4><i className="fa-solid fa-file-export"></i> Export to CSV<br/><br/></h4>
        <h5> </h5>
        
        <h4><i className="fa-solid fa-brain"></i> Personal Finance Insights (Maybe USE AI?) </h4>
        <h5> 
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          -- Generate messages :: You spent 20% more on Food this month || Transport expenses decreased by 12% || our savings rate improved from 18% to 24% 
        </h5>
      </div>

    </div>
  );
}

export default DashboardPage;
import {  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";