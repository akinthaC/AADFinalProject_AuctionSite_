handleUserGreeting();

document.getElementById("sellType").addEventListener("change", function() {
    let value = this.value;
    document.getElementById("priceSection").style.display = value !== "bidding" ? "block" : "none";
    document.getElementById("bidSection").style.display = value !== "fixed" ? "block" : "none";
});

// Initialize Rich Text Editor
tinymce.init({ selector: '#richTextEditor', height: 300, plugins: 'image lists', toolbar: 'undo redo | formatselect | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | image' });

$('#submitListning').on('click', function(e) {
    e.preventDefault(); // Prevent form submission
    let email = localStorage.getItem('email');
    console.log(email);

    console.log($("#editItemId").val())
    const formData = new FormData();
    if ($("#editItemId").val()) {
        formData.append("id", $("#editItemId").val());
    }

    formData.append("title", $("#title").val());
    formData.append("category", $("#category").val());
    formData.append("miniDesc", $("#miniDesc").val());
    formData.append("richTextEditor", tinymce.get("richTextEditor").getContent());
    formData.append("sellType", $("#sellType").val());
    formData.append("price", $("#price").val() || 0);
    formData.append("startingBid", $("#startingBid").val() || 0);
    formData.append("bidDuration", $("#bidDuration").val() || 0);
    formData.append("mainImage", $('#mainImage')[0].files[0]);
    formData.append("bidStartDate", $("#bidStartDate").val());
    formData.append("qty", $("#qty").val());
    formData.append("landDescription", $("#landDescription").val());
    formData.append("email", email);


    const otherImages = $('#otherImages')[0].files;
    for (let i = 0; i < otherImages.length; i++) {
        formData.append("otherImages", otherImages[i]);
    }

    formData.append("termsAccepted", $('#termsAccepted').is(':checked'));

    $.ajax({
        url: 'http://localhost:8080/api/listings',
        method: 'POST',
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            console.log(data);
            alert(data.message);
            clearFields();
        },
        error: function (data) {
            console.log(data);
            alert("Error saving Farmed Item.");
        }
    });
});

function clearFields() {
    $('#title').val('');
    $('#category').val('');
    $('#miniDesc').val('');
    $('#richTextEditor').val('');
    $('#sellType').val('fixed');
    $('#price').val('');
    $('#startingBid').val('');
    $('#bidDuration').val('');
    $('#mainImage').val('');
    $('#otherImages').val('');
    $('#termsAccepted').prop('checked', false);
}



// Display main image preview
document.getElementById("mainImage").addEventListener("change", function(event) {
    const preview = document.getElementById("mainImagePreview");
    preview.innerHTML = ""; // Clear previous preview
    const file = event.target.files[0];

    if (file) {
        const img = document.createElement("img");
        img.src = URL.createObjectURL(file);
        img.alt = "Main Image Preview";
        img.style.width = "200px"; // Adjust the size of the image
        img.style.height = "auto";
        preview.appendChild(img);
    }
});

// Display other images preview
document.getElementById("otherImages").addEventListener("change", function(event) {
    const preview = document.getElementById("otherImagesPreview");
    preview.innerHTML = ""; // Clear previous preview

    Array.from(event.target.files).forEach(file => {
        const img = document.createElement("img");
        img.src = URL.createObjectURL(file);
        img.alt = "Other Image Preview";
        img.style.width = "100px"; // Adjust the size of the images
        img.style.height = "100px";
        img.style.objectFit = "cover"; // Keeps the aspect ratio of images
        preview.appendChild(img);
    });
});






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

// Save Data in Local Storage with Expiration (2 Days)
function saveToLocalStorage(key, value, days) {
    const now = new Date();
    const expiryTime = now.getTime() + days * 24 * 60 * 60 * 1000;
    const data = { value: value, expiry: expiryTime };
    localStorage.setItem(key, JSON.stringify(data));
}

// Get Data from Local Storage with Expiration Check
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



