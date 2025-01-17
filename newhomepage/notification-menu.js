const toggleButton = document.getElementById("toggle-button");
const sidebar = document.getElementById("sidebar");

document.getElementById("hideButton").addEventListener("click", function() {
  const block = document.getElementById("close");
  if (block.style.display === "none") {
    block.style.display = "block"; // Show the block
  } else {
    block.style.display = "none"; // Hide the block
  }
});

const openIcon = toggleButton.querySelector(".bxs-right-arrow");
const closeIcon = toggleButton.querySelector(".bxs-left-arrow");

closeIcon.style.display = "none";

toggleButton.addEventListener("click", () => {
  sidebar.classList.toggle("active");

  if (sidebar.classList.contains("active")) {
    openIcon.style.display = "none";
    closeIcon.style.display = "block";
  } else {
    openIcon.style.display = "block";
    closeIcon.style.display = "none";
  }
});
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
