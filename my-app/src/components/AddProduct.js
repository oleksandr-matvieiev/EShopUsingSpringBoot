import React, { useState } from 'react';
import axios from 'axios';

const AddProducts = () => {
    const [productName, setProductName] = useState('');
    const [price, setPrice] = useState(0);
    const [quantity, setQuantity] = useState(0);
    const [categoryName, setCategoryName] = useState('');
    const [description, setDescription] = useState('');
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState('');

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('productName', productName);
        formData.append('price', price);
        formData.append('quantity', quantity);
        formData.append('categoryName', categoryName);
        formData.append('description', description);
        if (file) {
            formData.append('file', file);
        }

        try {
            const token = localStorage.getItem("token");
            await axios.post('http://localhost:8080/api/product/save', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`,
                },
            });
            setMessage('Product saved successfully.');
            setProductName('');
            setPrice(0);
            setQuantity(0);
            setCategoryName('');
            setDescription('');
            setFile(null);
        } catch (error) {
            setMessage('Error while saving product. Please check the data or try again.');
            console.error(error);
        }
    };

    return (
        <div>
            <h2>Add Product</h2>
            {message && <p>{message}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Name of product:</label>
                    <input
                        type="text"
                        value={productName}
                        onChange={(e) => setProductName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Price:</label>
                    <input
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Quantity:</label>
                    <input
                        type="number"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Category:</label>
                    <input
                        type="text"
                        value={categoryName}
                        onChange={(e) => setCategoryName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Upload image:</label>
                    <input type="file" onChange={handleFileChange} />
                </div>
                <button type="submit">Add Product</button>
            </form>
        </div>
    );
};

export default AddProducts;
