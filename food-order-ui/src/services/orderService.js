import axios from "axios";

// Base URL for the order-service (Spring Boot, port 8081).
// Override at build/run time with REACT_APP_ORDER_SERVICE_URL if needed.
const BASE_URL =
  process.env.REACT_APP_ORDER_SERVICE_URL || "http://localhost:8081/api";

const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: 8000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Central error normalizer so components can show a friendly message
// regardless of whether the failure was a network error or an API error.
const normalizeError = (error) => {
  if (error.response) {
    // Server responded with a non-2xx status
    const message =
      error.response.data?.message ||
      error.response.data?.error ||
      `Request failed with status ${error.response.status}`;
    return new Error(message);
  }
  if (error.request) {
    // Request was made but no response received (server down / CORS / network)
    return new Error(
      "Unable to reach the order service. Please check that it is running on port 8081."
    );
  }
  return new Error(error.message || "Unexpected error occurred.");
};

/**
 * Fetch all orders.
 * GET /api/orders
 */
export const getAllOrders = async () => {
  try {
    const response = await apiClient.get("/orders");
    return response.data;
  } catch (error) {
    throw normalizeError(error);
  }
};

/**
 * Place a new order.
 * POST /api/orders
 * @param {{ customerName: string, item: string, amount: number }} orderPayload
 */
export const placeOrder = async (orderPayload) => {
  try {
    const response = await apiClient.post("/orders", orderPayload);
    return response.data;
  } catch (error) {
    throw normalizeError(error);
  }
};

const orderService = {
  getAllOrders,
  placeOrder,
};

export default orderService;
