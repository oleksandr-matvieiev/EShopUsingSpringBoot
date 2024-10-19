import React from 'react';
import { BrowserRouter as Router, Link, Route, Routes } from 'react-router-dom';
import ProductList from './components/ProductList.js';
import Login from './components/Login.js';
import Register from './components/Register.js';
import AddProduct from './components/AddProduct.js';
import UserList from './components/UserListForAdmin.js';
import ActivityLog from './components/ActivityLog.js';
import ResetPassword from './components/ResetPassword.js';
import ProductDetails from './components/ProductDetails.js'; // Імпортуємо сторінку з інформацією про продукт

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
                            <Link to="/add-product">Add Product</Link>
                        </li>
                        {hasAccessToUserList() && (
                            <>
                                <li>
                                    <Link to="/admin/users">User List</Link>
                                </li>
                                <li>
                                    <Link to="/activity-log">Activity Logs</Link>
                                </li>
                            </>
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
                    <Route path="/reset-password" element={<ResetPassword />} />
                    <Route path="/product/:id" element={<ProductDetails />} /> {/* Додали маршрут для ProductDetails */}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
