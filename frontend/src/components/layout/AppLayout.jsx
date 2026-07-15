import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";

function AppLayout() {
  return (
    <div className="flex">
      <Sidebar />
      <main className="flex-1 bg-cream min-h-screen">
        <Outlet />
        {/* Outlet renders whichever child route is currently active -
            this is React Router's way of doing shared layouts */}
      </main>
    </div>
  );
}

export default AppLayout;
