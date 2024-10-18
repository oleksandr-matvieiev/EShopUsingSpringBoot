import React, {useState} from 'react';
import axios from 'axios';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState(null);
    const [resetMessage, setResetMessage] = useState(null);
    const [showResetForm, setShowResetForm] = useState(false);  // Для показу/приховування форми скидання паролю

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                username,
                password
            });
            console.log("Login successful. Token: ", response.data.Token);
            if (response.data.Token) {
                // Зберігаємо токен і ролі
                localStorage.setItem("roles", response.data.roles);
                localStorage.setItem("token", response.data.Token);
                setError(null);
            } else {
                setError("Invalid response from server. No token provided.");
            }
        } catch (err) {
            console.error('Login failed: ', err);
            setError("Login failed. Please check your credentials.");
        }
    };

    const handlePasswordResetRequest = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/auth/reset-password-request',{
                email: email
        });
            console.log("Password reset request send ");
        } catch (err) {
            console.error('Sending of password reset request failed : ', err);
            setError("Sending of password reset request failed. Please try again");
        }
    };

    return (
        <div>
            <h2>Login</h2>
            {error && <p style={{color: 'red'}}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                />
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                />
                <button type="submit">Login</button>
            </form>

            <button onClick={() => setShowResetForm(!showResetForm)}>
                {showResetForm ? "Cancel Reset" : "Forgot Password?"}
            </button>

            {showResetForm && (
                <form onSubmit={handlePasswordResetRequest}>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="Enter your email"
                    />
                    <button type="submit">Send Reset Link</button>
                </form>
            )}
            {resetMessage && <p>{resetMessage}</p>}
        </div>
    );
};

export default Login;
