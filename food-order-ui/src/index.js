import React from "react";
import ReactDOM from "react-dom/client";

// Bootstrap 5 CSS + JS bundle (includes Popper for dropdowns/tooltips)
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";

// Global app styles (loaded after Bootstrap so overrides apply)
import "./index.css";
import "./styles/app.css";

import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
