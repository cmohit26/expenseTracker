import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import styles from './AuthPage.module.css';
import { addUser } from '../services/UserServices';
import { checkBackendHealth } from '../services/healthService';
import axios from 'axios';

function AuthPage({ onNavigateTo }) {
  const [isLogin, setIsLogin] = useState(true);
  const [loginForm, setLoginForm] = useState({ email: '', password: '' });
  const [registerForm, setRegisterForm] = useState({
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    email: '',
    password: '',
    confirmPassword: '',
    roles: 'ROLE_USER'
  });
  const [error, setError] = useState(null);
  const [backendStatus, setBackendStatus] = useState('checking');

  const sizerRef = useRef(null);
  const loginPanelRef = useRef(null);
  const registerPanelRef = useRef(null);

  useLayoutEffect(() => {
    if (!sizerRef.current) return;
    const active = isLogin ? loginPanelRef.current : registerPanelRef.current;
    if (active) {
      sizerRef.current.style.height = active.offsetHeight + 'px';
    }
  }, []);

  useEffect(() => {
    if (!sizerRef.current) return;
    const active = isLogin ? loginPanelRef.current : registerPanelRef.current;
    if (active) {
      sizerRef.current.style.height = active.offsetHeight + 'px';
    }
  }, [isLogin, loginForm, registerForm]);

  // ✅ Backend health check
  useEffect(() => {
    const checkStatus = async () => {
      const isOnline = await checkBackendHealth();
      setBackendStatus(isOnline ? 'online' : 'offline');
    };

    checkStatus();
    const interval = setInterval(checkStatus, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    setLoginForm((prev) => ({ ...prev, [name]: value }));
    setError(null);
  };

  const handleRegisterChange = (e) => {
    const { name, value } = e.target;
    setRegisterForm((prev) => ({ ...prev, [name]: value }));
    setError(null);
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    if (backendStatus !== 'online') {
      setError('Backend is offline. Please try later.');
      return;
    }

    setError('');
    const loginData = new URLSearchParams();
    loginData.append('username', loginForm.email);
    loginData.append('password', loginForm.password);

    try {
      const response = await axios.post('http://localhost:8080/login', loginData, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        withCredentials: true
      });

      if (response.status === 200) {
        onNavigateTo('table');
      } else {
        setError('Login failed');
      }
    } catch (error) {
      console.error('Login error:', error);

      if (!error.response) {
        setError('Backend is offline. Please try later.');
      } else {
        setError(error.response?.data?.error || 'Login failed. Please try again.');
      }
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();

    if (backendStatus !== 'online') {
      setError('Backend is offline. Please try later.');
      return;
    }

    if (registerForm.password !== registerForm.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (!registerForm.firstName || !registerForm.lastName || !registerForm.dateOfBirth || !registerForm.email || !registerForm.password || !registerForm.roles) {
      setError('Please fill all fields');
      return;
    }

    try {
      await addUser({
        firstName: registerForm.firstName,
        lastName: registerForm.lastName,
        dateOfBirth: registerForm.dateOfBirth,
        email: registerForm.email,
        password: registerForm.password,
        roles: registerForm.roles
      });

      alert('Registration successful! Please log in.');
      setIsLogin(true);

      setRegisterForm({
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        email: '',
        password: '',
        confirmPassword: '',
        roles: 'ROLE_USER'
      });

      setError(null);
    } catch (error) {
      console.error('Registration error:', error);
      setError('Registration failed');
    }
  };

  return (
    <div className={styles.authWrapper}>
      <div className={styles.centerContainer}>
        <h2 className={styles.title}>Welcome to expense tracker</h2>

        {error && <div className={styles.error}>{error}</div>}

        {/* ✅ Tabs + Backend Status in SAME ROW */}
        <div className={styles.topRow}>
          <div className={styles.tabs} role="tablist" aria-label="Auth tabs">
            <button
              className={`${styles.tab} ${isLogin ? styles.tabActive : ''}`}
              role="tab"
              aria-selected={isLogin}
              onClick={() => setIsLogin(true)}
            >
              Login
            </button>
            <button
              className={`${styles.tab} ${!isLogin ? styles.tabActive : ''}`}
              role="tab"
              aria-selected={!isLogin}
              onClick={() => setIsLogin(false)}
            >
              Register
            </button>
          </div>

          {/* ✅ Backend Status */}
          <div className={styles.backendStatus}>
            <span
              className={`${styles.statusDot} ${
                backendStatus === 'online'
                  ? styles.green
                  : backendStatus === 'offline'
                  ? styles.red
                  : styles.gray
              }`}
            ></span>
            <span>
              {backendStatus === 'online' && 'Backend Online'}
              {backendStatus === 'offline' && 'Backend Offline'}
              {backendStatus === 'checking' && 'Checking Backend status...'}
            </span>
          </div>
        </div>

        <div className={styles.card}>
          <div className={styles.cardInner}>
            <div ref={sizerRef} className={styles.contentSizer}>

              {/* LOGIN */}
              <div ref={loginPanelRef} className={`${styles.panel} ${isLogin ? styles.panelActive : ''}`} aria-hidden={!isLogin}>
                <h3 className={styles.sectionTitle}>Login</h3>

                <form onSubmit={handleLoginSubmit} className={styles.form}>
                  <div>
                    <div className={styles.label}>Email</div>
                    <input className={styles.input} type="email" name="email" value={loginForm.email} onChange={handleLoginChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Password</div>
                    <input className={styles.input} type="password" name="password" value={loginForm.password} onChange={handleLoginChange} required />
                  </div>

                  <div className={styles.actions}>
                    <button type="submit" className={styles.submitButton} disabled={backendStatus !== 'online'}>
                      Submit
                    </button>
                    <button type="button" className={styles.switchButton} onClick={() => setIsLogin(false)}>
                      Go to registration
                    </button>
                  </div>
                </form>
              </div>

              {/* REGISTER */}
              <div ref={registerPanelRef} className={`${styles.panel} ${!isLogin ? styles.panelActive : ''}`} aria-hidden={isLogin}>
                <h3 className={styles.sectionTitle}>Register</h3>

                <form onSubmit={handleRegisterSubmit} className={styles.form}>
                  <div>
                    <div className={styles.label}>First Name</div>
                    <input className={styles.input} type="text" name="firstName" value={registerForm.firstName} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Last Name</div>
                    <input className={styles.input} type="text" name="lastName" value={registerForm.lastName} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Date of Birth</div>
                    <input className={styles.input} type="date" name="dateOfBirth" value={registerForm.dateOfBirth} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Email</div>
                    <input className={styles.input} type="email" name="email" value={registerForm.email} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Password</div>
                    <input className={styles.input} type="password" name="password" value={registerForm.password} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Confirm Password</div>
                    <input className={styles.input} type="password" name="confirmPassword" value={registerForm.confirmPassword} onChange={handleRegisterChange} required />
                  </div>

                  <div>
                    <div className={styles.label}>Role</div>
                    <select className={styles.input} name="roles" value={registerForm.roles} onChange={handleRegisterChange} required>
                      <option value="Place_Holder">Select Role</option>
                      <option value="ROLE_USER">User</option>
                      <option value="ROLE_ADMIN">Admin</option>
                    </select>
                  </div>

                  <div className={styles.actions}>
                    <button type="submit" className={styles.submitButton} disabled={backendStatus !== 'online'}>
                      Create account
                    </button>
                    <button type="button" className={styles.switchButton} onClick={() => setIsLogin(true)}>
                      Back to login
                    </button>
                  </div>
                </form>
              </div>

            </div>
          </div>
        </div>

      </div>
    </div>
  );
}

export default AuthPage;