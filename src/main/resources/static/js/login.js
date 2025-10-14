// Login Page JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Focus on username field when page loads
    const usernameField = document.getElementById('username');
    if (usernameField) {
        usernameField.focus();
    }
    
    // Add form validation
    const loginForm = document.querySelector('form[th\\:action="@{/login}"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value;
            
            if (!username) {
                event.preventDefault();
                showError('Please enter your username');
                return;
            }
            
            if (!password) {
                event.preventDefault();
                showError('Please enter your password');
                return;
            }
        });
    }
    
    // Clear error messages when user starts typing
    const inputs = document.querySelectorAll('input[type="text"], input[type="password"]');
    inputs.forEach(input => {
        input.addEventListener('input', function() {
            clearError();
        });
    });
});

function showError(message) {
    // Remove existing error messages
    clearError();
    
    // Create new error message
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error';
    errorDiv.textContent = message;
    
    // Insert error message at the top of the form
    const form = document.querySelector('form');
    if (form) {
        form.insertBefore(errorDiv, form.firstChild);
    }
}

function clearError() {
    const existingError = document.querySelector('.error');
    if (existingError && !existingError.textContent.includes('Invalid username or password')) {
        existingError.remove();
    }
}
