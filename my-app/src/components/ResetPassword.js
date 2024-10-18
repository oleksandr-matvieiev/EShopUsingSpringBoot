import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';

const ResetPassword = () => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const navigate = useNavigate();

    // Отримання токена з URL
    const search = useLocation().search;
    const token = new URLSearchParams(search).get('token');

    console.log("Token from URL:", token);

    useEffect(() => {
        if (!token) {
            setError("Invalid or missing token.");
        }
    }, [token]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (newPassword !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/auth/reset-password', {
                token: token,  // Передаємо токен і новий пароль в тілі запиту
                newPassword: newPassword
            });

            if (response.status === 200) {
                setSuccessMessage("Password successfully reset!");
                setTimeout(() => {
                    navigate("/login"); // Переходимо на сторінку логіну після успішного скидання
                }, 3000); // Затримка перед редіректом
            } else {
                setError("Failed to reset password.");
            }
        } catch (err) {
            console.error("Error resetting password:", err);
            setError("Failed to reset password. Please try again.");
        }
    };

    return (
        <div>
            <h2>Reset Password</h2>
            {error && <p style={{color: 'red'}}>{error}</p>}
            {successMessage && <p style={{color: 'green'}}>{successMessage}</p>}
            {!successMessage && (
                <form onSubmit={handleSubmit}>
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        placeholder="New Password"
                        required
                    />
                    <input
                        type="password"
                        value ={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Confirm Password"
                        required
                    />
                    <button type="submit">Reset Password</button>
                </form>
            )}
        </div>
    );
};

export default ResetPassword;
