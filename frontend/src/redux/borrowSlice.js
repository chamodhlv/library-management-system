import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";

export const fetchMyBorrows = createAsyncThunk(
  "borrow/fetchMyBorrows",
  async (memberId) => {
    const response = await axiosInstance.get(
      `/borrow-records/member/${memberId}`,
    );
    return response.data;
  },
);

export const returnBook = createAsyncThunk(
  "borrow/returnBook",
  async (borrowRecordId) => {
    const response = await axiosInstance.patch(
      `/borrow-records/${borrowRecordId}/return`,
    );
    return response.data;
  },
);

const borrowSlice = createSlice({
  name: "borrow",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchMyBorrows.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchMyBorrows.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchMyBorrows.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })
      .addCase(returnBook.fulfilled, (state, action) => {
        const index = state.items.findIndex((r) => r.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
        // Instead of re-fetching the whole list after returning a book,
        // we just swap the single updated record in place - faster,
        // and Immer (used internally by Toolkit) lets us "mutate" state directly here safely
      });
  },
});

export default borrowSlice.reducer;
