import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";

const empty = {
  nombre: "",
  ingredienteActivo: "",
  tipoAlmacenamiento: "ROOM_TEMPERATURE",
  fechaVencimiento: "",
  precio: "",
  stock: "",
  requiereReceta: false,
};

function manana() {
  const d = new Date();
  d.setDate(d.getDate() + 1);
  return d.toISOString().split("T")[0];
}

export default function AdminProducts() {
  const [productos, setProductos] = useState([]);
  const [form, setForm] = useState(empty);
  const [editandoId, setEditandoId] = useState(null);
  const [filtro, setFiltro] = useState("");
  const [dias, setDias] = useState(30);
  const navigate = useNavigate();

  const jwt = localStorage.getItem("jwt");
  const authHeader = { Authorization: `Bearer ${jwt}` };

  function load() {
    let url = "http://localhost:8080/api/products";
    if (filtro === "expiring")
      url = `http://localhost:8080/api/products/expiring-soon/${dias}`;
    else if (filtro === "expired")
      url = "http://localhost:8080/api/products/expired";
    else if (filtro === "REFRIGERATED" || filtro === "ROOM_TEMPERATURE")
      url = `http://localhost:8080/api/products/storage/${filtro}`;

    axios
      .get(url, { headers: authHeader })
      .then((r) => setProductos(r.data))
      .catch(() => toast.error("No se pudieron cargar los productos"));
  }

  useEffect(() => {
    if (
      !localStorage.getItem("jwt") ||
      localStorage.getItem("rol") !== "ADMIN"
    ) {
      navigate("/catalog");
      return;
    }
    load();
  }, [navigate, filtro, dias]);

  async function guardar(e) {
    e.preventDefault();

    const body = {
      nombre: form.nombre,
      ingredienteActivo: form.ingredienteActivo,
      tipoAlmacenamiento: form.tipoAlmacenamiento,
      fechaVencimiento: form.fechaVencimiento,
      precio: Number(form.precio),
      stock: Number(form.stock),
      requiereReceta: form.requiereReceta,
    };

    const promesa = editandoId
      ? axios.put(`http://localhost:8080/api/products/${editandoId}`, body, {
          headers: authHeader,
        })
      : axios.post("http://localhost:8080/api/products", body, {
          headers: authHeader,
        });

    try {
      await toast.promise(promesa, {
        loading: editandoId
          ? "Actualizando producto..."
          : "Creando producto...",
        success: editandoId
          ? "Producto actualizado correctamente"
          : "Producto creado correctamente",
        error: (err) =>
          err.response?.data?.message ||
          (err.response?.data?.errors &&
            Object.values(err.response.data.errors).join(", ")) ||
          "Error al guardar",
      });

      setForm(empty);
      setEditandoId(null);
      load();
    } catch {
      // el toast ya mostró el error
    }
  }

  function eliminar(id) {
    toast(
      (t) => (
        <div className="flex flex-col gap-2">
          <p className="font-medium">Eliminar producto #{id}</p>
          <div className="flex gap-2">
            <button
              onClick={() => {
                toast.dismiss(t.id);
                confirmarEliminar(id);
              }}
              className="rounded bg-red-600 px-3 py-1 text-sm text-white"
            >
              Eliminar
            </button>
            <button
              onClick={() => toast.dismiss(t.id)}
              className="rounded bg-gray-200 px-3 py-1 text-sm"
            >
              Cancelar
            </button>
          </div>
        </div>
      ),
      { duration: 6000 },
    );
  }

  async function confirmarEliminar(id) {
    try {
      await toast.promise(
        axios.delete(`http://localhost:8080/api/products/${id}`, {
          headers: authHeader,
        }),
        {
          loading: "Eliminando...",
          success: "Producto eliminado correctamente",
          error: "No se pudo eliminar",
        },
      );
      load();
    } catch {
      // el toast ya mostró el error
    }
  }

  function editar(p) {
    setEditandoId(p.id);
    setForm({
      nombre: p.nombre,
      ingredienteActivo: p.ingredienteActivo,
      tipoAlmacenamiento: p.tipoAlmacenamiento,
      fechaVencimiento: p.fechaVencimiento,
      precio: p.precio,
      stock: p.stock,
      requiereReceta: p.requiereReceta,
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
    toast.success(`Editando producto #${p.id}`);
  }

  function logout() {
    localStorage.clear();
    toast.success("Sesión cerrada");
    navigate("/login");
  }

  return (
    <div className="mx-auto max-w-3xl space-y-6 p-8">
      <div className="flex justify-between">
        <h1 className="text-2xl font-bold">Admin Productos</h1>
        <div className="flex gap-2">
          <button
            onClick={() => navigate("/catalog")}
            className="rounded bg-gray-200 px-3 py-2 text-sm"
          >
            Catálogo
          </button>
          <button
            onClick={() => navigate("/sales")}
            className="rounded bg-gray-200 px-3 py-2 text-sm"
          >
            Ventas
          </button>
          <button
            onClick={() => navigate("/profile")}
            className="rounded bg-gray-200 px-3 py-2 text-sm"
          >
            Perfil
          </button>
          <button onClick={logout} className="text-red-600">
            Salir
          </button>
        </div>
      </div>

      <div className="flex flex-wrap items-center gap-2">
        <button
          onClick={() => setFiltro("")}
          className={`rounded px-3 py-1 text-sm ${
            filtro === "" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
        >
          Todos
        </button>
        <button
          onClick={() => setFiltro("ROOM_TEMPERATURE")}
          className={`rounded px-3 py-1 text-sm ${
            filtro === "ROOM_TEMPERATURE"
              ? "bg-blue-600 text-white"
              : "bg-gray-200"
          }`}
        >
          Temperatura ambiente
        </button>
        <button
          onClick={() => setFiltro("REFRIGERATED")}
          className={`rounded px-3 py-1 text-sm ${
            filtro === "REFRIGERATED" ? "bg-blue-600 text-white" : "bg-gray-200"
          }`}
        >
          Refrigerados
        </button>
        <button
          onClick={() => setFiltro("expiring")}
          className={`rounded px-3 py-1 text-sm ${
            filtro === "expiring" ? "bg-yellow-500 text-white" : "bg-yellow-200"
          }`}
        >
          Próximos a vencer
        </button>
        {filtro === "expiring" && (
          <input
            type="number"
            min="1"
            value={dias}
            onChange={(e) => setDias(Number(e.target.value))}
            className="w-20 rounded border px-2 py-1 text-sm"
          />
        )}
        <button
          onClick={() => setFiltro("expired")}
          className={`rounded px-3 py-1 text-sm ${
            filtro === "expired" ? "bg-red-600 text-white" : "bg-red-200"
          }`}
        >
          Vencidos
        </button>
      </div>

      <form
        onSubmit={guardar}
        className="grid gap-3 rounded border bg-white p-6"
      >
        <h2 className="font-bold">
          {editandoId ? `Editar producto #${editandoId}` : "Crear producto"}
        </h2>
        <input
          className="rounded border px-3 py-2"
          placeholder="Nombre"
          required
          value={form.nombre}
          onChange={(e) => setForm({ ...form, nombre: e.target.value })}
        />
        <input
          className="rounded border px-3 py-2"
          placeholder="Ingrediente activo"
          required
          value={form.ingredienteActivo}
          onChange={(e) =>
            setForm({ ...form, ingredienteActivo: e.target.value })
          }
        />
        <select
          className="rounded border px-3 py-2"
          value={form.tipoAlmacenamiento}
          onChange={(e) =>
            setForm({ ...form, tipoAlmacenamiento: e.target.value })
          }
        >
          <option value="ROOM_TEMPERATURE">Temperatura ambiente</option>
          <option value="REFRIGERATED">Refrigerado</option>
        </select>
        <input
          type="date"
          className="rounded border px-3 py-2"
          required
          min={manana()}
          value={form.fechaVencimiento}
          onChange={(e) =>
            setForm({ ...form, fechaVencimiento: e.target.value })
          }
        />
        <input
          type="number"
          step="0.01"
          min="0.01"
          className="rounded border px-3 py-2"
          placeholder="Precio"
          required
          value={form.precio}
          onChange={(e) => setForm({ ...form, precio: e.target.value })}
        />
        <input
          type="number"
          min="0"
          className="rounded border px-3 py-2"
          placeholder="Stock"
          required
          value={form.stock}
          onChange={(e) => setForm({ ...form, stock: e.target.value })}
        />
        <label className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={form.requiereReceta}
            onChange={(e) =>
              setForm({ ...form, requiereReceta: e.target.checked })
            }
          />
          Requiere receta
        </label>
        <div className="flex gap-2">
          <button
            type="submit"
            className="rounded bg-blue-600 px-4 py-2 text-white"
          >
            {editandoId ? "Actualizar" : "Crear"}
          </button>
          {editandoId && (
            <button
              type="button"
              onClick={() => {
                setEditandoId(null);
                setForm(empty);
                toast("Edición cancelada", { icon: "i" });
              }}
              className="rounded bg-gray-300 px-4 py-2"
            >
              Cancelar
            </button>
          )}
        </div>
      </form>

      <ul className="space-y-2">
        {productos.length === 0 && (
          <li className="text-gray-500">No hay productos para mostrar.</li>
        )}
        {productos.map((p) => (
          <li
            key={p.id}
            className="flex justify-between rounded border bg-white px-4 py-3"
          >
            <span>
              #{p.id} {p.nombre} stock {p.stock}
              <br />
              <span className="text-xs text-gray-500">
                {p.tipoAlmacenamiento} Vence: {p.fechaVencimiento}
                {p.requiereReceta && " Requiere receta"}
              </span>
            </span>
            <span className="flex gap-2">
              <button
                onClick={() => editar(p)}
                className="text-sm text-blue-600"
              >
                Editar
              </button>
              <button
                onClick={() => eliminar(p.id)}
                className="text-sm text-red-600"
              >
                Eliminar
              </button>
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
}
