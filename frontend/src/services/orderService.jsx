import axios from "axios";

export const placeOrder = async (productId, quantity) => {
  const response = await axios.post(
    "/api/orders",
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
