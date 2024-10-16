import React from 'react';
import { BrowserRouter as Router, Link, Route, Routes } from 'react-router-dom';
import ProductList from './components/ProductList.js';
import Login from './components/Login.js';
import Register from './components/Register.js';
import AddProduct from './components/AddProduct.js';
import UserList from './components/UserListForAdmin.js';

function App() {

    const roles = localStorage.getItem("roles") ? localStorage.getItem("roles").split(",") : [];

    // func for checking role
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
                    </ul>
                </nav>

                <Routes>
                    <Route path="/" element={<ProductList />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/add-product" element={<AddProduct />} />
                    {/* url for UserList */}
                    <Route path="/admin/users" element={<UserList />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
