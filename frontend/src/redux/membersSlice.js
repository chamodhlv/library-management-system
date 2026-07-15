import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";

export const fetchMembers = createAsyncThunk(
  "members/fetchMembers",
  async () => {
    const response = await axiosInstance.get("/members");
    return response.data;
  },
);

export const updateMemberRole = createAsyncThunk(
  "members/updateMemberRole",
  async ({ id, role }) => {
    const response = await axiosInstance.patch(`/members/${id}/role`, { role });
    return response.data;
  },
);

export const deleteMember = createAsyncThunk(
  "members/deleteMember",
  async (id) => {
    await axiosInstance.delete(`/members/${id}`);
    return id;
  },
);

const membersSlice = createSlice({
  name: "members",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchMembers.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchMembers.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchMembers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })
      .addCase(updateMemberRole.fulfilled, (state, action) => {
        const index = state.items.findIndex((m) => m.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
      })
      .addCase(deleteMember.fulfilled, (state, action) => {
        state.items = state.items.filter((m) => m.id !== action.payload);
      });
  },
});

export default membersSlice.reducer;
