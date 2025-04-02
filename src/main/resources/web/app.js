const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});



  document.addEventListener("DOMContentLoaded", function () {
    const steps = document.querySelectorAll(".step");
    const progress = document.querySelector(".progress");
    const circles = document.querySelectorAll(".step-circle");
    const nextBtns = document.querySelectorAll(".next-step");
    const prevBtns = document.querySelectorAll(".prev-step");
    const form = document.querySelector(".sign-up-form");

    let currentStep = 0;

    function updateSteps() {
      steps.forEach((step, index) => {
        step.classList.toggle("active", index === currentStep);
      });

      // Update progress bar width
      progress.style.width = `${(currentStep / (steps.length - 1)) * 100}%`;

      // Update step indicators
      circles.forEach((circle, index) => {
        circle.classList.toggle("active", index <= currentStep);
      });
    }

    function validateStep() {
      let inputs = steps[currentStep].querySelectorAll("input");
      for (let input of inputs) {
        if (!input.value.trim()) {
          alert("Please fill all fields before proceeding.");
          return false;
        }
      }

      if (currentStep === 1) {
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        if (password !== confirmPassword) {
          alert("Passwords do not match!");
          return false;
        }
      }

      return true;
    }

    nextBtns.forEach((button) => {
      button.addEventListener("click", () => {
        if (validateStep() && currentStep < steps.length - 1) {
          currentStep++;
          updateSteps();
        }
      });
    });

    prevBtns.forEach((button) => {
      button.addEventListener("click", () => {
        if (currentStep > 0) {
          currentStep--;
          updateSteps();
        }
      });
    });

    form.addEventListener("submit", (e) => {
      e.preventDefault();
      alert("Form submitted successfully!");
    });

    updateSteps();
  });











let map, marker;

function initMap(lat = 7.8731, lng = 80.7718) { // Default location: Sri Lanka
  map = L.map('map').setView([lat, lng], 10);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map);

  marker = L.marker([lat, lng], { draggable: true }).addTo(map);

  // Update location when marker is moved
  marker.on('dragend', function(event) {
    let position = marker.getLatLng();
    document.getElementById('latitude').value = position.lat.toFixed(6);
    document.getElementById('longitude').value = position.lng.toFixed(6);
    reverseGeocode(position.lat, position.lng);
  });
}

function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        function(position) {
          let lat = position.coords.latitude;
          let lng = position.coords.longitude;

          document.getElementById('latitude').value = lat.toFixed(6);
          document.getElementById('longitude').value = lng.toFixed(6);

          map.setView([lat, lng], 15);
          marker.setLatLng([lat, lng]);

          reverseGeocode(lat, lng);
        },
        function(error) {
          alert("Error getting location: " + error.message);
        },
        { enableHighAccuracy: true, timeout: 10000 }
    );
  } else {
    alert("Geolocation is not supported by this browser.");
  }
}

function reverseGeocode(lat, lng) {
  fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`)
      .then(response => response.json())
      .then(data => {
        console.log("Geocode Response:", data); // Debugging response
        let locationLink = `https://www.google.com/maps?q=${lat},${lng}`;
        console.log(`Location Link: ${locationLink}`);

        if (data.address) {
          document.getElementById('address1').value = data.address.road || data.address.neighbourhood || "";
          document.getElementById('address12').value = data.address.city || data.address.town || data.address.village || "";
        } else {
          document.getElementById('address1').value = "Unknown Road";
          document.getElementById('address12').value = "Unknown City";
        }
      })
      .catch(error => {
        console.error("Error fetching address:", error);
      });
}

// Initialize the map on page load
document.addEventListener("DOMContentLoaded", function() {
  initMap();
});



document.getElementById("userType").addEventListener("change", function () {
  let userType = this.value;
  let dynamicSection = document.getElementById("dynamicSection");
  let dynamicTextArea = document.getElementById("dynamicTextArea");
  let dynamicIcon = document.getElementById("dynamicIcon");

  if (userType === "seller") {
    dynamicTextArea.placeholder = "What do you want to sell?";
    dynamicIcon.className = "fas fa-box"; // Seller icon
    dynamicSection.style.display = "block";
  } else if (userType === "buyer") {
    dynamicTextArea.placeholder = "What do you want to buy?";
    dynamicIcon.className = "fas fa-shopping-cart"; // Buyer icon
    dynamicSection.style.display = "block";
  } else {
    dynamicSection.style.display = "none"; // Hide if no selection
  }
});













// List of countries with codes
const countries = [
  { name: "Sri Lanka", code: "+94", flag: "🇱🇰" },
  { name: "United States", code: "+1", flag: "🇺🇸" },
  { name: "United Kingdom", code: "+44", flag: "🇬🇧" },
  { name: "India", code: "+91", flag: "🇮🇳" },
  { name: "Australia", code: "+61", flag: "🇦🇺" },
  { name: "Germany", code: "+49", flag: "🇩🇪" },
  { name: "France", code: "+33", flag: "🇫🇷" },
  { name: "Japan", code: "+81", flag: "🇯🇵" },
  { name: "Canada", code: "+1", flag: "🇨🇦" },
  { name: "China", code: "+86", flag: "🇨🇳" },
  { name: "Brazil", code: "+55", flag: "🇧🇷" },
  { name: "South Africa", code: "+27", flag: "🇿🇦" },
  { name: "Italy", code: "+39", flag: "🇮🇹" },
  { name: "Spain", code: "+34", flag: "🇪🇸" },
  // Add more countries as needed...
];

const countrySelect = document.getElementById("country");
const phoneInput = document.getElementById("tel");

// Populate country selector
countries.forEach(country => {
  const option = document.createElement("option");
  option.value = country.code;
  option.textContent = `${country.flag} ${country.name} (${country.code})`;
  countrySelect.appendChild(option);
});

// Auto-update phone input when country changes
countrySelect.addEventListener("change", function () {
  const selectedCode = this.value;

  // Only update if phone number is empty or doesn't start with the country code
  if (!phoneInput.value.startsWith(selectedCode)) {
    phoneInput.value = selectedCode + " ";
  }
});



// Function to validate phone number
function validatePhoneNumber() {
  const phoneInput = document.getElementById("tel").value;
  const countryCode = document.getElementById("country").value;

  // Remove spaces and check if it starts with the selected country code
  const formattedPhone = phoneInput.replace(/\s+/g, "");

  if (!formattedPhone.startsWith(countryCode)) {
    alert("Phone number must start with the correct country code!");
    return false;
  }

  // General phone number pattern (adjust for specific country needs)
  const phonePattern = /^[+]\d{1,3}[-.\s]?\d{4,14}$/;

  if (!phonePattern.test(formattedPhone)) {
    alert("Invalid phone number format!");
    return false;
  }


  return true;
}

// Attach event listener to validate on blur (when user leaves the input)
document.getElementById("tel").addEventListener("blur", validatePhoneNumber);



function toggleRegisterButton() {
  const checkbox = document.getElementById("termsCheckbox");
  const registerBtn = document.getElementById("registerBtn");

  registerBtn.disabled = !checkbox.checked;
}

// Open Terms Modal with Smooth Animation
function openTermsModal() {
  const modal = document.getElementById("termsModal");
  modal.style.display = "block"; // Make it visible
  setTimeout(() => {
    modal.classList.add("show"); // Add animation class
  }, 10);
}

// Close Terms Modal with Smooth Animation
function closeTermsModal() {
  const modal = document.getElementById("termsModal");
  modal.classList.remove("show"); // Start fade-out animation
  setTimeout(() => {
    modal.style.display = "none"; // Hide after animation
  }, 400); // Wait for animation duration
}

// Accept Terms and Check the Checkbox
function acceptTerms() {
  document.getElementById("termsCheckbox").checked = true;
  toggleRegisterButton();
  closeTermsModal();
}










// Modal and Button References
const forgotPasswordLink = document.getElementById('forgot-password-link');
const modal1 = document.getElementById('modal1');
const closeModalButton = document.querySelector('.close');
const checkRegisteredBtn = document.getElementById('check-registered-btn');
const verifyOtpBtn = document.getElementById('verify-otp-btn');

// Step Elements
const step1 = document.getElementById('step1');
const step2 = document.getElementById('step2');

// Status Elements
const checkStatus = document.getElementById('check-status');
const otpStatus = document.getElementById('otp-status');

// Send Again Button
const sendAgainBtn = document.getElementById('send-again-btn');

// Open Modal Function
forgotPasswordLink.addEventListener('click', function(event) {
  event.preventDefault();
  modal1.classList.add('show');  // Show the modal
});

// Close Modal
closeModalButton.addEventListener('click', function() {
  modal1.classList.remove('show');  // Close the modal
});

// Simulate the checking of registered email/phone
checkRegisteredBtn.addEventListener('click', function() {
  const emailOrPhone = document.getElementById('email-or-phone').value;

  if (emailOrPhone) {
    // Simulating the check in the database
    // In a real application, you would send an API request here
    if (emailOrPhone === "test@example.com" || emailOrPhone === "1234567890") {
      checkStatus.textContent = "Registered! OTP has been sent.";
      checkStatus.style.color = "green";

      // Hide Step 1 and show Step 2
      step1.style.display = "none";
      step2.style.display = "block";

      // Simulate sending OTP (you can trigger email/SMS here)
      sendOtp();
    } else {
      checkStatus.textContent = "This email/phone is not registered.";
      checkStatus.style.color = "red";
    }
  } else {
    checkStatus.textContent = "Please enter your email or phone number.";
    checkStatus.style.color = "red";
  }
});

// Simulate OTP sending
function sendOtp() {
  otpStatus.textContent = "OTP sent! You have 30 seconds to enter it.";
  otpStatus.style.color = "green";

  // Start timer for OTP timeout
  let timeLeft = 30;
  const timer = setInterval(function() {
    otpStatus.textContent = `OTP sent! You have ${timeLeft} seconds to enter it.`;
    if (timeLeft <= 0) {
      clearInterval(timer);
      otpStatus.textContent = "OTP expired. Please request again.";
      otpStatus.style.color = "red";
      sendAgainBtn.style.display = "block";  // Show Send Again button
    }
    timeLeft--;
  }, 1000);
}

// Handle Send Again Button
sendAgainBtn.addEventListener('click', function() {
  sendAgainBtn.style.display = "none"; // Hide Send Again button
  otpStatus.textContent = "Sending OTP again...";
  otpStatus.style.color = "green";
  sendOtp(); // Resend OTP
});

// Simulate OTP verification
verifyOtpBtn.addEventListener('click', function() {
  const otp = document.getElementById('otp').value;

  if (otp === "1234") {  // Simulating the OTP (in real case, verify with sent OTP)
    otpStatus.textContent = "OTP verified! You can reset your password.";
    otpStatus.style.color = "green";
  } else {
    otpStatus.textContent = "Invalid OTP. Please try again.";
    otpStatus.style.color = "red";
  }
});
