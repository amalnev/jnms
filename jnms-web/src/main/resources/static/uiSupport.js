function onDocumentLoaded() {
    var changePasswordCheckbox = document.getElementById("changePasswordCheckbox");
    var saveButton = document.getElementById("saveButton");

    if(changePasswordCheckbox != null) {
        changePasswordCheckbox.addEventListener("click", function () {
            var checkBox = document.getElementById("changePasswordCheckbox");
            var passwordField = document.getElementById("passwordField");
            var passwordConfirmationField = document.getElementById("passwordConfirmationField");

            if (passwordField.disabled) {
                passwordField.disabled = false;
                passwordConfirmationField.disabled = false;
            } else {
                passwordField.disabled = true;
                passwordConfirmationField.disabled = true;
            }
        });
    }

    if(saveButton != null) {
        saveButton.addEventListener("click", function () {
            var passwordField = document.getElementById("passwordField");
            var passwordConfirmationField = document.getElementById("passwordConfirmationField");
            var errorBox = document.getElementById("errorBox");
            var userForm = document.getElementById("userForm");

            if (passwordField.value != passwordConfirmationField.value) {
                errorBox.innerText = "Passwords do not match";
            } else {
                userForm.submit();
            }
        });
    }
}
