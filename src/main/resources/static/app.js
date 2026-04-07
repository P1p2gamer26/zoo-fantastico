// ==========================================
// CONFIGURACIÓN E INICIALIZACIÓN
// ==========================================
const API_Z = '/api/zonas';
const API_C = '/api/creatures';
let chP, chS;

document.addEventListener('DOMContentLoaded', () => {
    // Lógica para el menú de navegación
    document.querySelectorAll('.nav-link').forEach(l => {
        l.onclick = (e) => {
            if (l.dataset.target) {
                e.preventDefault();
                document.querySelectorAll('.nav-link, .view').forEach(x => x.classList.remove('active'));
                l.classList.add('active');
                const target = document.getElementById(l.dataset.target);
                if(target) target.classList.add('active');
            }
        };
    });

    // Cerrar cualquier modal
    const closeBtns = document.querySelectorAll('.close-modal');
    closeBtns.forEach(btn => {
        btn.onclick = () => {
            document.querySelectorAll('.modal').forEach(m => m.style.display = 'none');
        }
    });

    // Cargar toda la información al abrir la página
    cargarTodo();
});

// ==========================================
// CARGA PRINCIPAL DE DATOS
// ==========================================
async function cargarTodo() {
    try {
        // Obtenemos Zonas y Criaturas al mismo tiempo
        const [resZ, resC] = await Promise.all([fetch(API_Z), fetch(API_C)]);
        const zs = await resZ.json();
        const cs = await resC.json();
        
        actualizarTablas(zs, cs);
        actualizarStats(zs.length, cs.length);
        renderCharts(zs, cs);
    } catch (err) { 
        console.error("Error al cargar los datos:", err); 
    }
}

// ==========================================
// RENDERIZADO VISUAL (A PRUEBA DE FALLOS)
// ==========================================
function actualizarTablas(zs, cs) {
    const tbZ = document.getElementById('tabla-zonas');
    const tbC = document.getElementById('tabla-criaturas');
    const selectZonaEditC = document.getElementById('edit-zona-id'); 

    // 1. Dibujar Tabla de Zonas
    if (tbZ) {
        tbZ.innerHTML = zs.map(z => {
            // Protección contra datos Nulos
            const n = z.nombre || "DATO ROTO (Bórrame)";
            const d = z.descripcion || "Sin descripción";
            
            // Protección para las comillas en los botones
            const nEscapado = n.replace(/'/g, "\\'");
            const dEscapado = d.replace(/'/g, "\\'");

            return `<tr>
                <td>#${z.id}</td>
                <td><strong>${n}</strong></td>
                <td>${d}</td>
                <td>
                    <button class="btn-edit" onclick="abrirEditarZona(${z.id}, '${nEscapado}', '${dEscapado}')">✏</button>
                    <button class="btn-delete" onclick="borrarZ(${z.id})">🗑</button>
                </td>
            </tr>`;
        }).join('');
    }

    // 2. Dibujar Tabla de Criaturas
    if (tbC) {
        tbC.innerHTML = cs.map(c => {
            const zonaNombre = (c.zona && c.zona.nombre) ? c.zona.nombre : 'Sin zona';
            return `<tr>
                <td><strong>${c.name || 'N/A'}</strong><br><small>${c.species || ''}</small></td>
                <td>P:${c.dangerLevel || 0} | T:${c.size || 0}m</td>
                <td><span class="badge">${c.healthStatus || 'N/A'}</span></td>
                <td>${zonaNombre}</td>
                <td>
                    <button class="btn-edit" onclick="abrirEditarC(${c.id})">✏</button>
                    <button class="btn-delete" onclick="borrarC(${c.id})">🗑</button>
                </td>
            </tr>`;
        }).join('');
    }

    // 3. Actualizar los selects (desplegables) de Zonas para los formularios
    const optionsZ = zs.map(z => `<option value="${z.id}">${z.nombre || 'Zona Rota #' + z.id}</option>`).join('');
    if (selectZonaEditC) selectZonaEditC.innerHTML = optionsZ;
    // Si tienes un select para crear criatura, actualízalo aquí también
    const selectCreaC = document.getElementById('c-zona-id');
    if (selectCreaC) selectCreaC.innerHTML = optionsZ;
}

function actualizarStats(z, c) {
    const statZ = document.getElementById('stat-zonas');
    const statC = document.getElementById('stat-criaturas');
    if(statZ) statZ.innerText = z;
    if(statC) statC.innerText = c;
}

function renderCharts(zs, cs) {
    const ctxP = document.getElementById('chart-poblacion');
    const ctxS = document.getElementById('chart-salud');
    
    if(!ctxP || !ctxS) return;
    if(chP) chP.destroy(); 
    if(chS) chS.destroy();
    
    chP = new Chart(ctxP, {
        type:'bar', 
        data:{
            labels: zs.map(z => z.nombre || 'Desconocida'), 
            datasets:[{
                label:'Población', 
                data: zs.map(z => cs.filter(c => c.zona?.id === z.id).length), 
                backgroundColor:'#10B981'
            }]
        }
    });
    
    const sh = {Excelente:0, Estable:0, Crítico:0}; 
    cs.forEach(c => { if(c.healthStatus && sh[c.healthStatus]!==undefined) sh[c.healthStatus]++; });
    
    chS = new Chart(ctxS, {
        type:'doughnut', 
        data:{
            labels:Object.keys(sh), 
            datasets:[{
                data:Object.values(sh), 
                backgroundColor:['#10B981','#3B82F6','#EF4444']
            }]
        }
    });
}

// ==========================================
// LÓGICA DE ZONAS (CRUD)
// ==========================================
const formZona = document.getElementById('form-zona');
if(formZona) {
    formZona.onsubmit = async (e) => {
        e.preventDefault();
        await fetch(API_Z, {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({ 
                nombre: document.getElementById('zona-nombre').value, 
                descripcion: document.getElementById('zona-desc').value 
            })
        });
        e.target.reset();
        cargarTodo();
    };
}

window.abrirEditarZona = (id, nombre, descripcion) => {
    document.getElementById('edit-zona-id').value = id;
    document.getElementById('edit-zona-nombre').value = nombre;
    document.getElementById('edit-zona-desc').value = descripcion;
    
    const modal = document.getElementById('edit-modal');
    // Para que sirva si tu modal es id="edit-modal"
    if(modal) modal.style.display = 'flex';
};

const formEditZona = document.getElementById('form-edit-zona');
if(formEditZona) {
    formEditZona.onsubmit = async (e) => {
        e.preventDefault();
        const id = document.getElementById('edit-zona-id').value;
        await fetch(`${API_Z}/${id}`, {
            method: 'PUT',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({
                nombre: document.getElementById('edit-zona-nombre').value,
                descripcion: document.getElementById('edit-zona-desc').value
            })
        });
        document.getElementById('edit-modal').style.display = 'none';
        cargarTodo();
    };
}

window.borrarZ = async (id) => {
    if (!confirm('¿Eliminar esta zona?')) return;
    await fetch(`${API_Z}/${id}`, { method: 'DELETE' });
    cargarTodo();
};

// ==========================================
// LÓGICA DE CRIATURAS (CRUD)
// ==========================================
window.abrirEditarC = async (id) => {
    const resC = await fetch(`${API_C}/${id}`);
    const c = await resC.json();

    document.getElementById('edit-id').value = c.id;
    document.getElementById('edit-nombre').value = c.name;
    document.getElementById('edit-especie').value = c.species;
    document.getElementById('edit-tamano').value = c.size;
    document.getElementById('edit-peligro').value = c.dangerLevel;
    document.getElementById('edit-salud').value = c.healthStatus;

    // Seleccionamos la zona actual en el dropdown
    const selectZ = document.getElementById('edit-zona-id');
    if (selectZ && c.zona) {
        selectZ.value = c.zona.id;
    }

    const modal = document.getElementById('edit-modal-c'); 
    // Asegúrate de que el modal de criaturas tenga un ID distinto, e.g. edit-modal-c
    if(modal) {
        modal.style.display = 'flex';
    } else {
        // Si usas el mismo modal para todo (no recomendado), lo abrimos:
        document.getElementById('edit-modal').style.display = 'flex';
    }
};

const formEditC = document.getElementById('form-edit'); 
if(formEditC) {
    formEditC.onsubmit = async (e) => {
        e.preventDefault();
        const body = {
            name: document.getElementById('edit-nombre').value,
            species: document.getElementById('edit-especie').value,
            size: parseFloat(document.getElementById('edit-tamano').value),
            dangerLevel: parseInt(document.getElementById('edit-peligro').value),
            healthStatus: document.getElementById('edit-salud').value,
            zona: { id: parseInt(document.getElementById('edit-zona-id').value) }
        };
        await fetch(`${API_C}/${document.getElementById('edit-id').value}`, {
            method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)
        });
        document.getElementById('edit-modal').style.display = 'none'; // Cierra modal de Zonas si es el mismo
        const modalC = document.getElementById('edit-modal-c');
        if(modalC) modalC.style.display = 'none'; // Cierra modal de criaturas
        
        cargarTodo();
    };
}

window.borrarC = async (id) => {
    if(!confirm('¿Eliminar esta criatura?')) return;
    await fetch(`${API_C}/${id}`, {method:'DELETE'});
    cargarTodo();
};