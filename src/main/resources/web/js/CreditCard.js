


// Hide modal
document.getElementById('closeCardModalBtn').addEventListener('click', () => {
    document.getElementById('creditCardModalContainer').style.display = 'none';
});

// Close when clicking outside modal content
window.addEventListener('click', (e) => {
    const modal = document.getElementById('creditCardModalContainer');
    if (e.target === modal) {
        modal.style.display = 'none';
    }
});

// Update card preview as user types
document.getElementById('inputCardNumber').addEventListener('input', (e) => {
    const formatted = e.target.value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
    document.getElementById('displayCardNumber').textContent = formatted || '#### #### #### ####';
});

document.getElementById('inputCardHolder').addEventListener('input', (e) => {
    document.getElementById('displayCardHolder').textContent = e.target.value || 'YOUR NAME';
});

document.getElementById('inputCardMonth').addEventListener('input', updateExpiration);
document.getElementById('inputCardYear').addEventListener('input', updateExpiration);

function updateExpiration() {
    const month = document.getElementById('inputCardMonth').value.padStart(2, '0');
    const year = document.getElementById('inputCardYear').value.slice(-2);
    const text = month && year ? `${month}/${year}` : 'MM/YY';
    document.getElementById('displayCardExpiration').textContent = text;
}

// Optional: prevent form submission
document.getElementById('creditCardForm').addEventListener('submit', (e) => {
    e.preventDefault();
    alert("Payment submitted! (this is a demo)");
});
