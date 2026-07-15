import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout from "./components/layout/AppLayout";
import ProtectedRoute from "./components/layout/ProtectedRoute";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import CatalogPage from "./pages/CatalogPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public - no sidebar */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Everyone (including guests) - sidebar layout */}
        <Route element={<AppLayout />}>
          <Route path="/catalog" element={<CatalogPage />} />

          {/* Any authenticated user (MEMBER or LIBRARIAN) */}
          <Route element={<ProtectedRoute />}>
            <Route
              path="/my-borrows"
              element={<div className="p-8">My Borrows - coming soon</div>}
            />
          </Route>

          {/* LIBRARIAN only */}
          <Route element={<ProtectedRoute allowedRoles={["LIBRARIAN"]} />}>
            <Route
              path="/librarian/books"
              element={<div className="p-8">Manage Books - coming soon</div>}
            />
            <Route
              path="/librarian/authors"
              element={<div className="p-8">Manage Authors - coming soon</div>}
            />
            <Route
              path="/librarian/categories"
              element={
                <div className="p-8">Manage Categories - coming soon</div>
              }
            />
            <Route
              path="/librarian/members"
              element={<div className="p-8">Manage Members - coming soon</div>}
            />
            <Route
              path="/librarian/borrow-records"
              element={
                <div className="p-8">All Borrow Records - coming soon</div>
              }
            />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
