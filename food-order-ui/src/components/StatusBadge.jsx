import React from "react";

// Maps each backend order status to a CSS class and a human-readable label.
const STATUS_CONFIG = {
  PLACED: { className: "status-placed", label: "Placed" },
  PAYMENT: { className: "status-payment", label: "Payment" },
  KITCHEN_PREPARATION: {
    className: "status-kitchen_preparation",
    label: "Kitchen Preparation",
  },
  DELIVERY: { className: "status-delivery", label: "Delivery" },
  DELIVERED: { className: "status-delivered", label: "Delivered" },
  FAILED: { className: "status-failed", label: "Failed" },
};

/**
 * Renders a colored pill badge for a given order status.
 * Falls back to a neutral "unknown" style for any status the backend
 * sends that isn't in STATUS_CONFIG, so the UI never breaks on new states.
 */
const StatusBadge = ({ status }) => {
  const config = STATUS_CONFIG[status] || {
    className: "status-unknown",
    label: status || "Unknown",
  };

  return (
    <span className={`status-badge ${config.className}`}>
      <span className="status-dot" aria-hidden="true"></span>
      {config.label}
    </span>
  );
};

export default StatusBadge;
