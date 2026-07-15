import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import axiosInstance from "../api/axiosInstance";
import { setCredentials } from "../redux/authSlice";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await axiosInstance.post("/auth/login", {
        email,
        password,
      });
      // Matches your backend's LoginRequestDto shape exactly - { email, password }

      const { token, email: userEmail, role } = response.data;
      // Destructuring the LoginResponseDto shape - { token, email, role }

      dispatch(setCredentials({ token, email: userEmail, role }));
      // This updates Redux state AND saves to localStorage (handled inside the slice)

      navigate("/catalog");
      // Redirect to the main catalog page after successful login
    } catch (err) {
      setError("Invalid email or password");
      // Matches the deliberately vague message your backend's
      // AuthenticationException handler returns - good, consistent UX
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-cream flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <h1 className="font-serif font-bold text-4xl text-espresso text-center mb-8">
          Welcome Back
        </h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-2 rounded">
              {error}
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-espresso mb-1">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
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
            {loading ? "Logging in..." : "Log In"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
