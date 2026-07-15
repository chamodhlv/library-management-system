import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
  // Every request made with this instance automatically gets this prefix -
  // so components just call axiosInstance.get('/books') instead of the full URL
});

// Request interceptor - runs BEFORE every single request is sent
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  // We'll store the JWT in localStorage after login (persists across page refreshes,
  // unlike Redux state which resets on reload)

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    // Attaches "Authorization: Bearer <token>" to every outgoing request automatically -
    // this is exactly what your JwtAuthenticationFilter on the backend expects
  }

  return config;
});

export default axiosInstance;
