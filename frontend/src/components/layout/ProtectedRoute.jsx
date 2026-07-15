import { useSelector } from "react-redux";
import { Navigate, Outlet } from "react-router-dom";

function ProtectedRoute({ allowedRoles }) {
  const { isAuthenticated, role } = useSelector((state) => state.auth);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
    // Not logged in at all - kick back to login immediately
  }

  if (allowedRoles && !allowedRoles.includes(role)) {
    return <Navigate to="/catalog" replace />;
    // Logged in, but wrong role for this specific route -
    // redirect somewhere safe rather than showing a blank/broken page
  }

  return <Outlet />;
  // Passes all checks - render whatever child route was actually requested
}

export default ProtectedRoute;
