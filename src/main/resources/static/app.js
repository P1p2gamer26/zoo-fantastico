const API_Z = 'http://localhost:8080/api/zonas';
const API_C = 'http://localhost:8080/api/creatures';
let cEditT = '', cEditI = null, chP, chS;

document.addEventListener('DOMContentLoaded', () => {
    // Navegación SPA mejorada
    document.querySelectorAll('.nav-link').forEach(l => {
        l.onclick = (e) => {
            e.preventDefault();
            document.querySelectorAll('.nav-link, .view').forEach(x => x.classList.remove('active'));
            l.classList.add('active');
            const target = document.getElementById(l.dataset.target);
            if(target) target.classList.add('active');
            if(l.dataset.target === 'view-dashboard') cargarTodo();
        };
    });
    document.querySelector('.close-modal').onclick = () => document.getElementById('edit-modal').style.display = 'none';
    cargarTodo();
});

async function cargarTodo() {
    try {
        const resZ = await fetch(API_Z);
        const resC = await fetch(API_C);
        const zs = await resZ.json();
        const cs = await resC.json();
        
        actualizarTablas(zs, cs);
        actualizarStats(zs.length, cs.length);
        renderCharts(zs, cs);
    } catch (err) {
        console.error("Error cargando datos:", err);
    }
}

function actualizarTablas(zs, cs) {
    const tbZ = document.getElementById('tabla-zonas');
    const tbC = document.getElementById('tabla-criaturas');
    const sel = document.getElementById('c-zona-id');
    
    tbZ.innerHTML = ''; tbC.innerHTML = ''; sel.innerHTML = '<option value="" disabled selected>Asignar a Zona...</option>';
    
    zs.forEach(z => {
        tbZ.innerHTML += `<tr>
            <td>#${z.id}</td>
            <td><strong>${z.nombre}</strong></td>
            <td>${z.descripcion}</td>
            <td>
                <button class="btn-edit" onclick="abrirE('z',${z.id},'${z.nombre}','${z.descripcion}')">✏️</button>
                <button class="btn-delete" onclick="borrarZona(${z.id})">🗑️</button>
            </td>
        </tr>`;
        sel.innerHTML += `<option value="${z.id}">${z.nombre}</option>`;
    });

    cs.forEach(c => {
        tbC.innerHTML += `<tr>
            <td><strong>${c.name}</strong><br><small>${c.species}</small></td>
            <td>${c.dangerLevel}/10 | ${c.size}m</td>
            <td><span class="badge">${c.healthStatus}</span></td>
            <td>${c.zona ? c.zona.nombre : 'Sin zona'}</td>
            <td>
                <button class="btn-edit" onclick="abrirE('c',${c.id},'${c.name}','${c.species}',${c.dangerLevel},'${c.healthStatus}')">✏️</button>
                <button class="btn-delete" onclick="borrarCriatura(${c.id})">🗑️</button>
            </td>
        </tr>`;
    });
}

// --- FUNCIONES DE BORRADO CORREGIDAS ---
window.borrarZona = async (id) => {
    if(!confirm('¿Estás seguro? Si la zona tiene criaturas, no podrás borrarla hasta moverlas o eliminarlas.')) return;
    
    try {
        const res = await fetch(`${API_Z}/${id}`, { method: 'DELETE' });
        if(res.ok) {
            showToast("Zona eliminada con éxito");
            cargarTodo();
        } else {
            const errorData = await res.text();
            showToast("No se pudo borrar: Asegúrate de que la zona esté vacía", "error");
            console.error("Error del servidor:", errorData);
        }
    } catch (err) {
        showToast("Error de conexión", "error");
    }
};

window.borrarCriatura = async (id) => {
    if(!confirm('¿Eliminar esta criatura?')) return;
    await fetch(`${API_C}/${id}`, { method: 'DELETE' });
    showToast("Criatura eliminada");
    cargarTodo();
};

// --- RESTO DE LÓGICA (POST/PUT) ---
function abrirE(t, id, n, x, d, h) {
    cEditT = t; cEditI = id;
    const modal = document.getElementById('edit-modal');
    document.getElementById('modal-title').innerText = t==='z'?'Editar Zona':'Editar Criatura';
    document.getElementById('edit-fields').innerHTML = t==='z' 
        ? `<input id="en" value="${n}" placeholder="Nombre"><input id="ed" value="${x}" placeholder="Descripción">`
        : `<input id="en" value="${n}"><input id="es" value="${x}"><input id="ep" type="number" value="${d}"><input id="eh" value="${h}">`;
    modal.style.display = 'block';
}

document.getElementById('form-edit').onsubmit = async (e) => {
    e.preventDefault();
    const isZ = cEditT === 'z';
    const body = isZ ? {nombre:en.value, descripcion:ed.value} : {name:en.value, species:es.value, dangerLevel:parseInt(ep.value), healthStatus:eh.value};
    
    await fetch((isZ?API_Z:API_C)+'/'+cEditI, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    });
    document.getElementById('edit-modal').style.display = 'none';
    cargarTodo();
};

document.getElementById('form-zona').onsubmit = async (e) => {
    e.preventDefault();
    const nombre = document.getElementById('zona-nombre').value;
    const descripcion = document.getElementById('zona-desc').value;
    await fetch(API_Z, {
        method:'POST', 
        headers:{'Content-Type':'application/json'}, 
        body:JSON.stringify({nombre, descripcion})
    });
    document.getElementById('form-zona').reset();
    cargarTodo();
};

document.getElementById('form-criatura').onsubmit = async (e) => {
    e.preventDefault();
    const body = {
        name: document.getElementById('c-nombre').value,
        species: document.getElementById('c-especie').value,
        size: parseFloat(document.getElementById('c-tamano').value),
        dangerLevel: parseInt(document.getElementById('c-peligro').value),
        healthStatus: document.getElementById('c-salud').value,
        zona: {id: parseInt(document.getElementById('c-zona-id').value)}
    };
    await fetch(API_C, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(body)});
    document.getElementById('form-criatura').reset();
    cargarTodo();
};

function actualizarStats(z, c) {
    document.getElementById('stat-zonas').innerText = z;
    document.getElementById('stat-criaturas').innerText = c;
}

function showToast(m, type="success") {
    const c = document.getElementById('toast-container');
    const t = document.createElement('div'); 
    t.className = 'toast'; 
    t.style.borderLeftColor = type === "error" ? "#EF4444" : "#10B981";
    t.innerText = m;
    c.appendChild(t); 
    setTimeout(() => t.remove(), 3000);
}

function renderCharts(zs, cs) {
    const ctxP = document.getElementById('chart-poblacion');
    const ctxS = document.getElementById('chart-salud');
    if(!ctxP || !ctxS) return;
    if(chP) chP.destroy(); if(chS) chS.destroy();
    chP = new Chart(ctxP, {type:'bar', data:{labels:zs.map(z=>z.nombre), datasets:[{label:'Población', data:zs.map(z=>cs.filter(c=>c.zona?.id===z.id).length), backgroundColor:'#10B981'}]}});
    const sh = {Excelente:0, Estable:0, Crítico:0}; cs.forEach(c => { if(sh[c.healthStatus]!==undefined) sh[c.healthStatus]++; });
    chS = new Chart(ctxS, {type:'doughnut', data:{labels:Object.keys(sh), datasets:[{data:Object.values(sh), backgroundColor:['#10B981','#3B82F6','#EF4444']}]}});
}
