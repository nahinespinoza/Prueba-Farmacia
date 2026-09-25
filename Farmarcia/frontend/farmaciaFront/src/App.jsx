import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Login from "./components/Login";
import Register from "./components/Register";
import Catalog from "./components/Catalog";
import SaleList from "./components/SaleList";
import AdminProducts from "./components/AdminProducts";
import Profile from "./components/Profile";

function PrivateRoute({ children, roles }) {
  const jwt = localStorage.getItem("jwt");
  const rol = localStorage.getItem("rol");
  if (!jwt) return <Navigate to="/login" replace />;
  if (roles && !roles.includes(rol)) return <Navigate to="/catalog" replace />;
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-right" />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/catalog"
          element={
            <PrivateRoute>
              <Catalog />
            </PrivateRoute>
          }
        />
        <Route
          path="/sales"
          element={
            <PrivateRoute>
              <SaleList />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <Profile />
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/products"
          element={
            <PrivateRoute roles={["ADMIN"]}>
              <AdminProducts />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
