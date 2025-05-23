// Datos de ejemplo basados en la imagen
const ordersData = [
    {
        id: '#00031',
        cliente: 'Adam Smith',
        fecha: '05/01/2024',
        direccion: 'Kra 18 5-30',
        total: 155.00,
        pago: 'Nequi'
    },
    {
        id: '#00032',
        cliente: 'Edward James',
        fecha: '05/01/2024',
        direccion: 'Calle 20 4-50',
        total: 1550.00,
        pago: 'Efectivo'
    },
    // ... Agregar más registros según sea necesario
];

// Función para renderizar la tabla
function renderOrdersTable(data) {
    const tbody = document.getElementById('ordersTableBody');
    tbody.innerHTML = data.map(order => `
        <tr>
            <td>${order.id}</td>
            <td>${order.cliente}</td>
            <td>${order.fecha}</td>
            <td>${order.direccion}</td>
            <td>$${order.total.toFixed(2)}</td>
            <td><span class="payment-method">${order.pago}</span></td>
            <td>
                <button class="action-btn view-btn">
                    <i class="bi bi-eye"></i>
                </button>
                <button class="action-btn print-btn">
                    <i class="bi bi-printer"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

// Inicialización
document.addEventListener('DOMContentLoaded', () => {
    renderOrdersTable(ordersData);
    
    // Función de búsqueda
    document.querySelector('.search-container input').addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filtered = ordersData.filter(order => 
            order.id.toLowerCase().includes(searchTerm) ||
            order.cliente.toLowerCase().includes(searchTerm)
        );
        renderOrdersTable(filtered);
    });
});
