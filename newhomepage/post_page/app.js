function openModal() {
    document.getElementById('postModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('postModal').style.display = 'none';
}

function addPost() {
    const imageInput = document.getElementById('imageInput');
    const postText = document.getElementById('postText').value;

    if (imageInput.files.length === 0 || !postText.trim()) {
        alert('Please select an image and write a description.');
        return;
    }

    const reader = new FileReader();

    reader.onload = function(event) {
        const postContainer = document.getElementById('postContainer');

        const post = document.createElement('div');
        post.className = 'post';

        post.innerHTML = `
            <div class="post-header">
                <img class="profile-pic" src="https://via.placeholder.com/40" alt="Profile Picture" onclick="changeProfilePic(this)">
                <span class="username" onclick="changeUsername(this)">user_account</span>
                <span class="delete-icon" onclick="deletePost(this)">❌</span>
            </div>
            <img class="post-image" src="${event.target.result}" alt="Post Image">
            <div class="post-content">
                <p class="post-text">${postText}</p>
                <div class="interaction-bar">
                    <button onclick="likePost(this)">❤️ Like (<span class="like-count">0</span>)</button>
                    <button onclick="sharePost('${event.target.result}')">🔗 Share</button>
                </div>
                <a class="comment-btn" href="#" onclick="toggleComments(this)">View Comments (<span class="comment-count">0</span>)</a>
                <div class="comments-section" style="display: none;">
                    <textarea placeholder="Add a comment..."></textarea>
                    <button onclick="addComment(this)">Comment</button>
                    <div class="comments"></div>
                </div>
            </div>
        `;

        postContainer.append(post); // Append post to the bottom of the container
    };

    reader.readAsDataURL(imageInput.files[0]);

    document.getElementById('imageInput').value = '';
    document.getElementById('postText').value = '';
    closeModal();
}

function likePost(button) {
    const likeCount = button.querySelector('.like-count');
    likeCount.textContent = parseInt(likeCount.textContent) + 1;
}

function sharePost(imageUrl) {
    navigator.clipboard.writeText(imageUrl).then(() => {
        alert('Image URL copied to clipboard!');
    });
}

function toggleComments(button) {
    const commentsSection = button.parentElement.querySelector('.comments-section');
    commentsSection.style.display = commentsSection.style.display === 'none' ? 'block' : 'none';
}

function addComment(button) {
    const commentsSection = button.parentElement;
    const textarea = commentsSection.querySelector('textarea');
    const commentText = textarea.value.trim();

    if (commentText === '') {
        alert('Comment cannot be empty.');
        return;
    }

    const commentsContainer = commentsSection.querySelector('.comments');
    const comment = document.createElement('div');
    comment.className = 'comment';
    comment.textContent = commentText;

    commentsContainer.appendChild(comment);
    textarea.value = '';

    const commentCount = button.closest('.post-content').querySelector('.comment-count');
    commentCount.textContent = parseInt(commentCount.textContent) + 1;
}

function deletePost(icon) {
    const post = icon.closest('.post');
    post.remove();
}

function changeProfilePic(image) {
    // Trigger the hidden file input
    const profilePicInput = document.getElementById('profilePicInput');
    profilePicInput.dataset.target = image; // Store the target image element
    profilePicInput.click();
}

function updateProfilePic(input) {
    const file = input.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(event) {
            const targetImage = document.querySelector(`[data-target="profilePicInput"]`);
            if (targetImage) {
                targetImage.src = event.target.result; // Update the profile picture
            }
        };
        reader.readAsDataURL(file);
    }
}

function changeUsername(usernameSpan) {
    const newUsername = prompt('Enter new username:');
    if (newUsername) {
        usernameSpan.textContent = newUsername;
    }
}
function addPost() {
    const imageInput = document.getElementById('imageInput');
    const postText = document.getElementById('postText').value;

    if (imageInput.files.length === 0 || !postText.trim()) {
        alert('Please select an image and write a description.');
        return;
    }

    const reader = new FileReader();

    reader.onload = function(event) {
        const postContainer = document.getElementById('postContainer');

        const post = document.createElement('div');
        post.className = 'post';

        // 獲取當前時間並以 24 小時制格式化
        const now = new Date();
        const formattedTime = now.toLocaleString('en-GB', { 
            year: 'numeric', 
            month: '2-digit', 
            day: '2-digit', 
            hour: '2-digit', 
            minute: '2-digit', 
            second: '2-digit' 
        });

        post.innerHTML = `
            <div class="post-header">
                <img class="profile-pic" src="https://via.placeholder.com/40" alt="Profile Picture" onclick="changeProfilePic(this)">
                <span class="username" onclick="changeUsername(this)">user_account</span>
                <span class="delete-icon" onclick="deletePost(this)">❌</span>
            </div>
            <img class="post-image" src="${event.target.result}" alt="Post Image">
            <div class="post-content">
                <p class="post-text">${postText}</p>
                <p class="post-time">Posted on: ${formattedTime}</p>
                <div class="interaction-bar">
                    <button onclick="likePost(this)">❤️ Like (<span class="like-count">0</span>)</button>
                    <button onclick="sharePost('${event.target.result}')">🔗 Share</button>
                </div>
                <a class="comment-btn" href="#" onclick="toggleComments(this)">View Comments (<span class="comment-count">0</span>)</a>
                <div class="comments-section" style="display: none;">
                    <textarea placeholder="Add a comment..."></textarea>
                    <button onclick="addComment(this)">Comment</button>
                    <div class="comments"></div>
                </div>
            </div>
        `;

        postContainer.prepend(post);
    };

    reader.readAsDataURL(imageInput.files[0]);

    document.getElementById('imageInput').value = '';
    document.getElementById('postText').value = '';
    closeModal();
}
