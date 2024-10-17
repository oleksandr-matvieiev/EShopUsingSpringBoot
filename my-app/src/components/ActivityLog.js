import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ActivityLog.css'; // Імпортуємо CSS файл

const ActivityLog = () => {
    const [logs, setLogs] = useState([]);

    useEffect(() => {
        const fetchLogs = async () => {
            const token = localStorage.getItem("token");
            const response = await axios.get('http://localhost:8080/api/admin/activity-log', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setLogs(response.data);
        };

        fetchLogs();
    }, []);

    return (
        <div className="activity-log-container">
            <h2>Activity Log</h2>
            <table className="activity-log-table">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Action</th>
                    <th>IP Address</th>
                    <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                {logs.map(log => (
                    <tr key={log.id}>
                        <td>{log.username}</td>
                        <td>{log.action}</td>
                        <td>{log.ipAddress}</td>
                        <td>{log.timeStamp}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ActivityLog;
