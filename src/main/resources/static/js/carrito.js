function abrirModalLogin() {
    document.getElementById("modalRegistro").style.display = "flex";
    document.getElementById("formLogin").style.display = "block";
    document.getElementById("formRegistro").style.display = "none";
}
function loginUsuario() {
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    })
        .then(res => res.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem("token", data.token);
                localStorage.setItem("nombre", data.name);
                mostrarformPedido();
            } else {
                alert("Credenciales incorrectas.");
            }
        });
}
function cerrarModalRegistro() {
    document.getElementById("modalRegistro").style.display = "none";
    document.getElementById("formLogin").style.display = "none";
    document.getElementById("formRegistro").style.display = "none";
}
function registrarUsuario() {
    const email = document.getElementById("email").value;
    const nombre = document.getElementById("nombre").value;
    const password = document.getElementById("password").value;

    fetch('/api/auth/register', {
        method: 'POST',
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            email, password, name: nombre,
            telefono: "000", direccion: "Sahagún",
            role: "USER" // o "USUARIO" si así lo tienes
        })
    })
        .then(res => res.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem("token", data.token);
                localStorage.setItem("nombre", data.name);
                mostrarFormularioPedido();
            }
        });
}
function enviarPedido() {
    const token = localStorage.getItem("token");
    const direccion = document.querySelector("input[placeholder='Dirección']").value;
    const metodoPago = document.querySelector("input[name='pago']:checked").value;
    const notas = document.querySelector("textarea").value;

    const productos = [
        { productoId: 1, cantidad: 2 } // ← esto debes obtenerlo dinámicamente según tu carrito
    ];

    fetch("/api/pedidos/crear", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            direccion,
            metodoPago,
            notas,
            productos
        })
    })
        .then(res => res.ok ? res.json() : Promise.reject(res))
        .then(data => {
            alert("¡Pedido realizado con éxito!");
            window.location.href = "/"; // o a una página de confirmación
        })
        .catch(() => alert("Error al enviar el pedido."));
}


