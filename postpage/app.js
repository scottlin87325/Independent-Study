// Get elements
const createPostButton = document.getElementById('createPostButton');
const postModel = document.getElementById('postModel');
const postForm = document.getElementById('postForm');
const descriptionInput = document.getElementById('description');
const imageInput = document.getElementById('imageInput');
const postFeed = document.getElementById('postFeed');
const cancelPostButton = document.getElementById('cancelPost');

// Show the post model
createPostButton.addEventListener('click', () => {
    postModel.style.display = 'flex';  // Show the post modal
});

// Hide the post model
cancelPostButton.addEventListener('click', () => {
    postModel.style.display = 'none';  // Hide the post modal
});

// Handle post creation
postForm.addEventListener('submit', (event) => {
    event.preventDefault();  // Prevent form from submitting normally

    // Create a new post element
    const newPost = document.createElement('div');
    newPost.classList.add('post');

    // Create the image element
    const image = document.createElement('img');
    const file = imageInput.files[0];
    image.src = URL.createObjectURL(file);

    // Create the description element
    const description = document.createElement('p');
    description.textContent = descriptionInput.value;

    // Create delete button
    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Delete';
    deleteButton.classList.add('deleteButton');
    deleteButton.onclick = () => {
        newPost.remove();  // Delete the post
    };

    // Append image, description, and delete button to the post
    newPost.appendChild(image);
    newPost.appendChild(description);
    newPost.appendChild(deleteButton);

    // Add the post to the feed
    postFeed.appendChild(newPost);

    // Reset the modal and hide it
    postModel.style.display = 'none';
    descriptionInput.value = '';  // Reset description
    imageInput.value = '';  // Reset image input
});
