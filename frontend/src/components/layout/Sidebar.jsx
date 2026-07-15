import { NavLink, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../redux/authSlice";

function Sidebar() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isAuthenticated, email, role } = useSelector((state) => state.auth);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/login");
  };

  const linkClasses = ({ isActive }) =>
    `block px-4 py-2 rounded-md text-sm font-medium transition-colors ${
      isActive ? "bg-espresso text-cream" : "text-espresso hover:bg-espresso/10"
    }`;
  // NavLink gives us isActive automatically - highlights whichever
  // page the user is currently on

  return (
    <aside className="w-64 min-h-screen bg-white border-r border-espresso/10 flex flex-col p-4">
      <h1 className="font-serif font-bold text-2xl text-espresso mb-8 px-2">
        Library
      </h1>

      <nav className="flex-1 space-y-1">
        <NavLink to="/catalog" className={linkClasses}>
          Catalog
        </NavLink>

        {isAuthenticated && (
          <NavLink to="/my-borrows" className={linkClasses}>
            My Borrows
          </NavLink>
        )}

        {isAuthenticated && role === "LIBRARIAN" && (
          <>
            <div className="pt-4 pb-1 px-2 text-xs font-semibold text-espresso/40 uppercase tracking-wide">
              Librarian
            </div>
            <NavLink to="/librarian/books" className={linkClasses}>
              Manage Books
            </NavLink>
            <NavLink to="/librarian/authors" className={linkClasses}>
              Manage Authors
            </NavLink>
            <NavLink to="/librarian/categories" className={linkClasses}>
              Manage Categories
            </NavLink>
            <NavLink to="/librarian/members" className={linkClasses}>
              Manage Members
            </NavLink>
            <NavLink to="/librarian/borrow-records" className={linkClasses}>
              All Borrow Records
            </NavLink>
          </>
        )}
      </nav>

      <div className="border-t border-espresso/10 pt-4 mt-4">
        {isAuthenticated ? (
          <div className="px-2">
            <p className="text-xs text-espresso/50 mb-2 truncate">{email}</p>
            <button
              onClick={handleLogout}
              className="w-full text-left px-2 py-2 rounded-md text-sm font-medium
                         text-espresso hover:bg-espresso/10 transition-colors"
            >
              Log Out
            </button>
          </div>
        ) : (
          <div className="space-y-1">
            <NavLink to="/login" className={linkClasses}>
              Log In
            </NavLink>
            <NavLink to="/register" className={linkClasses}>
              Register
            </NavLink>
          </div>
        )}
      </div>
    </aside>
  );
}

export default Sidebar;
