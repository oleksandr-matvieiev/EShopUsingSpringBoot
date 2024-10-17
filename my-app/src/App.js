import React from 'react';
import { BrowserRouter as Router, Link, Route, Routes } from 'react-router-dom';
import ProductList from './components/ProductList.js';
import Login from './components/Login.js';
import Register from './components/Register.js';
import AddProduct from './components/AddProduct.js';
import UserList from './components/UserListForAdmin.js';
import ActivityLog from './components/ActivityLog.js';
import ResetPassword from './components/ResetPassword.js';

function App() {

    const roles = localStorage.getItem("roles") ? localStorage.getItem("roles").split(",") : [];

    const hasAccessToUserList = () => {
        return roles.includes("ROLE_ADMIN") || roles.includes("ROLE_SUPER_ADMIN");
    };

    return (
        <Router>
            <div>
                <nav>
                    <ul>
                        <li>
                            <Link to="/">Products</Link>
                        </li>
                        <li>
                            <Link to="/login">Login</Link>
                        </li>
                        <li>
                            <Link to="/register">Register</Link>
                        </li>
                        <li>
                            <Link to="/add-product">Add product</Link>
                        </li>
                        {hasAccessToUserList() && (
                            <li>
                                <Link to="/admin/users">User List</Link>
                            </li>
                        )}
                        {hasAccessToUserList() && (
                            <li>
                                <Link to="/activity-log">Activity logs</Link>
                            </li>
                        )}
                    </ul>
                </nav>

                <Routes>
                    <Route path="/" element={<ProductList />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/add-product" element={<AddProduct />} />
                    <Route path="/admin/users" element={<UserList />} />
                    <Route path="/activity-log" element={<ActivityLog />} />
                    <Route path="/reset-password" element={<ResetPassword />} />  {/* Новий маршрут */}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
