let signUpButton,signInButton,container,signupForm;

window.addEventListener("DOMContentLoaded", () => {

signUpButton = document.getElementById('signUp');
signInButton = document.getElementById('signIn');
container = document.getElementById('container');
signupForm = document.querySelector('form[name=signupForm]')

signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});

signupForm.addEventListener('submit', (event) => {
 event.preventDefault();
 let displayName = signupForm.querySelector('input[name=displayName]').value;
 let username = signupForm.querySelector('input[name=username]').value;
 let password = signupForm.querySelector('input[name=password]').value;
 let data = { displayName:displayname , username:username, password:password};
 fetch('/register',{
 method:'post',
 body: JSON.stringify(data)
 })
});

});