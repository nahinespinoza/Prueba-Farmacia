import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";

export default function Catalog() {
  const [productos, setProductos] = useState([]);
  const [productoId, setProductoId] = useState("");
  const [cantidad, setCantidad] = useState(1);
  const navigate = useNavigate();
  const rol = localStorage.getItem("rol");

  function cargarProductos() {
    const jwt = localStorage.getItem("jwt");
    axios
      .get("http://localhost:8080/api/products", {
        headers: { Authorization: `Bearer ${jwt}` },
      })
      .then((res) => setProductos(res.data))
      .catch(() => toast.error("No se pudieron cargar los productos"));
  }

  useEffect(() => {
    if (!localStorage.getItem("jwt")) {
      navigate("/login");
      return;
    }
    cargarProductos();
  }, [navigate]);

  async function vender(e) {
    e.preventDefault();
    const jwt = localStorage.getItem("jwt");

    try {
      const { data } = await toast.promise(
        axios.post(
          "http://localhost:8080/api/sales",
          { productoId: Number(productoId), cantidad: Number(cantidad) },
          { headers: { Authorization: `Bearer ${jwt}` } },
        ),
        {
          loading: "Registrando venta...",
          success: (res) => `Venta #${res.data.id} registrada`,
          error: (err) =>
            err.response?.data?.message || "No se pudo registrar la venta",
        },
      );

      setProductoId("");
      setCantidad(1);
      cargarProductos();
    } catch {
      // el toast ya mostró el error
    }
  }

  function logout() {
    localStorage.clear();
    toast.success("Sesión cerrada");
    navigate("/login");
  }

  return (
    <div className="mx-auto max-w-3xl space-y-8 p-8">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Catálogo</h1>
        <div className="flex gap-2">
          <button
            onClick={() => navigate("/sales")}
            className="rounded bg-gray-200 px-3 py-2 text-sm"
          >
            Mis ventas
          </button>
          <button
            onClick={() => navigate("/profile")}
            className="rounded bg-gray-200 px-3 py-2 text-sm"
          >
            Mi perfil
          </button>
          {rol === "ADMIN" && (
            <button
              onClick={() => navigate("/admin/products")}
              className="rounded bg-blue-600 px-3 py-2 text-sm text-white"
            >
              Admin
            </button>
          )}
          <button
            onClick={logout}
            className="rounded bg-red-600 px-3 py-2 text-sm text-white"
          >
            Salir
          </button>
        </div>
      </div>

      <ul className="space-y-2">
        {productos.length === 0 && (
          <li className="text-gray-500">No hay productos disponibles.</li>
        )}
        {productos.map((p) => (
          <li
            key={p.id}
            className="flex justify-between rounded border bg-white px-4 py-3"
          >
            <span>
              <strong>#{p.id}</strong> {p.nombre} {p.ingredienteActivo}
              <br />
              <span className="text-sm text-gray-500">
                Vence: {p.fechaVencimiento} Stock: {p.stock}{" "}
                {p.tipoAlmacenamiento}
                {p.requiereReceta && " Requiere receta"}
              </span>
            </span>
            <span className="font-semibold">${p.precio}</span>
          </li>
        ))}
      </ul>

      {rol === "USER" && (
        <form
          onSubmit={vender}
          className="space-y-3 rounded-xl border bg-white p-6"
        >
          <h2 className="text-lg font-bold">Registrar venta</h2>
          <input
            type="number"
            placeholder="ID producto"
            required
            className="w-full rounded border px-3 py-2"
            value={productoId}
            onChange={(e) => setProductoId(e.target.value)}
          />
          <input
            type="number"
            min="1"
            placeholder="Cantidad"
            required
            className="w-full rounded border px-3 py-2"
            value={cantidad}
            onChange={(e) => setCantidad(e.target.value)}
          />
          <button
            type="submit"
            className="w-full rounded bg-blue-600 py-2 text-white"
          >
            Vender
          </button>
        </form>
      )}
    </div>
  );
}
