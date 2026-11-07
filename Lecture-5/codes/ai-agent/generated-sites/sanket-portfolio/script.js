// Simple interactivity: nav toggle, current year, and form handler
document.addEventListener('DOMContentLoaded', function(){
  const toggle = document.querySelector('.nav-toggle');
  const links = document.querySelector('.nav-links');
  toggle.addEventListener('click', function(){
    links.classList.toggle('open');
  });

  document.getElementById('year').textContent = new Date().getFullYear();
});

function submitForm(e){
  e.preventDefault();
  const form = e.target;
  const name = form.name.value.trim();
  const email = form.email.value.trim();
  const message = form.message.value.trim();
  if(!name||!email||!message){
    alert('Please fill all fields.');
    return;
  }
  // In a static site we can't send email — show a friendly message
  alert('Thanks, '+name+"! I will get back to you soon.");
  form.reset();
}
