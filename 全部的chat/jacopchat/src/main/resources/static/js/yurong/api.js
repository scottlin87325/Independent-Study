document.addEventListener('DOMContentLoaded', function () {
    // 初始化分頁相關變量
    let currentPage = 0;
    let loading = false;
    let hasMore = true;
    
    // 獲取分享按鈕元素和通知輔助函數
    const shareBtn = document.querySelector('.share-btn');
    const { addNotification } = window.uiHelpers;

    // 定義所有API端點
    const API = {
        createPost: '/api/posts',                                         // 建立貼文
        getPost: (id) => `/api/posts/${id}`,                            // 獲取特定貼文
        getPosts: '/api/posts',                                         // 獲取貼文列表
        updatePost: (id) => `/api/posts/${id}`,                        // 更新貼文
        deletePost: (id) => `/api/posts/${id}`,                        // 刪除貼文
        updateLike: (id) => `/api/posts/${id}/like`,                   // 更新讚數
        addComment: (id) => `/api/posts/${id}/comments`,               // 新增評論
        deleteComment: (postId, commentId) => 
            `/api/posts/${postId}/comments/${commentId}`                // 刪除評論
    };

    // 包含認證的fetch請求函數
    const fetchWithAuth = async (url, options = {}) => {
        const defaultOptions = {
            credentials: 'include',
            headers: {
                ...options.headers
            }
        };
        const response = await fetch(url, { ...defaultOptions, ...options });
        if (!response.ok) {
            throw new Error(await response.text());
        }
        return response;
    };

    // 更新貼文讚數的函數
    async function updateLike(postId, isLike) {
        try {
            await fetchWithAuth(API.updateLike(postId) + `?isLike=${isLike}`, {
                method: 'PUT'
            });
        } catch (error) {
            throw new Error('Failed to update like');
        }
    }

    // 刪除貼文的函數
    async function deletePost(postId) {
        try {
            await fetchWithAuth(API.deletePost(postId), {
                method: 'DELETE'
            });
        } catch (error) {
            throw new Error('Failed to delete post');
        }
    }

    // 編輯貼文的函數
    async function editPost(postId, content) {
        const formData = new URLSearchParams();
        formData.append('content', content);

        try {
            const response = await fetchWithAuth(API.updatePost(postId), {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData
            });
            return response.json();
        } catch (error) {
            throw new Error('Failed to update post');
        }
    }

    // 監聽分享按鈕點擊事件
    shareBtn.addEventListener('click', createPost);

    // 建立新貼文的函數
    async function createPost() {
        const { getSelectedFiles, postText, modal, resetModal } = window.uiHelpers;
        const selectedFiles = getSelectedFiles();

        // 準備表單數據
        const formData = new FormData();
        formData.append('content', postText.value);
        if (selectedFiles && selectedFiles.length > 0) {
            selectedFiles.forEach(file => formData.append('photos', file));
        }

        try {
            // 發送建立貼文請求
            const response = await fetchWithAuth(API.createPost, {
                method: 'POST',
                body: formData
            });

            const post = await response.json();
            const postElement = createPostElement(post);
            document.getElementById('postContainer').prepend(postElement);

            // 重置模態框
            modal.style.display = 'none';
            resetModal();
        } catch (error) {
            console.error('Error:', error);
            alert('發布貼文失敗');
        }
    }

    // 建立貼文DOM元素的函數
    function createPostElement(post) {
        const postElement = document.createElement('div');
        postElement.className = 'post';
        postElement.dataset.postId = post.postId;

        // 生成貼文HTML結構
        postElement.innerHTML = `
           <div class="post-header">
                 <div class="post-header-left">
                         <img src="${post.posterAvatar || 'https://via.placeholder.com/32'}" alt="${post.posterName}">
                            <span class="username">${post.posterName}</span>
                 </div>
                <div class="post-header-right">
                    <span class="post-time">${post.postTime}</span>
                    ${post.ownPost ? `
                        <button class="edit-post-btn">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="delete-post-btn">
                            <i class="fas fa-times"></i>
                        </button>
                    ` : ''}
                </div>
            </div>
            <div class="post-carousel">
                <div class="carousel-wrapper">
                    <div class="carousel">
                        ${post.photoUrls.map(url =>
                            `<img src="${url}" alt="Post Image">`
                        ).join('')}
                    </div>
                    ${post.photoUrls.length > 1 ? `
                        <button class="carousel-btn prev">
                            <i class="fas fa-chevron-left"></i>
                        </button>
                        <button class="carousel-btn next">
                            <i class="fas fa-chevron-right"></i>
                        </button>
                        <div class="carousel-indicators">
                            ${post.photoUrls.map((_, i) =>
                                `<div class="indicator ${i === 0 ? 'active' : ''}"></div>`
                            ).join('')}
                        </div>
                    ` : ''}
                </div>
            </div>
            <div class="post-actions">
                <div class="action-buttons">
                    <div class="action-buttons-left">
                        <button class="like-btn">
                            <i class="${post.isLiked ? 'fas' : 'far'} fa-heart"></i>
                        </button>
                        <button class="comment-btn">
                            <i class="far fa-comment"></i>
                        </button>
                        <button class="share-post-btn">
                            <i class="far fa-paper-plane"></i>
                        </button>
                    </div>
                    <div class="action-buttons-right">
                        <button class="bookmark-btn">
                            <i class="${post.isCollected ? 'fas' : 'far'} fa-bookmark"></i>
                        </button>
                    </div>
                </div>
                <div class="likes-count">${post.likedCount || 0} 個讚</div>
                <div class="post-caption">
                    <span class="username">${post.posterName}</span>
                    <span class="caption-text">${post.postContent}</span>
                </div>
            </div>
            <div class="comments-section" style="display: none;">
                <div class="comments-list"></div>
                <div class="add-comment">
                    <input type="text" placeholder="添加留言..." class="comment-input">
                    <button class="post-comment-btn">發布</button>
                </div>
            </div>`;

        // 如果是自己的貼文，添加刪除功能
        if (post.ownPost) {
            const deleteBtn = postElement.querySelector('.delete-post-btn');
            deleteBtn?.addEventListener('click', async () => {
                if (confirm('確定要刪除這篇貼文嗎？')) {
                    try {
                        await deletePost(post.postId);
                        postElement.remove();
                    } catch (error) {
                        console.error('Error:', error);
                        alert('刪除貼文失敗');
                    }
                }
            });

            // 添加編輯功能
            const editBtn = postElement.querySelector('.edit-post-btn');
            editBtn?.addEventListener('click', async () => {
                const newContent = prompt('編輯貼文:', post.postContent);
                if (newContent !== null && newContent !== post.postContent) {
                    try {
                        const updatedPost = await editPost(post.postId, newContent);
                        postElement.querySelector('.caption-text').textContent = updatedPost.postContent;
                        post.postContent = updatedPost.postContent;
                    } catch (error) {
                        console.error('Error:', error);
                        alert('更新貼文失敗');
                    }
                }
            });
        }

        // 處理讚按鈕相關功能
        const likeBtn = postElement.querySelector('.like-btn');
        const likesCountElement = postElement.querySelector('.likes-count');
        let likeCount = post.likedCount || 0;

        likeBtn?.addEventListener('click', async () => {
            const icon = likeBtn.querySelector('i');
            const wasLiked = icon.classList.contains('fas');

            try {
                await updateLike(post.postId, !wasLiked);

                if (icon.classList.contains('far')) {
                    icon.classList.remove('far');
                    icon.classList.add('fas');
                    icon.style.color = '#ed4956';
                    likeCount++;
                    addNotification('like', {
                        name: post.posterName,
                        avatar: post.posterAvatar
                    }, post.postId, '');
                } else {
                    icon.classList.remove('fas');
                    icon.classList.add('far');
                    icon.style.color = '';
                    likeCount--;
                }
                likesCountElement.textContent = `${likeCount} 個讚`;
            } catch (error) {
                console.error('Error:', error);
                alert('更新讚數失敗');
            }
        });

        // 處理評論相關功能
        const commentBtn = postElement.querySelector('.comment-btn');
        const commentsSection = postElement.querySelector('.comments-section');
        const commentInput = postElement.querySelector('.comment-input');
        const postCommentBtn = postElement.querySelector('.post-comment-btn');
        const commentsList = postElement.querySelector('.comments-list');

        commentBtn?.addEventListener('click', () => {
            commentsSection.style.display =
                commentsSection.style.display === 'none' ? 'block' : 'none';
        });

        // 處理分享按鈕功能
        const shareBtn = postElement.querySelector('.share-post-btn');
        shareBtn?.addEventListener('click', () => {
            alert('已複製貼文連結！');
        });

        // 處理收藏按鈕功能
        const bookmarkBtn = postElement.querySelector('.bookmark-btn');
        bookmarkBtn?.addEventListener('click', () => {
            const icon = bookmarkBtn.querySelector('i');
            if (icon.classList.contains('far')) {
                icon.classList.remove('far');
                icon.classList.add('fas');
                bookmarkBtn.setAttribute('title', '取消收藏');
            } else {
                icon.classList.remove('fas');
                icon.classList.add('far');
                bookmarkBtn.setAttribute('title', '收藏');
            }
        });

        // 添加評論的函數
        async function addComment(content) {
            try {
                const response = await fetchWithAuth(API.addComment(post.postId), {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({ content })
                });
                
                const comment = await response.json();
                const commentElement = document.createElement('div');
                commentElement.className = 'comment';
                commentElement.innerHTML = `
                    <img src="${comment.memberAvatar || 'https://via.placeholder.com/24'}" alt="${comment.memberName}" style="width: 24px; height: 24px; border-radius: 50%;">
                    <div class="comment-content">
                        <span class="comment-username">${comment.memberName}</span>
                        <span class="comment-text">${comment.content}</span>
                        ${comment.isOwnComment ? `
                            <button class="delete-comment-btn">
                                <i class="fas fa-times"></i>
                            </button>
                        ` : ''}
                    </div>
                `;

                // 如果是自己的評論，添加刪除功能
                if (comment.isOwnComment) {
                    const deleteCommentBtn = commentElement.querySelector('.delete-comment-btn');
                    deleteCommentBtn?.addEventListener('click', async () => {
                        if (confirm('確定要刪除這則評論嗎？')) {
                            try {
                                await fetchWithAuth(API.deleteComment(post.postId, comment.id), {
                                    method: 'DELETE'
                                });
                                commentElement.remove();
                            } catch (error) {
                                console.error('Error:', error);
                                alert('刪除評論失敗');
                            }
                        }
                    });
                }

                commentsList.appendChild(commentElement);
                addNotification('comment', {
                    name: comment.memberName,
                    avatar: comment.memberAvatar
                }, post.postId, content);
            } catch (error) {
                console.error('Error:', error);
                alert('發布評論失敗');
            }
        }

        // 監聽發布評論按鈕
        postCommentBtn?.addEventListener('click', () => {
            const commentText = commentInput.value.trim();
            if (commentText) {
                addComment(commentText);
                commentInput.value = '';
            }
        });

        // 設置貼文輪播功能
        const { setupPostCarousel } = window.uiHelpers;
        setupPostCarousel(postElement);

        return postElement;
    }

    // 獲取貼文列表的函數
    async function getPostList() {
        if (loading || !hasMore) return;
        
        try {
            loading = true;
            console.log('獲取貼文列表，頁碼：', currentPage);
         // 處理找不到容器的情況
         const container = document.getElementById('postContainer');
         if (!container) {
             console.error('找不到 postContainer 元素');
             return;
         }

         // 顯示載入中動畫
         const loadingSpinner = document.createElement('div');
         loadingSpinner.className = 'loading-spinner';
         container.appendChild(loadingSpinner);

         // 從API獲取貼文數據
         const response = await fetchWithAuth(`${API.getPosts}?page=${currentPage}&size=10`);
         const posts = await response.json();
         
         // 移除載入中動畫
         loadingSpinner.remove();
         
         // 處理沒有更多貼文的情況
         if (posts.length === 0) {
             hasMore = false;
             if (currentPage === 0) {
                 container.innerHTML = '<p>目前沒有貼文</p>';
             }
             return;
         }

         // 將獲取的貼文添加到容器中
         posts.forEach(post => {
             const postElement = createPostElement(post);
             container.appendChild(postElement);
         });

         currentPage++;
     } catch (error) {
         console.error('錯誤:', error);
         alert('獲取貼文列表失敗');
     } finally {
         loading = false;
     }
 }

 // 添加滾動監聽，實現無限滾動
 window.addEventListener('scroll', () => {
     if ((window.innerHeight + window.scrollY) >= document.documentElement.scrollHeight - 100) {
         getPostList();
     }
 });

 // 初始加載貼文列表
 getPostList();
});