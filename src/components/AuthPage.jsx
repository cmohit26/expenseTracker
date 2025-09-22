import React, { useEffect, useLayoutEffect, useRef, useState } from 'react';
import styles from './AuthPage.module.css';
import { addUser } from '../services/UserServices';
import axios from 'axios';

// AuthPage: Handles Login and Registration flows with simple tabbed UI.
// Props:
// - onNavigateTo: (page: string) => void - navigate to another app section after auth
function AuthPage({ onNavigateTo }) {
  const [isLogin, setIsLogin] = useState(true);
  const [loginForm, setLoginForm] = useState({ email: '', password: '' });
  const [registerForm, setRegisterForm] = useState({
    firstName: '',
    lastName: '',
    age: '',
    email: '',
    password: '',
    confirmPassword: '',
    roles: 'ROLE_USER'
  });
  const [error, setError] = useState(null);
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

    setError('');
    const loginData = new URLSearchParams();
    loginData.append('username', loginForm.email);
    loginData.append('password', loginForm.password);

    try {
        const response = await axios.post('http://localhost:8082/login', loginData, {
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
        setError(error.response?.data?.error || 'Login failed. Please try again.');
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    if (registerForm.password !== registerForm.confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    if (!registerForm.firstName || !registerForm.lastName || !registerForm.age || !registerForm.email || !registerForm.password || !registerForm.roles) {
      setError('Please fill all fields');
      return;
    }
    try {
      await addUser({
        firstName: registerForm.firstName,
        lastName: registerForm.lastName,
        age: registerForm.age,
        email: registerForm.email,
        password: registerForm.password,
        roles: registerForm.roles
      });
      alert('Registration successful! Please log in.');
      setIsLogin(true);
      setRegisterForm({
        firstName: '',
        lastName: '',
        age: '',
        email: '',
        password: '',
        confirmPassword: '',
        roles: 'ROLE_USER'
      });
      setError(null);
    } catch (error) {
      console.error('Registration error:', error);
      setError(error);
    }
  };

  return (
    <div className={styles.authWrapper}>
      <div className={styles.centerContainer}>
        <h2 className={styles.title}>Welcome to expense tracker</h2>
        {error && <div className={styles.error}>{error}</div>}
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
        <div className={styles.card}>
          <div className={styles.cardInner}>
            <div ref={sizerRef} className={styles.contentSizer}>
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
                    <button type="submit" className={styles.submitButton}>Submit</button>
                    <button type="button" className={styles.switchButton} onClick={() => setIsLogin(false)}>Go to registration</button>
                  </div>
                </form>
              </div>
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
                    <div className={styles.label}>Age</div>
                    <input className={styles.input} type="number" name="age" value={registerForm.age} onChange={handleRegisterChange} required min="1" />
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
                      <option value="ROLE_USER">User</option>
                      <option value="ROLE_ADMIN">Admin</option>
                    </select>
                  </div>
                  <div className={styles.actions}>
                    <button type="submit" className={styles.submitButton}>Create account</button>
                    <button type="button" className={styles.switchButton} onClick={() => setIsLogin(true)}>Back to login</button>
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


