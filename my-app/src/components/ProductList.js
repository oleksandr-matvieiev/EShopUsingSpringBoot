import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductList.css';

const ProductList = () => {
    const [products, setProducts] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/product/get');
                setProducts(response.data);
            } catch (err) {
                console.error("Error fetching products", err);
                setError("Failed to fetch products.");
            }
        };
        fetchProducts();
    }, []);

    return (
        <div className="product-container">
            <h1>Products</h1>
            {error && <p style={{color: 'red'}}>{error}</p>}
            <div className="product-grid">
                {products.map(product => (
                    <div className="product-card" key={product.id}>
                        <img src={`http://localhost:8080${product.imageUrl}`} alt={product.name}
                             className="product-image"/>

                        <h2>{product.name}</h2>
                        <p>Price: ${product.price}</p>
                        <p>Quantity: {product.quantity}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductList;
