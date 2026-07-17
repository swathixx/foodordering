import React, { useState } from "react";

const INITIAL_FORM_STATE = {
  customerName: "",
  foodItem: "",
  amount: "",
};

const OrderForm = ({ onPlaceOrder, isSubmitting }) => {
  const [formData, setFormData] = useState(INITIAL_FORM_STATE);
  const [validationErrors, setValidationErrors] = useState({});

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validate = () => {
    const errors = {};

    if (!formData.customerName.trim()) {
      errors.customerName = "Customer name is required.";
    }

    if (!formData.foodItem.trim()) {
      errors.foodItem = "Food item is required.";
    }

    const amountNumber = Number(formData.amount);

    if (!formData.amount || Number.isNaN(amountNumber) || amountNumber <= 0) {
      errors.amount = "Enter a valid amount greater than 0.";
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!validate()) {
      return;
    }

    const payload = {
      customerName: formData.customerName.trim(),
      foodItem: formData.foodItem.trim(),
      amount: Number(formData.amount),
    };

    const success = await onPlaceOrder(payload);

    if (success) {
      setFormData(INITIAL_FORM_STATE);
      setValidationErrors({});
    }
  };

  return (
    <form className="order-form" onSubmit={handleSubmit} noValidate>
      <div className="mb-3">
        <label htmlFor="customerName" className="form-label">
          Customer Name
        </label>

        <input
          type="text"
          className={`form-control ${
            validationErrors.customerName ? "is-invalid" : ""
          }`}
          id="customerName"
          name="customerName"
          placeholder="e.g. Susil"
          value={formData.customerName}
          onChange={handleChange}
          disabled={isSubmitting}
        />

        {validationErrors.customerName && (
          <div className="invalid-feedback">
            {validationErrors.customerName}
          </div>
        )}
      </div>

      <div className="mb-3">
        <label htmlFor="foodItem" className="form-label">
          Food Item
        </label>

        <input
          type="text"
          className={`form-control ${
            validationErrors.foodItem ? "is-invalid" : ""
          }`}
          id="foodItem"
          name="foodItem"
          placeholder="e.g. Pizza"
          value={formData.foodItem}
          onChange={handleChange}
          disabled={isSubmitting}
        />

        {validationErrors.foodItem && (
          <div className="invalid-feedback">
            {validationErrors.foodItem}
          </div>
        )}
      </div>

      <div className="mb-4">
        <label htmlFor="amount" className="form-label">
          Amount (₹)
        </label>

        <input
          type="number"
          className={`form-control ${
            validationErrors.amount ? "is-invalid" : ""
          }`}
          id="amount"
          name="amount"
          placeholder="e.g. 250"
          min="1"
          step="0.01"
          value={formData.amount}
          onChange={handleChange}
          disabled={isSubmitting}
        />

        {validationErrors.amount && (
          <div className="invalid-feedback">
            {validationErrors.amount}
          </div>
        )}
      </div>

      <button
        type="submit"
        className="btn btn-accent w-100"
        disabled={isSubmitting}
      >
        {isSubmitting ? (
          <>
            <span
              className="spinner-border spinner-border-sm me-2"
              role="status"
              aria-hidden="true"
            ></span>
            Placing Order...
          </>
        ) : (
          "Place Order"
        )}
      </button>
    </form>
  );
};

export default OrderForm;