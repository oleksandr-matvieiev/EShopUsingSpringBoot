import React, {useState} from 'react';
import axios from 'axios';

const AddProducts = () => {
    // Правильне використання useState з квадратними дужками
    const [productName, setProductName] = useState('');
    const [price, setPrice] = useState(0);
    const [quantity, setQuantity] = useState(0);
    const [categoryName, setCategoryName] = useState('');
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
        if (file) {
            formData.append('file', file);
        }

        try {
            const token = localStorage.getItem("token")
            await axios.post('http://localhost:8080/api/product/save', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${token}`,
                },
            });
            setMessage('Product saved.');
        } catch (error) {
            setMessage('Error while saving product. Please, check data or try again.');
            console.error(error);
        }
    };

    return (
        <div>
            <h2>Add product</h2>
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
                    <label>Upload image:</label>
                    <input type="file" onChange={handleFileChange}/>
                </div>
                <button type="submit">Add product</button>
            </form>
        </div>
    );
};

export default AddProducts;
