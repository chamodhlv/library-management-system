import { createSlice } from "@reduxjs/toolkit";

const storedToken = localStorage.getItem("token");
const storedEmail = localStorage.getItem("email");
const storedRole = localStorage.getItem("role");
// On app load, check if we already have auth info saved from a previous session -
// this is what makes "staying logged in after refresh" work

const initialState = {
  token: storedToken || null,
  email: storedEmail || null,
  role: storedRole || null,
  isAuthenticated: !!storedToken,
  // !!storedToken converts "string or null" into a clean true/false
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state, action) => {
      const { token, email, role } = action.payload;
      state.token = token;
      state.email = email;
      state.role = role;
      state.isAuthenticated = true;

      // Persist to localStorage so a page refresh doesn't log the user out
      localStorage.setItem("token", token);
      localStorage.setItem("email", email);
      localStorage.setItem("role", role);
    },
    logout: (state) => {
      state.token = null;
      state.email = null;
      state.role = null;
      state.isAuthenticated = false;

      localStorage.removeItem("token");
      localStorage.removeItem("email");
      localStorage.removeItem("role");
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;
