import axios from "axios";

const ORDER_API_BASE_URL = "http://localhost:8082";

export const placeOrder = async (productId, quantity) => {
  const response = await axios.post(
    `${ORDER_API_BASE_URL}/orders`,
    {
      productId,
      quantity
    },
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );

  return response.data;
};
