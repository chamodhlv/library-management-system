import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { fetchCurrentMember } from "./redux/authSlice";
import AppLayout from "./components/layout/AppLayout";
import ProtectedRoute from "./components/layout/ProtectedRoute";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import CatalogPage from "./pages/CatalogPage";
import MyBorrowsPage from "./pages/MyBorrowsPage";
import ManageAuthorsPage from "./pages/librarian/ManageAuthorsPage";
import ManageCategoriesPage from "./pages/librarian/ManageCategoriesPage";
import ManageBooksPage from "./pages/librarian/ManageBooksPage";
import ManageMembersPage from "./pages/librarian/ManageMembersPage";
import AllBorrowRecordsPage from "./pages/librarian/AllBorrowRecordsPage";

function App() {
  const dispatch = useDispatch();
  const { isAuthenticated, memberId } = useSelector((state) => state.auth);

  useEffect(() => {
    if (isAuthenticated && !memberId) {
      dispatch(fetchCurrentMember());
      // Runs once when the app first loads - if we're logged in (token exists
      // in localStorage) but don't yet have memberId in memory, fetch it now
    }
  }, [isAuthenticated, memberId, dispatch]);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<AppLayout />}>
          <Route path="/catalog" element={<CatalogPage />} />

          <Route element={<ProtectedRoute />}>
            <Route path="/my-borrows" element={<MyBorrowsPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["LIBRARIAN"]} />}>
            <Route path="/librarian/books" element={<ManageBooksPage />} />
            <Route path="/librarian/authors" element={<ManageAuthorsPage />} />
            <Route
              path="/librarian/categories"
              element={<ManageCategoriesPage />}
            />
            <Route path="/librarian/members" element={<ManageMembersPage />} />
            <Route
              path="/librarian/borrow-records"
              element={<AllBorrowRecordsPage />}
            />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
