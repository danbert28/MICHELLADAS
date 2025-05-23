window.addEventListener('DOMContentLoaded', () => {
    const logo = document.querySelector('.logo-overlay');

    let isPaused = false;

    logo.addEventListener('click', () => {
    if (isPaused) {
        logo.style.animationPlayState = 'running';
    } else {
        logo.style.animationPlayState = 'paused';
    }
    isPaused = !isPaused;
    });
});

document.addEventListener('DOMContentLoaded', function () {
    actualizarContadorCarrito();
});

function actualizarContadorCarrito() {
    fetch('/carrito/contador')
        .then(res => res.json())
        .then(data => {
            document.getElementById('contadorCarrito').textContent = data;
        });
}


