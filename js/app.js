// ===== SKILL SWAP HUB v2 - app.js =====

const API = 'http://localhost:8080/api/users';
const AVAIL_API = 'http://localhost:8080/api/availability';

function showAlert(id, msg, type = 'error') {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = msg;
  el.className = `alert alert-${type}`;
  el.style.display = 'block';
  setTimeout(() => el.style.display = 'none', 4000);
}

function getLoggedUser() {
  const u = localStorage.getItem('loggedUser');
  return u ? JSON.parse(u) : null;
}

function requireLogin() {
  const u = getLoggedUser();
  if (!u) { window.location.href = 'login.html'; return null; }
  return u;
}

function avatarColors(name) {
  const colors = [
    ['#6c63ff','#a084ff'], ['#ff6584','#ff8fab'],
    ['#3ddc84','#38f9d7'], ['#ffd166','#ff9f1c'],
    ['#06d6a0','#1b9aaa'], ['#ef476f','#f77f00']
  ];
  return colors[name.charCodeAt(0) % colors.length];
}

function makeAvatar(name, size = 46) {
  const [c1, c2] = avatarColors(name);
  return `<div class="user-avatar" style="width:${size}px;height:${size}px;font-size:${size*0.38}px;background:linear-gradient(135deg,${c1},${c2})">${name.charAt(0).toUpperCase()}</div>`;
}

// ===== REGISTER =====
async function registerUser(e) {
  e.preventDefault();
  const name     = document.getElementById('reg-name').value.trim();
  const email    = document.getElementById('reg-email').value.trim();
  const password = document.getElementById('reg-password').value;
  if (!name || !email || !password) return showAlert('reg-alert', 'Please fill all fields.');
  try {
    const res  = await fetch(`${API}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    });
    const data = await res.json();
    if (res.ok) {
      showAlert('reg-alert', 'Registered! Redirecting...', 'success');
      setTimeout(() => window.location.href = 'login.html', 1400);
    } else {
      showAlert('reg-alert', data.error || 'Registration failed.');
    }
  } catch { showAlert('reg-alert', 'Cannot connect to server.'); }
}

// ===== LOGIN =====
async function loginUser(e) {
  e.preventDefault();
  const email    = document.getElementById('login-email').value.trim();
  const password = document.getElementById('login-password').value;
  if (!email || !password) return showAlert('login-alert', 'Please fill all fields.');
  try {
    const res  = await fetch(`${API}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    const data = await res.json();
    if (res.ok) {
      localStorage.setItem('loggedUser', JSON.stringify(data.user));
      showAlert('login-alert', 'Login successful!', 'success');
      setTimeout(() => window.location.href = 'dashboard.html', 1200);
    } else {
      showAlert('login-alert', data.error || 'Login failed.');
    }
  } catch { showAlert('login-alert', 'Cannot connect to server.'); }
}

// ===== LOGOUT =====
function logout() {
  localStorage.removeItem('loggedUser');
  window.location.href = 'index.html';
}

// ===== LOAD ALL USERS =====
async function loadUsers(keyword = '') {
  const grid = document.getElementById('users-grid');
  if (!grid) return;
  grid.innerHTML = `<div class="loading"><div class="spinner"></div>Loading users...</div>`;
  try {
    const url   = keyword ? `${API}/search?keyword=${encodeURIComponent(keyword)}` : API;
    const res   = await fetch(url);
    const users = await res.json();
    updateStats(users);
    const me = getLoggedUser();
    if (users.length === 0) {
      grid.innerHTML = `<div class="empty-state" style="grid-column:1/-1">
        <div class="empty-icon">🔍</div><h3>No users found</h3>
        <p>${keyword ? 'No results for "' + keyword + '"' : 'No users yet.'}</p></div>`;
      return;
    }
    grid.innerHTML = users.map(u => {
      const teachTags = u.skills
        ? u.skills.split(',').map(s => `<span class="tag tag-teach">${s.trim()}</span>`).join('')
        : '<span class="tag-empty">No skills listed</span>';
      const learnTags = u.wantToLearn
        ? u.wantToLearn.split(',').map(s => `<span class="tag tag-learn">${s.trim()}</span>`).join('')
        : '<span class="tag-empty">Not specified</span>';
      const isMe = me && me.id === u.id;
      return `
        <div class="user-card" id="card-${u.id}">
          <div class="user-card-top">
            ${makeAvatar(u.name)}
            <div class="user-info">
              <h3>${u.name} ${isMe ? '<span style="font-size:0.7rem;color:var(--accent);background:rgba(108,99,255,0.15);padding:0.1rem 0.5rem;border-radius:50px;">You</span>' : ''}</h3>
              <div class="email">${u.email}</div>
              ${u.location ? '<div class="location">📍 ' + u.location + '</div>' : ''}
            </div>
          </div>
          ${u.bio ? '<p class="user-bio">' + u.bio + '</p>' : ''}
          <div class="skill-section">
            <div class="skill-section-label">🎓 Can Teach</div>
            <div class="skill-tags">${teachTags}</div>
          </div>
          <div class="skill-section">
            <div class="skill-section-label">📚 Wants to Learn</div>
            <div class="skill-tags">${learnTags}</div>
          </div>
          <div class="user-card-actions">
            ${!isMe
              ? `<button class="btn btn-success btn-sm" onclick="openChat(${u.id},'${u.name}')">💬 Chat</button>`
              : `<button class="btn btn-ghost btn-sm" onclick="switchTab('profile')">✏️ Edit Profile</button>`
            }
            <button class="btn btn-danger btn-sm" onclick="deleteUser(${u.id})">🗑 Delete</button>
          </div>
        </div>`;
    }).join('');
  } catch {
    grid.innerHTML = `<div class="loading">❌ Failed to load. Is Spring Boot running?</div>`;
  }
}

// ===== STATS =====
function updateStats(users) {
  const totalEl  = document.getElementById('stat-total');
  const skillsEl = document.getElementById('stat-skills');
  if (totalEl)  totalEl.textContent  = users.length;
  if (skillsEl) {
    const all = users.flatMap(u => u.skills ? u.skills.split(',') : []);
    skillsEl.textContent = [...new Set(all.map(s => s.trim().toLowerCase()))].length;
  }
}

// ===== SEARCH =====
let searchTimer;
function handleSearch(e) {
  clearTimeout(searchTimer);
  searchTimer = setTimeout(() => loadUsers(e.target.value.trim()), 350);
}

// ===== DELETE USER =====
async function deleteUser(id) {
  if (!confirm('Delete this user from the database?')) return;
  try {
    const res = await fetch(`${API}/${id}`, { method: 'DELETE' });
    if (res.ok) {
      document.getElementById('card-' + id)?.remove();
      const me = getLoggedUser();
      if (me && me.id === id) logout();
    }
  } catch { alert('Error deleting user.'); }
}

// ===== DELETE ALL =====
async function deleteAllUsers() {
  if (!confirm('Delete ALL users from database?')) return;
  if (!confirm('This cannot be undone!')) return;
  try {
    const res  = await fetch(`${API}/all`, { method: 'DELETE' });
    const data = await res.json();
    if (res.ok) {
      alert('Done! ' + data.message);
      localStorage.removeItem('loggedUser');
      window.location.href = 'index.html';
    }
  } catch { alert('Error. Is server running?'); }
}

// ===== SAVE PROFILE =====
async function saveProfile(e) {
  e.preventDefault();
  const user = requireLogin();
  if (!user) return;
  const updated = {
    name:        document.getElementById('p-name').value.trim(),
    bio:         document.getElementById('p-bio').value.trim(),
    skills:      document.getElementById('p-skills').value.trim(),
    wantToLearn: document.getElementById('p-learn').value.trim(),
    location:    document.getElementById('p-location').value.trim()
  };
  try {
    const res  = await fetch(`${API}/${user.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updated)
    });
    const data = await res.json();
    if (res.ok) {
      localStorage.setItem('loggedUser', JSON.stringify(data.user));
      showAlert('profile-alert', 'Profile updated!', 'success');
      renderProfileHeader(data.user);
    } else {
      showAlert('profile-alert', data.error || 'Update failed.');
    }
  } catch { showAlert('profile-alert', 'Cannot connect to server.'); }
}

// ===== PROFILE HEADER =====
function renderProfileHeader(user) {
  const el = document.getElementById('profile-header');
  if (!el) return;
  el.innerHTML = makeAvatar(user.name, 72) +
    '<div class="profile-header-info">' +
      '<h2>' + user.name + '</h2>' +
      '<p>' + user.email + '</p>' +
      (user.location ? '<p>📍 ' + user.location + '</p>' : '') +
    '</div>';
}

// ===== SIDEBAR NAV =====
function switchTab(tab) {

    document.querySelectorAll('.tab-panel')
        .forEach(p => p.classList.remove('active'));

    document.querySelectorAll('.sidebar-item')
        .forEach(i => i.classList.remove('active'));

    const panel = document.getElementById('tab-' + tab);
    if (panel) panel.classList.add('active');

    const item = document.querySelector('[data-tab="' + tab + '"]');
    if (item) item.classList.add('active');

    if (tab === 'browse') {
        loadUsers();
    }

    if (tab === 'availability') {
        loadAvailability();
    }
}
async function loadAvailability() {

    const user = getLoggedUser();

    if (!user) return;

    try {

        const res = await fetch(
            `http://localhost:8080/api/availability/${user.id}`
        );

        const slots = await res.json();

        const list = document.getElementById('availability-list');

        if (!slots.length) {
            list.innerHTML = '<p>No availability added yet.</p>';
            return;
        }

        list.innerHTML = slots.map(slot => `
            <div class="user-card">
                <strong>${slot.day}</strong>
                <br>
                ${slot.startTime} - ${slot.endTime}

                <button
                    class="btn btn-danger btn-sm"
                    onclick="deleteAvailability(${slot.id})">
                    Delete
                </button>
            </div>
        `).join('');

    } catch (e) {
        console.error(e);
    }
}

async function addAvailability() {

    const user = getLoggedUser();

    const slot = {
        day: document.getElementById('avail-day').value,
        startTime: document.getElementById('avail-start').value,
        endTime: document.getElementById('avail-end').value
    };

    try {

        await fetch(
            `http://localhost:8080/api/availability/${user.id}`,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify([slot])
            }
        );

        loadAvailability();
        alert("Availability saved successfully!");

    } catch (e) {
        console.error(e);
        alert('Failed to save');
    }
}

async function deleteAvailability(id) {

    try {

        await fetch(
            `http://localhost:8080/api/availability/${id}`,
            {
                method: 'DELETE'
            }
        );

        loadAvailability();

    } catch (e) {
        alert('Delete failed');
    }
}
async function loadUserAvailability(userId) {

    const box = document.getElementById('chat-availability');

    if (!box) return;

    try {

        const res = await fetch(
            `http://localhost:8080/api/availability/${userId}`
        );

        const slots = await res.json();

        if (!slots.length) {

            box.innerHTML = `
                <div class="availability-chat">
                    <h4>📅 Availability</h4>
                    <p>No availability added yet.</p>
                </div>
            `;
            return;
        }

        box.innerHTML = `
            <div class="availability-chat">
                <h4>📅 Availability</h4>

                ${slots.map(slot => `
                    <div class="availability-slot">
                        ${slot.day} :
                        ${slot.startTime}
                        -
                        ${slot.endTime}
                    </div>
                `).join('')}

            </div>
        `;

    } catch (e) {

        console.error(e);

        box.innerHTML = `
            <div class="availability-chat">
                <h4>📅 Availability</h4>
                <p>Unable to load availability.</p>
            </div>
        `;
    }
}

// ===== PAGE INIT =====
document.addEventListener('DOMContentLoaded', () => {
  const regForm = document.getElementById('register-form');
  if (regForm) regForm.addEventListener('submit', registerUser);

  const loginForm = document.getElementById('login-form');
  if (loginForm) loginForm.addEventListener('submit', loginUser);

  if (document.getElementById('tab-browse')) {
    const user = requireLogin();
    if (!user) return;

    const navName   = document.getElementById('nav-username');
    const navAvatar = document.getElementById('nav-avatar');
    if (navName)   navName.textContent   = user.name;
    if (navAvatar) navAvatar.textContent = user.name.charAt(0).toUpperCase();

    const welcomeEl = document.getElementById('welcome-name-browse');
    if (welcomeEl) welcomeEl.textContent = user.name + '!';

    const pName   = document.getElementById('p-name');
    const pBio    = document.getElementById('p-bio');
    const pSkills = document.getElementById('p-skills');
    const pLearn  = document.getElementById('p-learn');
    const pLoc    = document.getElementById('p-location');
    if (pName)   pName.value   = user.name       || '';
    if (pBio)    pBio.value    = user.bio         || '';
    if (pSkills) pSkills.value = user.skills      || '';
    if (pLearn)  pLearn.value  = user.wantToLearn || '';
    if (pLoc)    pLoc.value    = user.location    || '';

    renderProfileHeader(user);

    const profForm = document.getElementById('profile-form');
    if (profForm) profForm.addEventListener('submit', saveProfile);

    const searchEl = document.getElementById('search-input');
    if (searchEl) searchEl.addEventListener('input', handleSearch);

    document.querySelectorAll('.sidebar-item').forEach(item => {
      item.addEventListener('click', () => switchTab(item.dataset.tab));
    });

    switchTab('browse');
  }
});