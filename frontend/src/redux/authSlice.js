import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";

export const fetchCurrentMember = createAsyncThunk(
  "auth/fetchCurrentMember",
  async () => {
    const response = await axiosInstance.get("/members/me");
    return response.data;
    // Returns MemberResponseDto: { id, name, email, phoneNumber, membershipDate }
  },
);

const storedToken = localStorage.getItem("token");
const storedEmail = localStorage.getItem("email");
const storedRole = localStorage.getItem("role");

const initialState = {
  token: storedToken || null,
  email: storedEmail || null,
  role: storedRole || null,
  memberId: null,
  // NEW - not persisted to localStorage, refetched fresh on app load instead
  isAuthenticated: !!storedToken,
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

      localStorage.setItem("token", token);
      localStorage.setItem("email", email);
      localStorage.setItem("role", role);
    },
    logout: (state) => {
      state.token = null;
      state.email = null;
      state.role = null;
      state.memberId = null;
      state.isAuthenticated = false;

      localStorage.removeItem("token");
      localStorage.removeItem("email");
      localStorage.removeItem("role");
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchCurrentMember.fulfilled, (state, action) => {
      state.memberId = action.payload.id;
    });
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;
