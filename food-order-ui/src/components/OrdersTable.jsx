import React from "react";
import StatusBadge from "./StatusBadge";
import LoadingSpinner from "./LoadingSpinner";

const formatCurrency = (amount) => {
  const value = Number(amount);
  if (Number.isNaN(value)) return amount;
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 2,
  }).format(value);
};

const formatDateTime = (value) => {
  if (!value) return "—";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return new Intl.DateTimeFormat("en-IN", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date);
};

/**
 * Renders the live orders table.
 * Shows a spinner on the very first load, and an empty state once loading
 * has finished but no orders exist yet.
 */
const OrdersTable = ({ orders, isLoading }) => {
  if (isLoading) {
    return <LoadingSpinner message="Fetching latest orders..." />;
  }

  if (!orders || orders.length === 0) {
    return (
      <div className="empty-state">
        <span className="empty-emoji" aria-hidden="true">
          🧾
        </span>
        No orders yet. Place your first order to see it appear here.
      </div>
    );
  }

  return (
    <div className="table-responsive">
      <table className="table orders-table mb-0">
        <thead>
          <tr>
            <th scope="col">Order ID</th>
            <th scope="col">Customer Name</th>
            <th scope="col">Food Item</th>
            <th scope="col">Amount</th>
            <th scope="col">Status</th>
            <th scope="col">Created Time</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td className="order-id-pill">#{order.id}</td>
              <td>{order.customerName}</td>
              <td>{order.foodItem}</td>
              <td>{formatCurrency(order.amount)}</td>
              <td>
                <StatusBadge status={order.status} />
              </td>
              <td>{formatDateTime(order.createdAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default OrdersTable;
