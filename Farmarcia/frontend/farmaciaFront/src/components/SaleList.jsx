import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";

export default function SaleList() {
  const [ventas, setVentas] = useState([]);
  const navigate = useNavigate();
  const rol = localStorage.getItem("rol");

  useEffect(() => {
    if (!localStorage.getItem("jwt")) {
      navigate("/login");
      return;
    }
    const jwt = localStorage.getItem("jwt");
    axios
      .get("http://localhost:8080/api/sales", {
        headers: { Authorization: `Bearer ${jwt}` },
      })
      .then((res) => setVentas(res.data))
      .catch(() => toast.error("No se pudieron cargar las ventas"));
  }, [navigate]);

  return (
    <div className="mx-auto max-w-2xl space-y-4 p-8">
      <button onClick={() => navigate(-1)} className="text-blue-600">
        Volver
      </button>
      <h1 className="text-2xl font-bold">
        {rol === "ADMIN" ? "Todas las ventas" : "Mis ventas"}
      </h1>
      {ventas.length === 0 && (
        <p className="text-gray-500">No hay ventas registradas.</p>
      )}
      {ventas.map((v) => (
        <div key={v.id} className="rounded border bg-white p-4">
          <p>
            <strong>#{v.id}</strong> {v.nombreProducto} x {v.cantidad}
          </p>
          <p className="text-sm text-gray-500">
            Vendedor: {v.nombreVendedor} |{" "}
            {new Date(v.fechaVenta).toLocaleString()}
          </p>
        </div>
      ))}
    </div>
  );
}
