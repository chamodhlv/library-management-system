import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../api/axiosInstance";

export const fetchBooks = createAsyncThunk("books/fetchBooks", async () => {
  const response = await axiosInstance.get("/books");
  return response.data;
});

export const createBook = createAsyncThunk(
  "books/createBook",
  async (bookData) => {
    const response = await axiosInstance.post("/books", bookData);
    return response.data;
  },
);

export const updateBook = createAsyncThunk(
  "books/updateBook",
  async ({ id, bookData }) => {
    const response = await axiosInstance.put(`/books/${id}`, bookData);
    return response.data;
  },
);

export const deleteBook = createAsyncThunk("books/deleteBook", async (id) => {
  await axiosInstance.delete(`/books/${id}`);
  return id;
});

const booksSlice = createSlice({
  name: "books",
  initialState: {
    items: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchBooks.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchBooks.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload;
      })
      .addCase(fetchBooks.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      })
      .addCase(createBook.fulfilled, (state, action) => {
        state.items.push(action.payload);
      })
      .addCase(updateBook.fulfilled, (state, action) => {
        const index = state.items.findIndex((b) => b.id === action.payload.id);
        if (index !== -1) {
          state.items[index] = action.payload;
        }
      })
      .addCase(deleteBook.fulfilled, (state, action) => {
        state.items = state.items.filter((b) => b.id !== action.payload);
      });
  },
});

export default booksSlice.reducer;
