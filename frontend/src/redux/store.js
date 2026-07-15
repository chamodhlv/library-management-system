import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import booksReducer from "./booksSlice";
import borrowReducer from "./borrowSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    books: booksReducer,
    borrow: borrowReducer,
  },
});
