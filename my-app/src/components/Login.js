// src/components/Login.js
import React, {useState} from 'react';
import axios from "axios";

const Login = () => {
        const [username, setUsername] = useState('');
        const [password, setPassword] = useState('');
        const [error, setEr] = useState(null);

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
                    setEr(null);
                } else {
                    setEr("Invalid response from server. No token provided.");
                }
            } catch (err) {
                console.error('Login failed: ', err);
                setEr("Login failed. Please check your credentials.");
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
            </div>
        );
    }
;

export default Login;
