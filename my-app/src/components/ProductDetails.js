import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

const ProductDetails = () => {
    const { id } = useParams(); // Отримуємо ID продукту з URL
    const [product, setProduct] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/product/${id}`);
                setProduct(response.data);
            } catch (err) {
                console.error("Error fetching product details", err);
                setError("Failed to load product details.");
            }
        };
        fetchProduct();
    }, [id]);

    if (error) {
        return <p style={{color: 'red'}}>{error}</p>;
    }

    if (!product) {
        return <p>Loading...</p>;
    }

    return (
        <div className="product-details">
            <img src={`http://localhost:8080${product.imageUrl}`} alt={product.name} className="product-image-large" />
            <h1>{product.name}</h1>
            <p>Price: ${product.price}</p>
            <p>Quantity: {product.quantity}</p>
            <p>Description: {product.description}</p>
            <p>Category: {product.category ? product.category.name : 'N/A'}</p>
        </div>
    );
};

export default ProductDetails;
