import React, { useCallback, useEffect, useRef, useState } from "react";
import OrderForm from "../components/OrderForm";
import OrdersTable from "../components/OrdersTable";
import { getAllOrders, placeOrder } from "../services/orderService";

const AUTO_REFRESH_INTERVAL_MS = 2000;

const TERMINAL_FAILURE_STATUS = "FAILED";
const TERMINAL_SUCCESS_STATUS = "DELIVERED";

const Dashboard = () => {
  const [orders, setOrders] = useState([]);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [alert, setAlert] = useState(null); // { type: 'success' | 'danger', message: string }

  // Tracks whether a fetch is already in flight so overlapping polls
  // (e.g. a slow request plus the next 2s tick) don't stack up.
  const isFetchingRef = useRef(false);

  const showAlert = useCallback((type, message) => {
    setAlert({ type, message });
  }, []);

  useEffect(() => {
    if (!alert) return undefined;
    const timeoutId = setTimeout(() => setAlert(null), 4000);
    return () => clearTimeout(timeoutId);
  }, [alert]);

  const fetchOrders = useCallback(
    async ({ silent } = { silent: false }) => {
      if (isFetchingRef.current) return;
      isFetchingRef.current = true;

      try {
        const data = await getAllOrders();
        setOrders(Array.isArray(data) ? data : []);
      } catch (error) {
        // Only surface fetch errors for the very first load; silent
        // background polls fail quietly so the UI doesn't spam alerts
        // if the backend blips for a second.
        if (!silent) {
          showAlert("danger", error.message);
        }
      } finally {
        isFetchingRef.current = false;
        setIsInitialLoading(false);
      }
    },
    [showAlert]
  );

  useEffect(() => {
    fetchOrders({ silent: false });

    const intervalId = setInterval(() => {
      fetchOrders({ silent: true });
    }, AUTO_REFRESH_INTERVAL_MS);

    return () => clearInterval(intervalId);
  }, [fetchOrders]);

  const handlePlaceOrder = async (payload) => {
    setIsSubmitting(true);
    try {
      await placeOrder(payload);
      showAlert("success", `Order placed for ${payload.customerName}!`);
      await fetchOrders({ silent: true });
      return true;
    } catch (error) {
      showAlert("danger", error.message);
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  const totalOrders = orders.length;
  const deliveredCount = orders.filter((o) => o.status === TERMINAL_SUCCESS_STATUS).length;
  const failedCount = orders.filter((o) => o.status === TERMINAL_FAILURE_STATUS).length;
  const inProgressCount = totalOrders - deliveredCount - failedCount;

  return (
    <div className="container py-4">
      {alert && (
        <div
          className={`alert alert-${alert.type} alert-dismissible alert-floating fade show`}
          role="alert"
        >
          {alert.message}
          <button
            type="button"
            className="btn-close"
            aria-label="Close"
            onClick={() => setAlert(null)}
          ></button>
        </div>
      )}

      {/* Stat overview */}
      <div className="row g-3 mb-4">
        <div className="col-6 col-md-3">
          <div className="stat-card">
            <div className="stat-label">Total Orders</div>
            <div className="stat-value">{totalOrders}</div>
          </div>
        </div>
        <div className="col-6 col-md-3">
          <div className="stat-card">
            <div className="stat-label">In Progress</div>
            <div className="stat-value">{inProgressCount}</div>
          </div>
        </div>
        <div className="col-6 col-md-3">
          <div className="stat-card">
            <div className="stat-label">Delivered</div>
            <div className="stat-value">{deliveredCount}</div>
          </div>
        </div>
        <div className="col-6 col-md-3">
          <div className="stat-card">
            <div className="stat-label">Failed</div>
            <div className="stat-value">{failedCount}</div>
          </div>
        </div>
      </div>

      <div className="row g-4">
        {/* Place order form */}
        <div className="col-12 col-lg-4">
          <div className="dashboard-card h-100">
            <div className="card-header-custom">Place a New Order</div>
            <div className="p-3 p-md-4">
              <OrderForm onPlaceOrder={handlePlaceOrder} isSubmitting={isSubmitting} />
            </div>
          </div>
        </div>

        {/* Orders table */}
        <div className="col-12 col-lg-8">
          <div className="dashboard-card h-100">
            <div className="card-header-custom">
              Live Orders
              <span className="badge text-bg-light border">{totalOrders} total</span>
            </div>
            <div className="p-2 p-md-3">
              <OrdersTable orders={orders} isLoading={isInitialLoading} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
