import { useState } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-hot-toast";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    try {
      const { data } = await axios.post(
        "http://localhost:8080/api/auth/login",
        { email, password },
      );

      localStorage.setItem("jwt", data.jwt);
      localStorage.setItem("idUsuario", data.idUsuario);
      localStorage.setItem("rol", data.rol);

      toast.success("Sesión iniciada correctamente");
      navigate(data.rol === "ADMIN" ? "/admin/products" : "/catalog");
    } catch (err) {
      toast.error(
        err.response?.status === 401 || err.response?.status === 403
          ? "Email o contraseña incorrectos"
          : "Error al iniciar sesión",
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
      <div className="w-full max-w-md space-y-6 rounded-2xl border bg-white p-10 shadow-xl">
        <h2 className="text-center text-3xl font-extrabold">Farmacia Login</h2>
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-sm font-medium">Email</label>
            <input
              type="email"
              required
              className="mt-1 w-full rounded-lg border px-4 py-3"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div>
            <label className="block text-sm font-medium">Contraseña</label>
            <input
              type="password"
              required
              className="mt-1 w-full rounded-lg border px-4 py-3"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="w-full rounded-lg bg-blue-600 py-3 text-white hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>
        <p className="text-center text-sm">
          No tienes cuenta?{" "}
          <Link to="/register" className="font-medium text-blue-600">
            Registrate
          </Link>
        </p>
      </div>
    </div>
  );
}
