// src/components/Register.js
import React, {useState} from 'react';
import axios from 'axios';

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState(null)

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const respone = await axios.post('http://localhost:8080/api/auth/register',
                {
                    username,
                    password,
                    email
                });
            setMessage(respone.data)
        } catch (err) {
            console.error("Registration failed: ", err)
            setMessage("Registration failed. Try again")
        }

    };

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                />
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                />
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                />
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;
