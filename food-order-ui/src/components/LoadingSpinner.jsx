import React from "react";

/**
 * Reusable loading spinner with an optional message.
 * `size` controls the Bootstrap spinner sizing ("sm" for inline use).
 */
const LoadingSpinner = ({ message = "Loading...", size }) => {
  const spinnerClass = size === "sm" ? "spinner-border spinner-border-sm" : "spinner-border";

  return (
    <div className="loading-wrap">
      <div className={spinnerClass} role="status" style={{ color: "var(--color-accent)" }}>
        <span className="visually-hidden">Loading...</span>
      </div>
      {message && <span>{message}</span>}
    </div>
  );
};

export default LoadingSpinner;
