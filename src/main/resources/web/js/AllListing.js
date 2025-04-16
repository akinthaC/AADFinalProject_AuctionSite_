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

    userGreeting.addEventListener("click", function () {
        hideNotificationDot();
    });
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

function hideNotificationDot() {
    const dot = document.getElementById("notificationDot");
    if (dot) {
        console.log("Hiding notification dot");
        dot.style.display = "none";  // Hides the notification dot
    } else {
        console.log("Notification dot not found.");
    }
}
// Setup Modern Dropdown for User Profile
function setupUserDropdown(role) {
    let dropdownMenu = document.createElement("div");
    dropdownMenu.classList.add("user-dropdown-menu");
    dropdownMenu.style.display = "none"; // Initially hidden

    let profileOption = `<div id="profile" class="user-dropdown-item">👤 Profile</div>`;
    let logoutOption = `<div class="user-dropdown-item logout-option">🚪 Logout</div>`;
    let cartOption = `<div class="user-dropdown-item">🛒 Add to Cart
<span id="cartCount" style="background-color: red; color: white; font-size: 12px; font-weight: bold; padding: 2px 6px; border-radius: 12px; margin-left: 8px;">0</span></div>`;
    let watchlistOption = `<div class="user-dropdown-item">📌 Watchlist
<span id="WatchCount" style="background-color: red; color: white; font-size: 12px; font-weight: bold; padding: 2px 6px; border-radius: 12px; margin-left: 8px;">0</span></div>`;
    let notificationsOption = `<div class="user-dropdown-item">🔔 Notifications</div>`;
    let YouAreBuyOption = `<div class="user-dropdown-item">📜 You buy/Bid Items</div>`;
    let YouAreOrdersOption = `<div class="user-dropdown-item">📜 You're Orders</div>`;

    let sellerOption = `<div id="showListing" class="user-dropdown-item">📜 Your Listings</div>`;
    let createListingOption = `<div id="createListing" class="user-dropdown-item">➕ Create Listing</div>`;
    let buyerOption = `<div id="startSelling" class="user-dropdown-item">📢 Start Selling</div>`;

    dropdownMenu.innerHTML = profileOption + notificationsOption + cartOption + watchlistOption+YouAreBuyOption;

    if (role === "seller") {
        dropdownMenu.innerHTML += sellerOption + createListingOption +YouAreOrdersOption; // Add both options
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

    dropdownMenu.querySelector(".user-dropdown-item:nth-child(3)").addEventListener("click", function () {
        document.getElementById("cartModal").style.display = "flex";
        loadCartItems()
        showModal()// Load items into modal
    });

    dropdownMenu.querySelector(".user-dropdown-item:nth-child(4)").addEventListener("click", function () {
        document.getElementById("watchlistModal").style.display = "flex";
        loadWatchedItems()
        showModalWatched()// Load items into modal
    });

    dropdownMenu.querySelector(".user-dropdown-item:nth-child(5)").addEventListener("click", function () {
        document.getElementById('youBuyModal').style.display = 'block';
        loadBuyerItems('bids'); // load default tab

    });
    dropdownMenu.querySelector(".user-dropdown-item:nth-child(8)").addEventListener("click", function () {
        document.getElementById('youOrderModal').style.display = 'block';
        loadBuyerItems1('paid'); // load default tab

    });

    dropdownMenu.querySelector(".user-dropdown-item:nth-child(6)").addEventListener("click", function () {
        const selected = localStorage.getItem('selectedCategory');
        console.log("Selected Category:", selected);

        // Redirect to the listing page based on selected category
        // Redirect based on category
        if (selected === "Farmed") {
            window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/showListning.html?_ijt=uiiintlm9oe1t9726sj4b124lj&_ij_reload=RELOAD_ON_SAVE';
        } else if (selected === "Lands") {
            window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/ShowListingLands.html?_ijt=uiiintlm9oe1t9726sj4b124lj&_ij_reload=RELOAD_ON_SAVE';
        } else if (selected === "Vehicles") {
            window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/ShowListingVehicle.html?_ijt=uiiintlm9oe1t9726sj4b124lj&_ij_reload=RELOAD_ON_SAVE';
        } else {
            // Fallback/default
            window.location.href = '../FarmedItem.html';
        }
    });


    dropdownMenu.querySelector(".user-dropdown-item:nth-child(7)").addEventListener("click", function () {
        const selected = localStorage.getItem('selectedCategory');
        console.log("Selected Category:", selected);

            // Redirect to the listing page based on selected category
            // Redirect based on category
            if (selected === "Farmed") {
                window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/FarmedItem.html?_ijt=4fgosg79n0nslcholq7d45ur7s&_ij_reload=RELOAD_ON_SAVE';
            } else if (selected === "Lands") {
                window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/LandsSell.html?_ijt=f5gjdoojgp7r1osfp9oae96185&_ij_reload=RELOAD_ON_SAVE';
            } else if (selected === "Vehicles") {
                window.location.href = 'http://localhost:63342/AADFinalProject_AuctionSite_/src/main/resources/web/vehicleListing.html?_ijt=3n5eubpkr4tmhvs0p3i2b13pad&_ij_reload=RELOAD_ON_SAVE';
            } else {
                // Fallback/default
                window.location.href = '../FarmedItem.html';
            }



    });


    dropdownMenu.querySelector(".user-dropdown-item:nth-child(1)").addEventListener("click", function () {
        if (!document.querySelector("#profileModal")) {
            document.body.insertAdjacentHTML("beforeend", `
            <div id="profileModal" class="modal-overlay">
                <div class="modal-content">
                    <span class="close-btn">&times;</span>
                    <h2>👤 User Profile</h2>
                    <div class="modal-body">
                        <p><strong>Full Name:</strong> <span id="userFullName">Loading...</span></p>
                        <p><strong>Type:</strong> <span id="userType">Loading...</span></p>
                        <p><strong>Address:</strong> <span id="userAddress">Loading...</span></p>
                        <p><strong>Ratings:</strong> <span id="userRating">⭐ 2.3</span></p>
                    </div>
                </div>
            </div>
        `);

            // Show modal
            const modal = document.querySelector("#profileModal");
            modal.style.display = "flex";

            // Close modal with close button
            modal.querySelector(".close-btn").addEventListener("click", function () {
                modal.style.display = "none";
            });

            // Optional: click outside modal to close
            modal.addEventListener("click", function (e) {
                if (e.target === modal) {
                    modal.style.display = "none";
                }
            });

            // Get token and email from localStorage
            const token = localStorage.getItem("token");
            const email = localStorage.getItem("email");

            // Make sure both token and email are available
            if (token && email) {
                $.ajax({
                    url: `http://localhost:8080/api/v1/user/getUserByEmail?email=${encodeURIComponent(email)}`,
                    method: 'GET',
                    contentType: 'application/json',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function (response) {
                        if (response && response.data) {
                            document.getElementById("userFullName").innerText = `${response.data.firstName || ""} ${response.data.lastName || ""}`.trim();
                            document.getElementById("userType").innerText = response.data.role || "N/A";
                            document.getElementById("userAddress").innerText = response.data.addressLine2 || "N/A";
                        } else {
                            document.getElementById("userFullName").innerText = "Error loading data";
                            document.getElementById("userType").innerText = "N/A";
                            document.getElementById("userAddress").innerText = "N/A";
                        }
                    },
                    error: function (err) {
                        console.error("Error fetching user profile:", err);
                        document.getElementById("userFullName").innerText = "Error loading data";
                        document.getElementById("userType").innerText = "N/A";
                        document.getElementById("userAddress").innerText = "N/A";
                    }
                });
            } else {
                document.getElementById("userFullName").innerText = "User not logged in";
            }
        } else {
            // If already created, just display it again
            document.querySelector("#profileModal").style.display = "flex";
        }
    });

}



handleUserGreeting()


loadCartItems();
loadWatchedItems();



// Open modal when clicking "You buy/Bid Items"


// Close modal
document.querySelector('.close-modal')?.addEventListener('click', () => {
    document.getElementById('youBuyModal').style.display = 'none';
});

document.querySelector('.close-modal')?.addEventListener('click', () => {
    document.getElementById('youOrderModal').style.display = 'none';
});

// Switch tabs
document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        // Remove the 'active' class from all buttons
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        // Hide all modal-tab-content elements (if you use them)
        document.querySelectorAll('.modal-tab-content').forEach(c => {
            if (c) {
                c.style.display = 'none';
            }
        });

        // Show the selected tab content by ID if it exists
        const tabContent = document.getElementById(btn.dataset.tab);
        if (tabContent) {
            tabContent.style.display = 'block';
        }


        loadBuyerItems(btn.dataset.tab);
        loadBuyerItems1(btn.dataset.tab);
    });
});





// Load buyer items using AJAX
// Load buyer items using AJAX
function loadBuyerItems(type) {
    const content = $('#buyerItemsContent');
    content.html('<p>Loading...</p>'); // Show loading while fetching

    let url = '';
    let email = localStorage.getItem("email"); // Get user email

    switch (type) {
        case 'bids':
            url = `http://localhost:8080/api/v1/PlaceOrder/getAllWin/${email}`;
            break;
        case 'pending':
            loadPendingItems(); // Handle this separately
            return; // Return early to avoid unnecessary AJAX call
        case 'delivered':
            loadDiliverdItems();
            break;
        default:
            content.html('<p>Invalid tab type</p>');
            return;
    }

    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (!data || !data.data || !Array.isArray(data.data.bids) || data.data.bids.length === 0) {
                content.html('<p>No items found.</p>');
                return;
            }

            console.log("Response:", data);

            const bids = data.data.bids;

            let html = '<div class="buyer-item-list">';
            bids.forEach(item => {
                html += `
                    <div class="buyer-item-card">
                        <div class="item-image-container">
                            <img src="/static/uploads/${item.mainImage}" alt="Image of ${item.listingName}" class="item-main-image" />
                        </div>
                        <h4 class="item-name">${item.listingName}</h4>
                        <p class="item-price"><strong>Price:</strong> ${item.price} LKR</p>
                        <p class="item-quantity"><strong>Quantity Available:</strong> ${item.qty}</p>
                        <p class="payment-status"><strong>Days Remaining for Payment:</strong> ${item.dateHave} days</p>
                        ${generateActionButton(type, item)}
                    </div>
                `;
            });
            html += '</div>';
            content.html(html);
        },
        error: function () {
            content.html('<p style="color:red;">Failed to load data.</p>');
        }
    });
}

// Load pending items (shipped or not)
function loadPendingItems() {
    const content = $('#buyerItemsContent');
    content.html('<p>Loading...</p>'); // Show loading while fetching

    let email = localStorage.getItem("email"); // Get user email

    $.ajax({
        url: `http://localhost:8080/api/v1/PlaceOrder/getPending&Shipped/${email}`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (!data || !data.data || !Array.isArray(data.data.pending) || data.data.pending.length === 0) {
                content.html('<p>No items found.</p>');
                return;
            }

            console.log("Response:", data);

            const bids = data.data.pending;

            let html = '<div class="buyer-item-list">';
            bids.forEach(item => {
                let deliveryStatusText = "";
                let remainingDaysText = "";
                let remainingDaysTag = ""; // Empty string to conditionally include the tag
                let actionButtonHtml = ""; // To store button if status is 'shipped'

                // Check the delivery status
                if (item.status === "Pending") {
                    deliveryStatusText = "Not yet shipped";
                } else if (item.status === "shipped") {
                    deliveryStatusText = "Shipped";
                    remainingDaysText = item.dateHave;
                    remainingDaysTag = `<p class="payment-status"><strong>Days Remaining for delivery:</strong> ${remainingDaysText}</p>`;
                    actionButtonHtml = generateActionButton('pending', item); // Show button only if shipped
                } else if (item.status === "Delivered") {
                    deliveryStatusText = "Delivered successfully";
                }

                html += `
                    <div class="buyer-item-card">
                        <div class="item-image-container">
                            <img src="/static/uploads/${item.mainImage}" alt="Image of ${item.listingName}" class="item-main-image" />
                        </div>
                        <h4 class="item-OrId">${item.orderId}</h4>
                        <h4 class="item-name">${item.listingName}</h4>
                        <p class="item-Shipped"><strong>Shipped status:<br></strong> ${deliveryStatusText}</p>
                        ${remainingDaysTag}
                        ${actionButtonHtml}
                    </div>
                `;
            });

            html += '</div>';
            content.html(html);
        },
        error: function () {
            content.html('<p style="color:red;">Failed to load data.</p>');
        }
    });
}


function loadShippedItems() {
    const content = $('#buyerItemsContentOrder');
    content.html('<p>Loading...</p>'); // Show loading while fetching

    let email = localStorage.getItem("email"); // Get user email
    console.log(email)

    $.ajax({
        url: `http://localhost:8080/api/v1/PlaceOrder/shipped/${email}`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (!data || !data.data || !Array.isArray(data.data.shipped) || data.data.shipped.length === 0) {
                content.html('<p>No items found.</p>');
                return;
            }

            console.log("Response:", data);

            const bids = data.data.shipped;

            let html = '<div class="buyer-item-list">';
            bids.forEach(item => {
                let deliveryStatusText = "";
                let remainingDaysText = "";
                let remainingDaysTag = ""; // Empty string to conditionally include the tag

                // Check the delivery status
                if (item.status === "Delivered") {
                    deliveryStatusText = "delivered Success"; // Status when delivery is pending
                } else if (item.status === "shipped") {
                    deliveryStatusText = "Shipped"; // Status when item has been shipped
                    remainingDaysText = item.date; // Delivery is complete
                    remainingDaysTag = `<p class="payment-status"><strong>Days Remaining for delivery:</strong> ${remainingDaysText}</p>`;
                }

                html += `
                    <div class="buyer-item-card">
                        <div class="item-image-container">
                            <img src="/static/uploads/${item.mainImage}" alt="Image of ${item.listingName}" class="item-main-image" />
                        </div>
                        <h4 class="item-OrId">${item.listingId}</h4>
                        <h4 class="item-name">${item.listingName}</h4>
                        <p class="item-Shipped"><strong>Shipped status:<br></strong> ${deliveryStatusText}</p>
                        ${remainingDaysTag} <!-- Display Days Remaining tag only if the status is 'shipped' -->
                       
                    </div>
                `;
            });

            html += '</div>';
            content.html(html);
        },
        error: function () {
            content.html('<p style="color:red;">Failed to load data.</p>');
        }
    });
}

function loadDiliverdItems() {
    const content = $('#buyerItemsContent');
    content.html('<p>Loading...</p>'); // Show loading while fetching

    let email = localStorage.getItem("email"); // Get user email
    console.log(email)

    $.ajax({
        url: `http://localhost:8080/api/v1/PlaceOrder/Delivered/${email}`,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            if (!data || !data.data || !Array.isArray(data.data.delivered) || data.data.delivered.length === 0) {
                content.html('<p>No items found.</p>');
                return;
            }

            console.log("Response:", data);

            const bids = data.data.delivered;

            let html = '<div class="buyer-item-list">';
            bids.forEach(item => {

                html += `
                    <div class="buyer-item-card">
                        <div class="item-image-container">
                            <img src="/static/uploads/${item.mainImage}" alt="Image of ${item.listingName}" class="item-main-image" />
                        </div>
                        <h4 class="item-OrId">${item.listingId}</h4>
                        <h4 class="item-name">${item.listingName}</h4>
                        <p class="item-Shipped"><strong>Shipped status:<br></strong>"delivered Success"</p>
                        
                       
                    </div>
                `;
            });

            html += '</div>';
            content.html(html);
        },
        error: function () {
            content.html('<p style="color:red;">Failed to load data.</p>');
        }
    });
}


function loadBuyerItems1(type) {
    const content = $('#buyerItemsContentOrder');
    content.html('<p>Loading...</p>'); // Show loading while fetching

    let email = localStorage.getItem("email"); // Get user email
    let url = '';

    // Ensure email is available
    if (!email) {
        content.html('<p style="color:red;">User not logged in.</p>');
        return;
    }

    // Construct the URL based on the type
    switch (type) {
        case 'paid':
            url = `http://localhost:8080/api/v1/PlaceOrder/getPaid/${email}`;
            break;
        case 'nonPaid':
            loadPendingItems(); // Load separately
            return;
        case 'shipped':
            loadShippedItems(); // Load separately
            return;
        default:
            content.html('<p>Invalid tab type</p>');
            return;
    }

    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);

            const bids = data?.data?.paid;

            if (!Array.isArray(bids) || bids.length === 0) {
                content.html('<p>No items found.</p>');
                return;
            }

            let html = '<div class="buyer-item-list">';
            bids.forEach(item => {
                html += `
                    <div class="buyer-item-card">
                        <div class="item-image-container">
                            <img src="/static/uploads/${item.mainImage}" alt="Image of ${item.listingName}" class="item-main-image" />
                        </div>
                        <h4 class="item-name">${item.listingName}</h4>
                        <p class="item-quantity"><strong>Quantity Buying:</strong> ${item.qty}</p>
                        <p class="name"><strong>User Name:</strong> ${item.userName}</p>
                        ${generateActionButton1(type, item)}
                    </div>
                `;
            });
            html += '</div>';
            content.html(html);
        },
        error: function () {
            content.html('<p style="color:red;">Failed to load data.</p>');
        }
    });
}


function openDeliveryInfoModal(item) {
    document.getElementById('infoUserName').value = item.userName || '';
    document.getElementById('infoUserPhone').value = item.phone || '';
    document.getElementById('infoAddress1').value = item.address1 || '';
    document.getElementById('infoAddress2').value = item.address2 || '';
    document.getElementById('infoPostalCode').value = item.postalCode || '';

    document.getElementById('deliveryInfoModal').style.display = 'block';
}

function closeDeliveryInfoModal() {
    document.getElementById('deliveryInfoModal').style.display = 'none';
}

function submitShipment(item) {
    document.getElementById('youOrderModal').style.display = 'none';
    document.getElementById('shipModal').style.display = 'block';

    document.getElementById("submitShipmentDetails").onclick = function () {
        const listingID = item.listingId;
        const today = new Date();
        const formattedDate = today.toISOString().split('T')[0];
        const trackingNumber = document.getElementById('shipTrackingNumber').value;
        const estDeliveryDays = document.getElementById('shipEstDelivery').value;
        const shipmentProof = document.getElementById('shipmentProof').files[0];
        const status = "shipped";

        const sendData = new FormData();
        sendData.append('listingID', listingID);
        sendData.append('Date', formattedDate);
        sendData.append('trackingNumber', trackingNumber);
        sendData.append('estDeliveryDays', estDeliveryDays);
        sendData.append('shipmentImage', shipmentProof);
        sendData.append('status', status);

        $.ajax({
            url: 'http://localhost:8080/api/v1/Delivery/Shipment',
            method: 'POST',
            data: sendData,
            contentType: false,
            processData: false,
            success: function () {
                Swal.fire({
                    icon: 'success',
                    title: 'Shipment Confirmed',
                    text: 'Shipment was confirmed successfully!',
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: 'OK'
                }).then(() => {
                    document.getElementById('shipModal').style.display = 'none';
                });
            },
            error: function () {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Failed to submit shipment!',
                    confirmButtonColor: '#d33',
                    confirmButtonText: 'Try Again'
                });
            }
        });
    }
}






function generateActionButton1(type, item) {
    let email = localStorage.getItem("email");
    if (type === 'paid') {
        // Show "Purchase Now" button for bids
        return `<button class="btn-purchase" onclick='submitShipment(${JSON.stringify(item)})'>✅ Conform Shipped</button>
            <button class="btn-deliver" onclick='openDeliveryInfoModal(${JSON.stringify(item)})'>📦 Delivery Details</button>`;
    } else if (type === 'pending') {
        // Show "Mark as Delivered" button for pending items
        return `
            <button class="btn-deliver" onclick="markAsDelivered('${item.itemId}', '${email}', '${item.OrderId}')">✅ Mark as Delivered</button>
            <button class="btn-inquiry" onclick="inquireItem('${item.OrderId}')">❓ Inquiry</button>
        `;
    } else {
        // No button for delivered items
        return '';
    }
}


function generateActionButton(type, item) {
    let email = localStorage.getItem("email");
    if (type === 'bids') {
        // Show "Purchase Now" button for bids
        return `<button class="btn-purchase" onclick="purchaseNow('${item.listingID}', '${item.listingName}', '${item.price}')">🛒 Purchase Now</button>`;
    } else if (type === 'pending') {
        // Show "Mark as Delivered" button for pending items
        return `
            <button class="btn-deliver" onclick='markAsDelivered(${JSON.stringify(item)})'>✅ Mark as Delivered</button>
            <button class="btn-inquiry" onclick='inquireItem(${JSON.stringify(item)})'>❓ Inquiry</button>
        `;
    } else {
        // No button for delivered items
        return '';
    }
}

function inquireItem(item) {
    console.log(item);

    $('#inquiryOrderId').val(item.orderId); // Set order ID
    $('#inquiryMessage').val('');
    document.getElementById('youBuyModal').style.display = 'none';
    $('#inquiryModal').modal('show');
}

// Only bind the form submit once
$(document).ready(function () {
    $('#inquiryForm').on('submit', function (e) {
        e.preventDefault();

        const orderId = $('#inquiryOrderId').val();
        const message = $('#inquiryMessage').val().trim();
        const email = localStorage.getItem("email");

        if (!message) {
            Swal.fire('Oops!', 'Please enter a message.', 'warning');
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/Inquiry/send',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                orderId: orderId,
                message: message,
                email: email
            }),
            success: function () {
                Swal.fire('Success', 'Inquiry sent successfully!', 'success');
                $('#inquiryModal').modal('hide');
            },
            error: function () {
                Swal.fire('Error', 'Failed to send inquiry.', 'error');
            }
        });
    });
});

function markAsDelivered(item) {
    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to mark this item as delivered?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, mark it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: 'http://localhost:8080/api/v1/Delivery/UpdateShipment',
                method: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify({
                    orderId: item.orderId
                }),
                success: function (response) {
                    Swal.fire(
                        'Success!',
                        'Item marked as delivered.',
                        'success'
                    );
                    loadBuyerItems1('nonPaid'); // Reload updated section
                },
                error: function () {
                    Swal.fire(
                        'Error!',
                        'Failed to mark as delivered.',
                        'error'
                    );
                }
            });
        }
    });
}






/*
// Confirm purchase (for Winning Bids)
function confirmPurchase(itemId) {
    if (confirm("Are you sure you want to purchase this item?")) {
        fetch(`/api/purchase-item`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ itemId })
        })
            .then(res => res.json())
            .then(response => {
                alert(response.message);
                loadBuyerItems('bids'); // Refresh bids
            })
            .catch(err => {
                alert("Purchase failed.");
                console.error(err);
            });
    }
}*/







function loadCartItems() {
    let email = localStorage.getItem("email"); // Assuming email is stored in localStorage

    $.ajax({
        url: `http://localhost:8080/api/v1/AddToCart/getByid/${email}`,  // Get cart items based on the email
        method: "GET",
        dataType: "json",
        success: function (response) {
            console.log("Response data:", response);  // Log the entire data response
            console.log("Type of data:", Array.isArray(response));  // Check if it's an array

            // If the response contains 'data' which is an array of cart items
            if (response && Array.isArray(response.data)) {
                let cartItems = response.data;
                console.log("Cart items:", cartItems);

                updateCartCount(cartItems.length);  // Update cart item count

                // Check if the cart is empty
                if (cartItems.length === 0) {
                    $("#cartItemList").html("<p>Your cart is empty.</p>");
                    return;
                }

                // Initialize HTML for cart items
                let html = `
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Select</th>
                                <th>Image</th>
                                <th>Item</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                `;

                let completedRequests = 0; // To track when all farmed item requests are completed

                // Loop through the cart items and generate HTML for each one
                cartItems.forEach(item => {
                    // Handle land and car items directly (no additional AJAX needed)
                    if (item.listingType === "land" || item.listingType === "car") {
                        let price = !isNaN(item.price) ? item.price.toFixed(2) : "0.00";
                        html += `
                            <tr class="cart-item">
                                <td><input type="checkbox" class="cart-item-checkbox" data-id="${item.listingId}" /></td>
                                <td><strong>${item.itemName}</strong></td>
                                <td>${item.quantity}</td>
                                <td>Rs. ${price}</td>
                                <td>
                                    <button onclick="buyItem(${item.listingId}, '${item.listingType}')">Buy Now</button>
                                    <button onclick="placeBid(${item.listingId}, '${item.listingType}')">Bid Now</button>
                                    <button onclick="removeItem(${item.listingId}, '${item.listingType}')">Remove</button>
                                </td>
                            </tr>
                        `;
                    }

                    // Handle farmed items (Check one by one)
                    if (item.listingType === "farmed") {
                        $.ajax({
                            url: `http://localhost:8080/api/listings/getById/${item.listingItemId}`,  // Get farmed item by ID
                            method: "GET",
                            dataType: "json",
                            success: function (farmedResponse) {
                                if (farmedResponse && farmedResponse.data) {
                                    let farmedItem = farmedResponse.data;
                                    let price = !isNaN(item.price) ? item.price.toFixed(2) : "0.00";

                                    // Define buttons based on selling type
                                    let buyNowButton = "";
                                    let bidNowButton = "";

                                    // Check if it's fixed price or auction
                                    if (farmedItem.sellType === "fixed") {
                                        buyNowButton = `<button onclick="buyItem(${item.listingId}, 'farmed')">Buy Now</button>`;
                                    } else if (farmedItem.sellType === "bidding") {
                                        bidNowButton = `<button onclick="placeBid(${item.listingId}, 'farmed')">Bid Now</button>`;
                                    } else if (farmedItem.sellType === "both") {
                                        buyNowButton = `<button onclick="buyItem(${item.listingId}, 'farmed')">Buy Now</button>`;
                                        bidNowButton = `<button onclick="placeBid(${item.listingId}, 'farmed')">Bid Now</button>`;
                                    }

                                    // Construct HTML for cart item
                                    html += `
                                        <tr class="cart-item">
                                            <td><input type="checkbox" class="cart-item-checkbox" data-id="${item.listingId}" /></td>
                                            <td><img src="/static/uploads/${farmedItem.mainImage}" alt="${farmedItem.title}" class="cart-item-image" /></td>
                                            <td><strong>Farmed Item: ${farmedItem.title}</strong></td>
                                            <td>${item.quantity}</td>
                                            <td>Rs. ${price}</td>
                                            <td>
                                                ${buyNowButton} ${bidNowButton}
                                                <button onclick="removeItem(${item.listingId}, '${item.listingType}')">Remove</button>
                                            </td>
                                        </tr>
                                    `;
                                }

                                // Increment completed requests count
                                completedRequests++;

                                // Check if all AJAX requests are completed
                                if (completedRequests === cartItems.length) {
                                    html += `</tbody></table>`; // Close the table structure
                                    $("#cartItemList").html(html);
                                     // Show the modal after items are loaded
                                }
                            },
                            error: function (error) {
                                console.error("Error fetching farmed item:", error);
                                html += `<p>Unable to fetch farmed item details.</p>`;

                                // Increment completed requests count even if there was an error
                                completedRequests++;

                                // Check if all AJAX requests are completed
                                if (completedRequests === cartItems.length) {
                                    html += `</tbody></table>`; // Close the table structure
                                    $("#cartItemList").html(html);
                                      // Show the modal after items are loaded
                                }
                            }
                        });
                    }
                });

                $("#cartItemList").html(html);

            } else {
                console.error("Invalid response structure: 'data' is not an array");
                $("#cartItemList").html("<p>Error: Unable to load cart items.</p>");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error loading cart:", error);
            $("#cartItemList").html("<p>Something went wrong loading the cart.</p>");
        }
    });
}
function removeItem(listingId ,listingType) {
    // Send an AJAX request to remove the individual item from the cart
    $.ajax({
        url: `http://localhost:8080/api/v1/AddToCart/removeItem/${listingId}`,
        method: "DELETE",
        success: function (response) {
            console.log("Item removed:", response);
            loadCartItems();  // Reload cart items after removal
        },
        error: function (xhr, status, error) {
            console.error("Error removing item:", error);
            alert("Failed to remove the item from the cart.");
        }
    });
}
document.querySelector(".cart-close").addEventListener("click", function() {
    closeModal();
});

window.onclick = function(event) {
    let modal = document.getElementById("cartModal");
    if (event.target === modal) {
        closeModal();  // Close if clicked outside the modal
    }
}
function closeModal() {
    let modal = document.getElementById("cartModal");
    modal.style.display = "none";  // Hide the modal
}
function showModal() {
    document.getElementById('cartModal').style.display = "block";
    document.getElementById('cartOverlay').style.display = "block";
}

function updateCartCount(count) {
    const cartCountElement = document.getElementById("cartCount");
    if (cartCountElement) {
        cartCountElement.textContent = count;
        cartCountElement.style.display = count > 0 ? "inline-block" : "none"; // Hide if 0
    }
}



//WatchedItem section

function loadWatchedItems() {
    let email = localStorage.getItem("email");

    $.ajax({
        url: `http://localhost:8080/api/v1/WatchItem/getByid/${email}`,
        method: "GET",
        dataType: "json",
        success: function (response) {
            console.log("Response data:", response);

            if (response && Array.isArray(response.data)) {
                let cartItems = response.data;
                console.log("Watched items:", cartItems);

                updateWatchedCount(cartItems.length);

                if (cartItems.length === 0) {
                    $("#watchItemList").html("<p>You haven't watched any items yet.</p>");
                    return;
                }

                let html = "";
                let completedRequests = 0;

                cartItems.forEach(item => {
                    if (item.listingType === "land" || item.listingType === "car") {
                        let price = !isNaN(item.price) ? item.price.toFixed(2) : "0.00";
                        html += `
                            <div class="watched-item">
                                <div class="watched-item-img">
                                    <img src="/static/uploads/${item.image}" alt="${item.itemName}">
                                </div>
                                <div class="watched-item-details">
                                    <h4>${item.itemName}</h4>
                                    <p><strong>Type:</strong> ${item.listingType}</p>
                                    <p><strong>Qty:</strong> ${item.quantity}</p>
                                    <p><strong>Price:</strong> Rs. ${price}</p>
                                    <div class="watched-item-actions">
                                        <button onclick="buyItem(${item.listingId}, '${item.listingType}')">Buy Now</button>
                                        <button onclick="placeBid(${item.listingId}, '${item.listingType}')">Bid Now</button>
                                        <button onclick="removeItem(${item.listingId}, '${item.listingType}')">Remove</button>
                                    </div>
                                </div>
                            </div>
                        `;
                        completedRequests++;
                        if (completedRequests === cartItems.length) {
                            $("#watchItemList").html(html);
                        }
                    }

                    if (item.listingType === "farmed") {
                        $.ajax({
                            url: `http://localhost:8080/api/listings/getById/${item.listingItemId}`,
                            method: "GET",
                            dataType: "json",
                            success: function (farmedResponse) {
                                if (farmedResponse && farmedResponse.data) {
                                    let farmedItem = farmedResponse.data;
                                    let price = !isNaN(item.price) ? item.price.toFixed(2) : "0.00";

                                    let buyNowButton = "";
                                    let bidNowButton = "";

                                    if (farmedItem.sellType === "fixed") {
                                        buyNowButton = `<button onclick="buyItem(${item.listingId}, 'farmed')">Buy Now</button>`;
                                    } else if (farmedItem.sellType === "bidding") {
                                        bidNowButton = `<button onclick="placeBid(${item.listingId}, 'farmed')">Bid Now</button>`;
                                    } else if (farmedItem.sellType === "both") {
                                        buyNowButton = `<button onclick="buyItem(${item.listingId}, 'farmed')">Buy Now</button>`;
                                        bidNowButton = `<button onclick="placeBid(${item.listingId}, 'farmed')">Bid Now</button>`;
                                    }

                                    html += `
                                        <div class="watched-item">
                                            <div class="watched-item-img">
                                                <img src="/static/uploads/${farmedItem.mainImage}" alt="${farmedItem.title}">
                                            </div>
                                            <div class="watched-item-details">
                                                <h4>${farmedItem.title}</h4>
                                                <p><strong>Type:</strong> ${item.listingType}</p>
                                                <p><strong>Qty:</strong> ${item.quantity}</p>
                                                <p><strong>Price:</strong> Rs. ${price}</p>
                                                <div class="watched-item-actions">
                                                    ${buyNowButton} ${bidNowButton}
                                                    <button onclick="removeItem(${item.listingId}, '${item.listingType}')">Remove</button>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                    `;
                                }

                                completedRequests++;
                                if (completedRequests === cartItems.length) {
                                    $("#watchItemList").html(html);
                                }
                            },
                            error: function () {
                                html += `<p>Unable to fetch farmed item details.</p>`;
                                completedRequests++;
                                if (completedRequests === cartItems.length) {
                                    $("#watchItemList").html(html);
                                }
                            }
                        });
                    }
                });

            } else {
                $("#watchItemList").html("<p>Error: Unable to load watched items.</p>");
            }
        },
        error: function (xhr, status, error) {
            console.error("Error loading watched items:", error);
            $("#watchItemList").html("<p>Something went wrong loading watched items.</p>");
        }
    });
}

function showModalWatched() {
    document.getElementById('watchlistModal').style.display = "block";
}
function closeModalWatched() {
    document.getElementById("watchlistModal").style.display = "none";
}

// Close when clicking the × button
document.getElementById("closeWatchlistModal").addEventListener("click", function () {
    closeModalWatched();
});

// Close when clicking outside the modal content
window.addEventListener("click", function (event) {
    const modal = document.getElementById("watchlistModal");
    if (event.target === modal) {
        closeModalWatched();
    }
});



function buyItem(listingId, type) {
    console.log(`Buying ${type} item with listing ID: ${listingId}`);
    // Implement the buy functionality here
}

/*function placeBid(listingId, type) {
    console.log(`Placing bid for ${type} item with listing ID: ${listingId}`);
    // Implement the bid functionality here
}*/

function updateWatchedCount(count) {
    const cartCountElement = document.getElementById("WatchCount");
    if (cartCountElement) {
        cartCountElement.textContent = count;
        cartCountElement.style.display = count > 0 ? "inline-block" : "none"; // Hide if 0
    }
}




function savePaymentDetails(purchaseId, totalPrice, email,type,id) {
    console.log("Saving payment details...");

    // Create the payment data object
    const paymentData = {
        purchaseId: purchaseId,
        amount: totalPrice,
        email: email,
        paymentStatus: "Hold",
        paymentMethod:"card",
        listingId: id,
        listingType:type
    };

    // Send payment details to your server
    $.ajax({
        url: 'http://localhost:8080/api/v1/Payment', // replace with your actual endpoint
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(paymentData),
        success: function (response) {

        },
        error: function (error) {
            console.error("Error saving payment details:", error);

        }
    });
}

// Function to generate the next purchaseId based on the latest one
function generateNextPurchaseId(latestPurchaseId) {
    const prefix = "OR";
    let currentIdNumber = parseInt(latestPurchaseId.replace(prefix, ''));

    // Increment the number
    const newIdNumber = currentIdNumber + 1;

    // Format the new purchaseId (e.g., OR001, OR002, OR003)
    const newPurchaseId = prefix + String(newIdNumber).padStart(3, '0');
    return newPurchaseId;
}

function loadUserDeliveryData(email, newPurchaseId) {
    const token = localStorage.getItem("token");
    $.ajax({
        url: `http://localhost:8080/api/v1/user/getUserByEmail?email=${encodeURIComponent(email)}`,
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': 'Bearer ' + token // Add your JWT token here
        },
        success: function (response) {
            console.log("API Response:", response); // Log the response to verify the structure

            // Set the form fields with the response data
            $('#addressLine1').val(response.data.addressLine1 || '');
            $('#addressLine2').val(response.data.addressLine2 || '');
            $('#contactNumber').val(response.data.phoneNumber || '');
            $('#purchaseId').val(newPurchaseId);

            // Show the modal only after the data has been set
            setTimeout(function() {
                var deliveryModal = new bootstrap.Modal(document.getElementById('deliveryModal'));
                deliveryModal.show();
            }, 100); // Delay to ensure data is set before showing modal
        },
        error: function (error) {
            console.error("Error fetching delivery data:", error);
            alert("Error loading delivery data.");
        }
    });
}



document.addEventListener("click", function (event) {
    if (event.target && event.target.classList.contains("buy-now")) {
        const listingId = event.target.dataset.id;
        const title = event.target.dataset.title;
        const price = event.target.dataset.price;

        // Set the modal details
        document.getElementById("buyTitle").textContent = `Buy ${title}`;
        document.getElementById("buyPrice").textContent = price;

        purchaseNow(listingId,title,price)


    }
});


document.addEventListener("DOMContentLoaded", function () {
    console.log("aaa")
    const decreaseBtn = document.querySelector(".decrease");
    const increaseBtn = document.querySelector(".increase");
    const quantityInput = document.getElementById("quantity");
    const unitPriceSpan = document.getElementById("buyPrice");
    const totalPriceSpan = document.getElementById("totalPrice");

    function calculateTotal() {
        const quantity = parseInt(quantityInput.value) || 1;
        const unitPrice = parseFloat(unitPriceSpan.textContent) || 0;
        const total = (quantity * unitPrice).toFixed(2);
        totalPriceSpan.textContent = total;
    }

    if (decreaseBtn && increaseBtn && quantityInput) {
        decreaseBtn.addEventListener("click", function () {
            let currentVal = parseInt(quantityInput.value);
            if (currentVal > 1) {
                quantityInput.value = currentVal - 1;
                calculateTotal();
            }
        });

        increaseBtn.addEventListener("click", function () {
            let currentVal = parseInt(quantityInput.value);
            quantityInput.value = currentVal + 1;
            calculateTotal();
        });

        quantityInput.addEventListener("input", calculateTotal);
    }

    // Recalculate on modal open
    $('#buyNowModal').on('shown.bs.modal', function () {
        calculateTotal();
    });
});


function purchaseNow(itemId, listingName, price) {
    console.log("Item ID:", itemId);
    console.log("Listing Name:", listingName);
    console.log("Price:", price);

    Swal.fire({
        title: `Purchase Confirmation`,
        text: `Do you want to purchase "${listingName}" for $${price}?`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, buy it!',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            $('#youBuyModal').hide();
            document.getElementById("buyTitle").textContent = `Buy ${listingName}`;
            document.getElementById("buyPrice").textContent = `${price}`;
            $('#buyNowModal').modal('show');

            $("#placeOrderBtn").off("click").on("click", function () {
                const email = localStorage.getItem("email");
                const listingType = document.getElementById("placeOrderBtn").getAttribute("data-listing-type");
                const quantity = parseInt(document.getElementById("quantity").value);

                if (!quantity || quantity <= 0) {
                    Swal.fire("Invalid Quantity", "Please enter a valid quantity.", "warning");
                    return;
                }

                const totalPrice = price * quantity;

                $('#buyNowModal').modal('hide');
                const creditCardModal = new bootstrap.Modal(document.getElementById('creditCardModalContainer'));
                creditCardModal.show();

                $("#submitCardPaymentBtn").off("click").on("click", function () {
                    console.log("Processing payment...");
                    creditCardModal.hide();

                    const purchaseData = {
                        listingId: itemId,
                        listingType: listingType,
                        quantity: quantity,
                        totalPrice: totalPrice
                    };

                    $.ajax({
                        url: `http://localhost:8080/api/v1/Purchase?email=${email}`,
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(purchaseData),
                        success: function () {
                            console.log("Purchase placed successfully");

                            $.ajax({
                                url: 'http://localhost:8080/api/v1/Purchase/latestPurchaseId',
                                type: 'GET',
                                success: function (latestPurchaseId) {
                                    console.log("Latest Purchase ID:", latestPurchaseId);

                                    loadUserDeliveryData(email, latestPurchaseId);

                                    document.getElementById("submitDeliveryDetails").onclick = function () {
                                        const purchaseId = document.getElementById("purchaseId").value;
                                        const addressLine1 = document.getElementById("addressLine1").value;
                                        const addressLine2 = document.getElementById("addressLine2").value;
                                        const contactNumber = document.getElementById("contactNumber").value;
                                        const postalCode = document.getElementById("postalCode").value;

                                        if (!purchaseId || !addressLine1 || !contactNumber || !postalCode) {
                                            Swal.fire("Missing Info", "Please fill all required delivery fields.", "warning");
                                            return;
                                        }

                                        $.ajax({
                                            url: 'http://localhost:8080/api/v1/Delivery',
                                            type: 'POST',
                                            data: JSON.stringify({
                                                purchaseId: purchaseId,
                                                address1: addressLine1,
                                                address2: addressLine2,
                                                contactNumber: contactNumber,
                                                postalCode: postalCode
                                            }),
                                            contentType: 'application/json',
                                            success: function () {
                                                savePaymentDetails(purchaseId, totalPrice, email, listingType, itemId);
                                                Swal.fire("Success", "Payment success !", "success");
                                                const deliveryModal = new bootstrap.Modal(document.getElementById('deliveryModal'));
                                                deliveryModal.hide();
                                            },
                                            error: function () {
                                                Swal.fire("Error", "Error saving delivery details!", "error");
                                            }
                                        });
                                    };
                                },
                                error: function (xhr, status, error) {
                                    console.error("Error fetching latest purchaseId:", error);
                                    Swal.fire("Error", "Error fetching latest purchase ID.", "error");
                                }
                            });
                        },
                        error: function (xhr, status, error) {
                            console.error("Error placing the order:", error);
                            Swal.fire("Error", "❌ Error placing the order.", "error");
                        }
                    });
                });
            });
        }
    });
}