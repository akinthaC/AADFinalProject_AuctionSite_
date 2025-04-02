function handleUserGreeting() {
    const username = getFromLocalStorage('firstName')
    const email = localStorage.getItem('email')
    const role = getFromLocalStorage('role')// Assuming role is stored

    let userGreeting = document.getElementById("userGreeting");

    console.log("Username:", username);
    console.log("Email:", email);
    console.log("Role:", role);

    if (username) {
        userGreeting.innerHTML = `Hi, ${username} ⏷`; // Dropdown arrow
        userGreeting.style.display = "block";
        userGreeting.classList.add("dropdown-trigger"); // Add hover effect
        setupUserDropdown(role); // Set up dropdown menu
    } else if (email) {
        showLoginExpiredModal();
    } else {
        userGreeting.style.display = "none";
    }

    // Remove SIGNUP and REGISTER links if user is logged in
    if (username) {
        document.querySelectorAll(".nav-item").forEach(item => {
            let link = item.querySelector("a");
            if (link && (link.innerText.includes("SIGNUP") || link.innerText.includes("REGISTER"))) {
                item.remove();
            }
        });
    }
}
function getFromLocalStorage(key) {
    const data = localStorage.getItem(key);
    if (!data) return null;

    const parsedData = JSON.parse(data);
    const now = new Date().getTime();

    if (now > parsedData.expiry) {
        localStorage.removeItem(key);
        return null;
    }
    return parsedData.value;
}

// Show Login Expired Modal
function showLoginExpiredModal() {
    let modal = document.createElement("div");
    modal.innerHTML = `
        <div class="modal-glass">
            <h2>Session Expired</h2>
            <p>Your login has expired. Please log in again.</p>
            <button class="btn-modern" onclick="redirectToLogin()">Login</button>
        </div>
    `;
    modal.classList.add("modal-container");
    document.body.appendChild(modal);
}

// Redirect to Login Page
function redirectToLogin() {
    localStorage.clear();
    window.location.href = "../SingUpRegister.html";
}

// Setup Modern Dropdown for User Profile
function setupUserDropdown(role) {
    let dropdownMenu = document.createElement("div");
    dropdownMenu.classList.add("user-dropdown-menu");
    dropdownMenu.style.display = "none"; // Initially hidden

    let profileOption = `<div class="user-dropdown-item">👤 Profile</div>`;
    let logoutOption = `<div class="user-dropdown-item logout-option">🚪 Logout</div>`;
    let cartOption = `<div class="user-dropdown-item">🛒 Add to Cart</div>`;
    let watchlistOption = `<div class="user-dropdown-item">📌 Watchlist</div>`;
    let notificationsOption = `<div class="user-dropdown-item">🔔 Notifications</div>`;

    let sellerOption = `<div class="user-dropdown-item">📜 Your Listings</div>`;
    let createListingOption = `<div class="user-dropdown-item">➕ Create Listing</div>`; // New option
    let buyerOption = `<div class="user-dropdown-item">📢 Start Selling</div>`;

    dropdownMenu.innerHTML = profileOption + notificationsOption + cartOption + watchlistOption;

    if (role === "seller") {
        dropdownMenu.innerHTML += sellerOption + createListingOption; // Add both options
    } else if (role === "buyer") {
        dropdownMenu.innerHTML += buyerOption;
    }

    dropdownMenu.innerHTML += logoutOption;

    document.getElementById("userGreeting").appendChild(dropdownMenu);

    // Show dropdown on click
    document.getElementById("userGreeting").addEventListener("click", function (e) {
        e.stopPropagation(); // Prevent click from propagating to the body
        dropdownMenu.style.display = (dropdownMenu.style.display === "block") ? "none" : "block";
    });

    // Close dropdown if clicked outside
    window.addEventListener("click", function (event) {
        if (!document.getElementById("userGreeting").contains(event.target) && !dropdownMenu.contains(event.target)) {
            dropdownMenu.style.display = "none";
        }
    });

    // Logout functionality
    document.querySelector(".logout-option").addEventListener("click", function () {
        localStorage.clear();
        window.location.href = "login.html";
    });
}

handleUserGreeting()