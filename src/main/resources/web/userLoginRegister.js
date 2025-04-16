$(document).ready(function () {

    $('#login').on('click', function(e) {
        e.preventDefault();
        console.log("okkkk")

        const email = $('#email_phone').val().trim();
        const password = $('#login-password').val().trim();

        if (!email || !password) {
            alert("Please enter both email/phone and password.");
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/authenticate',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email, password }),
            success: function (response) {
                if (response.data && response.data.token) {
                    localStorage.setItem("token", response.data.token);
                    localStorage.setItem("email", response.data.email);
                    getUsername()
                    console.log(response.data);
                    checkUserRole();
                } else {
                    alert("Invalid login response. Please try again.");
                }
            },
            error: function (xhr) {
                console.error('Error:', xhr);
                alert(xhr.responseJSON?.message || 'Invalid credentials or server error.');
            }
        });
    });

    function checkUserRole() {
        let token = localStorage.getItem("token");
        if (!token) return;

        function checkRole(url, successCallback, failureCallback) {
            $.ajax({
                url:`http://localhost:8080/api/v1/admin/${url}`,
                type: "GET",
                headers: {"Authorization": `Bearer ${token}`},
                success: successCallback,
                error: failureCallback
            });
        }



        checkRole("test1",
            function () {
                showMainAdminDashboard();  // If success, user is Admin
            },
            function () {
                checkRole("test2",
                    function () {
                        showAdminDashboard();// If success, user is Seller
                    },
                    function () {
                        checkRole("test3",
                            function () {
                                showSellerDashboard();  // If success, user is Seller
                            },
                            function () {
                                checkRole("test4",
                                    function () {
                                        showBuyerDashboard();  // If success, user is Buyer
                                    },
                                    function () {
                                        showDashboard("Unauthorized: Access denied.");  // If all checks fail
                                    }
                                );
                            }
                        );
                    }
                );
            }
        );
}

    function showAdminDashboard() {
        window.location.href = 'adminDashboard.html';
    }

    function showMainAdminDashboard() {
        window.location.href = 'mainadminDashboard.html';
    }

    function showSellerDashboard() {
        window.location.href = 'index.html';
    }

    function showBuyerDashboard() {
        window.location.href = 'index.html';
    }

    function showDashboard(message) {
        alert(message,-'Unknown role.');
    }


    $('#registerBtn').on('click', function(e) {
        e.preventDefault();

        const userData = {
            firstName: $('#username').val(),
            lastName: $('#lastname').val(),
            email: $('#email').val(),
            phoneNumber: $('#tel').val(),
            password: $('#password').val(),
            role: $('#userType').val(),  // Buyer or Seller
            description: $('#dynamicTextArea').val(),
            addressLine1: $('#address1').val(),
            addressLine2: $('#address12').val(),
            latitude: $('#latitude').val(),
            longitude: $('#longitude').val()
        };

        $.ajax({
            url: 'http://localhost:8080/api/v1/user/register',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function (response) {
                alert("User registered successfully!");
                window.location.href = 'login.html';
            },
            error: function (error) {
                console.error('Error:', error);
                alert(error.responseJSON?.message || 'Registration failed.');
            }
        });
    });




    function getUsername() {
        const email = $('#email_phone').val().trim();
        const password = $('#login-password').val().trim(); // Unused, remove if unnecessary
        const token = localStorage.getItem("token");

        $.ajax({
            url: `http://localhost:8080/api/v1/user/getUserByEmail?email=${encodeURIComponent(email)}`,
            method: 'GET',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + token  // Add your JWT token here
            },
            success: function (response) {
                if (response.data) {
                    saveToLocalStorage("firstName", response.data.firstName, 2);
                    saveToLocalStorage("role", response.data.role, 2);
                    alert("User retrieved successfully!");
                    window.location.href = 'index.html';
                } else {
                    alert("User not found.");
                }
            },
            error: function (error) {
                console.error('Error:', error);
                alert(error.responseJSON?.message || 'Failed to get user details.');
            }
        });

    }

// Save firstName in localStorage with expiration (2 days)
    function saveToLocalStorage(key, value, days) {
        const now = new Date();
        const expiryTime = now.getTime() + days * 24 * 60 * 60 * 1000;
        const data = { value: value, expiry: expiryTime };
        localStorage.setItem(key, JSON.stringify(data));
    }

// Retrieve firstName with expiration check
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

// Example: Get first name from local storage
    const firstName = getFromLocalStorage("firstName");
    console.log("Retrieved First Name:", firstName);





});

