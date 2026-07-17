import React from "react";

/**
 * Top navigation bar. Shows the product name and a live "auto-refreshing"
 * indicator so the user always knows the table is polling in the background.
 */
const Navbar = ({ isAutoRefreshing = true }) => {
  return (
    <nav className="app-navbar">
      <div className="container d-flex align-items-center justify-content-between flex-wrap gap-2">
        <span className="brand-mark">
          <span className="brand-emoji" aria-hidden="true">
            🍽️
          </span>
          Order Control
          <span className="brand-tagline">Food Ordering Dashboard</span>
        </span>

        {isAutoRefreshing && (
          <span className="live-indicator">
            <span className="live-dot" aria-hidden="true"></span>
            Live · refreshing every 2s
          </span>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
