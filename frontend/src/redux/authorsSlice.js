import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";

export const fetchAuthors = createAsyncThunk(
  "authors/fetchAuthors",
  async () => {
    const response = await axiosInstance.get("/authors");
    return response.data;
  },
);

export const createAuthor = createAsyncThunk(
  "authors/createAuthor",
  async (authorData) => {
    const response = await axiosInstance.post("/authors", authorData);
    return response.data;
  },
);

export const updateAuthor = createAsyncThunk(
  "authors/updateAuthor",
  async ({ id, authorData }) => {
    const response = await axiosInstance.put(`/authors/${id}`, authorData);
    return response.data;
  },
);

export const deleteAuthor = createAsyncThunk(
  "authors/deleteAuthor",
  async (id) => {
    await axiosInstance.delete(`/authors/${id}`);
    return id;
  },
);

const authorsSlice = createSlice({
  name: "authors",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchAuthors.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAuthors.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchAuthors.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })
      .addCase(createAuthor.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })
      .addCase(updateAuthor.fulfilled, (state, action) => {
        const index = state.items.findIndex((a) => a.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
      })
      .addCase(deleteAuthor.fulfilled, (state, action) => {
        state.items = state.items.filter((a) => a.id !== action.payload);
      });
  },
});

export default authorsSlice.reducer;
