import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./authSlice";
import booksReducer from "./booksSlice";
import borrowReducer from "./borrowSlice";
import authorsReducer from "./authorsSlice";
import categoriesReducer from "./categoriesSlice";
import membersReducer from "./membersSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    books: booksReducer,
    borrow: borrowReducer,
    authors: authorsReducer,
    categories: categoriesReducer,
    members: membersReducer,
  },
});
