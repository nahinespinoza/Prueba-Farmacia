import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";

export default function Profile() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!localStorage.getItem("jwt")) {
      navigate("/login");
      return;
    }
    const jwt = localStorage.getItem("jwt");
    axios
      .get("http://localhost:8080/api/auth/me", {
        headers: { Authorization: `Bearer ${jwt}` },
      })
      .then((res) => setUser(res.data))
      .catch(() => toast.error("No se pudo cargar el perfil"));
  }, [navigate]);

  function logout() {
    localStorage.clear();
    toast.success("Sesión cerrada");
    navigate("/login");
  }

  if (!user) return <p className="p-8">Cargando...</p>;

  return (
    <div className="mx-auto max-w-md space-y-4 p-8">
      <h1 className="text-2xl font-bold">Mi perfil</h1>
      <div className="space-y-2 rounded border bg-white p-6">
        <p>
          <strong>ID:</strong> {user.id}
        </p>
        <p>
          <strong>Nombre:</strong> {user.nombre}
        </p>
        <p>
          <strong>Email:</strong> {user.email}
        </p>
        <p>
          <strong>Rol:</strong> {user.rol}
        </p>
      </div>
      <div className="flex gap-2">
        <button
          onClick={() => navigate("/catalog")}
          className="rounded bg-gray-200 px-4 py-2"
        >
          Ir al catálogo
        </button>
        <button
          onClick={logout}
          className="rounded bg-red-600 px-4 py-2 text-white"
        >
          Cerrar sesión
        </button>
      </div>
    </div>
  );
}
