const prevButton = document.querySelector('.prev');
const nextButton = document.querySelector('.next');
const postImages = document.querySelector('.post-images');
const images = document.querySelectorAll('.post-images img');
let currentIndex = 0;

// Function to show the current image based on the index
function updatePost() {
  const totalImages = images.length;
  const offset = -(currentIndex * 100) + '%';
  postImages.style.transform = `translateX(${offset})`;
}

// Event listener for the previous button
prevButton.addEventListener('click', () => {
  currentIndex = (currentIndex === 0) ? images.length - 1 : currentIndex - 1;
  updatePost();
});

// Event listener for the next button
nextButton.addEventListener('click', () => {
  currentIndex = (currentIndex === images.length - 1) ? 0 : currentIndex + 1;
  updatePost();
});

// Optional: Auto cycle every 3 seconds
setInterval(() => {
  currentIndex = (currentIndex === images.length - 1) ? 0 : currentIndex + 1;
  updatePost();
}, 3000);
