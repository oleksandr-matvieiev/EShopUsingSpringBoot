import React, { useEffect, useState } from 'react';
import axios from 'axios';

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [search, setSearch] = useState('');
    const [role, setRole] = useState('USER');
    const [sortField, setSortField] = useState('username');
    const [sortOrder, setSortOrder] = useState('asc');
    const [page, setPage] = useState(0);
    const [selectedRole, setSelectedRole] = useState('USER');
    const [selectedUser, setSelectedUser] = useState('');

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const token = localStorage.getItem('token'); // Отримуємо токен із локального сховища
                const response = await axios.get('http://localhost:8080/api/admin/userList', {
                    headers: {
                        Authorization: `Bearer ${token}` // Додаємо токен у заголовок запиту
                    },
                    params: {
                        search: search,
                        role: role,
                        page: page,
                        sort: `${sortField},${sortOrder}`
                    }
                });
                setUsers(response.data.content);
            } catch (err) {
                console.error("Error fetching users", err);
            }
        };
        fetchUsers();
    }, [search, role, sortField, sortOrder, page]);

    // Функція для надання нової ролі користувачу
    const assignRole = async (username) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post('http://localhost:8080/api/admin/assign-role', null, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                params: {
                    username: username,
                    role: selectedRole,
                },
            });
            console.log(`Role ${selectedRole} assigned to ${username}`);
            alert(response.data); // Виведення повідомлення про успішне призначення
        } catch (err) {
            console.error("Error assigning role", err);
        }
    };

    return (
        <div>
            <h1>Users List</h1>
            <div>
                <input
                    type="text"
                    placeholder="Search by username"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                />
                <select value={role} onChange={(e) => setRole(e.target.value)}>
                    <option value="USER">User</option>
                    <option value="ADMIN">Admin</option>
                    <option value="SUPER_ADMIN">Super Admin</option>
                </select>
                <button onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}>
                    Sort {sortOrder === 'asc' ? 'Ascending' : 'Descending'}
                </button>
            </div>

            <table>
                <thead>
                <tr>
                    <th onClick={() => setSortField('username')}>Username</th>
                    <th onClick={() => setSortField('email')}>Email</th>
                    <th>Role</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.username}</td>
                        <td>{user.email}</td>
                        <td>{user.roles.map(role => role.roleName).join(', ')}</td>
                        <td>
                            {/* Вибір ролі для призначення */}
                            <select value={selectedRole} onChange={(e) => setSelectedRole(e.target.value)}>
                                <option value="USER">User</option>
                                <option value="ADMIN">Admin</option>
                                <option value="SUPER_ADMIN">Super Admin</option>
                            </select>
                            <button onClick={() => assignRole(user.username)}>
                                Assign Role
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => setPage(prev => Math.max(prev - 1, 0))}>Previous</button>
            <button onClick={() => setPage(prev => prev + 1)}>Next</button>
        </div>
    );
};

export default UserList;
