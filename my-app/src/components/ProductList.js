import React, {useEffect, useState} from "react";
import axios from 'axios';

const ProductList = () => {
    const [products,setProducts]=useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/product/get')
            .then(response=>{
                console.log(response.data)
                setProducts(response.data);
            })
            .catch(error=>{
                console.error("Error fetching products",error)
            })
    }, []);

    return (
        <div>
            <h1>Products</h1>
            <ul>
                {products.map(product=>(
                    <li key={product.id}>
                        <h2>{product.name}</h2>
                        <p>Price: {product.price}</p>
                        <p>Quantity: {product.quantity}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProductList;
