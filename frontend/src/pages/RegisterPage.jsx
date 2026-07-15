import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axiosInstance from "../api/axiosInstance";

function RegisterPage() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phoneNumber: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    // Generic handler - works for any input by matching its "name" attribute
    // to the formData key, so we don't need a separate onChange per field
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await axiosInstance.post("/auth/register", formData);
      // Matches your backend's MemberRequestDto shape exactly:
      // { name, email, password, phoneNumber }

      navigate("/login");
      // After successful registration, send them to log in -
      // register doesn't return a token, only login does
    } catch (err) {
      const message = err.response?.data?.message || "Registration failed";
      // Pulls the actual validation message from your backend's
      // MethodArgumentNotValidException handler, e.g. "email: Email must be valid"
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-cream flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <h1 className="font-serif font-bold text-4xl text-espresso text-center mb-8">
          Create Account
        </h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-2 rounded">
              {error}
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Name
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40
                         bg-white text-espresso"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Email
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40
                         bg-white text-espresso"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Password
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              minLength={8}
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40
                         bg-white text-espresso"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Phone Number
            </label>
            <input
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-espresso/20 rounded-md
                         focus:outline-none focus:ring-2 focus:ring-espresso/40
                         bg-white text-espresso"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-espresso text-cream font-medium py-2 rounded-md
                       hover:bg-espresso/90 transition-colors disabled:opacity-50"
          >
            {loading ? "Creating account..." : "Register"}
          </button>
        </form>

        <p className="text-center text-sm text-espresso/60 mt-6">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-espresso font-medium hover:underline"
          >
            Log in
          </Link>
        </p>
      </div>
    </div>
  );
}

export default RegisterPage;
