const API_Z = '/api/zonas';
const API_C = '/api/creatures';
let chP, chS;

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.nav-link').forEach(l => {
        l.onclick = (e) => {
            e.preventDefault();
            document.querySelectorAll('.nav-link, .view').forEach(x => x.classList.remove('active'));
            l.classList.add('active');
            const target = document.getElementById(l.dataset.target);
            if(target) target.classList.add('active');
            cargarTodo();
        };
    });
    document.querySelector('.close-modal').onclick = () => document.getElementById('edit-modal').style.display = 'none';
    cargarTodo();
});

async function cargarTodo() {
    try {
        const [resZ, resC] = await Promise.all([fetch(API_Z), fetch(API_C)]);
        const zs = await resZ.json();
        const cs = await resC.json();
        actualizarTablas(zs, cs);
        actualizarStats(zs.length, cs.length);
        renderCharts(zs, cs);
    } catch (err) { console.error("Error:", err); }
}

function actualizarTablas(zs, cs) {
    const tbZ = document.getElementById('tabla-zonas');
    const tbC = document.getElementById('tabla-criaturas');
    const selC = document.getElementById('c-zona-id');
    tbZ.innerHTML = ''; tbC.innerHTML = ''; 
    selC.innerHTML = zs.map(z => `<option value="${z.id}">${z.nombre}</option>`).join('');

    zs.forEach(z => {
        tbZ.innerHTML += `<tr><td>#${z.id}</td><td><strong>${z.nombre}</strong></td><td>${z.descripcion}</td>
            <td><button class="btn-edit" onclick="borrarZ(${z.id})">🗑</button></td></tr>`;
    });

    cs.forEach(c => {
        tbC.innerHTML += `<tr>
            <td><strong>${c.name}</strong><br><small>${c.species}</small></td>
            <td>P:${c.dangerLevel} | T:${c.size}m</td>
            <td><span class="badge">${c.healthStatus}</span></td>
            <td>${c.zona ? c.zona.nombre : 'Sin zona'}</td>
            <td>
                <button class="btn-edit" onclick="abrirEditar(${c.id})">✏</button>
                <button class="btn-delete" onclick="borrarC(${c.id})">🗑</button>
            </td>
        </tr>`;
    });
}

window.abrirEditar = async (id) => {
    const [resC, resZ] = await Promise.all([fetch(`${API_C}/${id}`), fetch(API_Z)]);
    const c = await resC.json();
    const zs = await resZ.json();

    document.getElementById('edit-id').value = c.id;
    document.getElementById('edit-nombre').value = c.name;
    document.getElementById('edit-especie').value = c.species;
    document.getElementById('edit-tamano').value = c.size;
    document.getElementById('edit-peligro').value = c.dangerLevel;
    document.getElementById('edit-salud').value = c.healthStatus;

    const selectZ = document.getElementById('edit-zona-id');
    selectZ.innerHTML = zs.map(z => 
        `<option value="${z.id}" ${c.zona && c.zona.id === z.id ? 'selected' : ''}>${z.nombre}</option>`
    ).join('');

    document.getElementById('edit-modal').style.display = 'block';
};

document.getElementById('form-edit').onsubmit = async (e) => {
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
    document.getElementById('edit-modal').style.display = 'none';
    cargarTodo();
};

window.borrarC = (id) => fetch(`${API_C}/${id}`, {method:'DELETE'}).then(cargarTodo);
window.borrarZ = (id) => fetch(`${API_Z}/${id}`, {method:'DELETE'}).then(cargarTodo);

function actualizarStats(z, c) {
    document.getElementById('stat-zonas').innerText = z;
    document.getElementById('stat-criaturas').innerText = c;
}

function renderCharts(zs, cs) {
    const ctxP = document.getElementById('chart-poblacion');
    const ctxS = document.getElementById('chart-salud');
    if(!ctxP || !ctxS) return;
    if(chP) chP.destroy(); if(chS) chS.destroy();
    chP = new Chart(ctxP, {type:'bar', data:{labels:zs.map(z=>z.nombre), datasets:[{label:'Población', data:zs.map(z=>cs.filter(c=>c.zona?.id===z.id).length), backgroundColor:'#10B981'}]}});
    const sh = {Excelente:0, Estable:0, Crítico:0}; 
    cs.forEach(c => { if(sh[c.healthStatus]!==undefined) sh[c.healthStatus]++; });
    chS = new Chart(ctxS, {type:'doughnut', data:{labels:Object.keys(sh), datasets:[{data:Object.values(sh), backgroundColor:['#10B981','#3B82F6','#EF4444']}]}});
}
