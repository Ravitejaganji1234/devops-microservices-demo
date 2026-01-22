import { useState } from "react";
import { placeOrder } from "../services/orderService";

function OrderForm() {
  const [productId, setProductId] = useState("");
  const [quantity, setQuantity] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");

    try {
      const result = await placeOrder(productId, Number(quantity));
      setMessage(`Order placed successfully. Order ID: ${result.id}`);
    } catch (error) {
      setMessage("Order failed. Check stock or backend service.");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Place Order</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Product ID:</label>
          <input
            type="text"
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
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

        <button type="submit">Place Order</button>
      </form>

      <p>{message}</p>
    </div>
  );
}

export default OrderForm;
