document.addEventListener('DOMContentLoaded', function () {
    // 初始化UI狀態管理
    const uiState = {
        currentIndex: 0,           // 當前輪播圖片索引
        selectedFiles: []          // 已選擇的文件列表
    };
 
    // 獲取所有需要的DOM元素
    const modal = document.getElementById('createPostModal');                   // 創建貼文的模態框
    const uploadArea = document.getElementById('uploadArea');                  // 上傳區域
    const carouselContainer = document.getElementById('carouselContainer');    // 輪播容器
    const carousel = document.getElementById('carousel');                      // 輪播組件
    const prevBtn = document.getElementById('prevBtn');                       // 上一張按鈕
    const nextBtn = document.getElementById('nextBtn');                       // 下一張按鈕
    const indicators = document.getElementById('indicators');                 // 輪播指示器
    const fileInput = document.getElementById('fileInput');                  // 文件輸入框
    const createPostBtn = document.getElementById('createPostBtn');          // 創建貼文按鈕
    const shareBtn = document.querySelector('.share-btn');                   // 分享按鈕
    const cancelBtn = document.querySelector('.cancel-btn');                 // 取消按鈕
    const postText = document.getElementById('postText');                    // 貼文文字輸入框
    const notificationsModal = document.getElementById('notificationsModal'); // 通知模態框
 
    // 初始化通知系統
    let notifications = [];  // 存儲通知的數組
    const notificationBtn = document.querySelector('.menu-item[data-page="notifications"]');
 
    // 獲取頁面切換相關元素
    const menuItems = document.querySelectorAll('.menu-item[data-page]');
    const pages = {
        'home': document.getElementById('home-content'),         // 首頁內容
        'search': document.getElementById('search-content'),     // 搜尋內容
        'messages': document.getElementById('messages-content'), // 訊息內容
        'notifications': document.getElementById('notifications-content'), // 通知內容
        'profile': document.getElementById('profile-content')    // 個人檔案內容
    };
 
    // 頁面切換功能
    function switchPage(pageId) {
        // 隱藏所有頁面
        Object.values(pages).forEach(page => {
            if (page) page.classList.remove('active');
        });
 
        // 顯示當前選中的頁面
        const currentPage = pages[pageId];
        if (currentPage) currentPage.classList.add('active');
 
        // 更新選單項目狀態
        menuItems.forEach(item => {
            item.classList.remove('active');
            if (item.dataset.page === pageId) {
                item.classList.add('active');
            }
        });
 
        // 保存當前頁面到本地存儲
        localStorage.setItem('currentPage', pageId);
    }
 
    // 通知按鈕點擊事件處理
    notificationBtn.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        notificationsModal.classList.toggle('active');
        e.target.closest('.menu-item').classList.remove('active');
    });
 
    // 點擊其他地方關閉通知模態框
    document.addEventListener('click', (e) => {
        if (notificationsModal.classList.contains('active') &&
            !notificationsModal.querySelector('.notifications-wrapper').contains(e.target) &&
            !notificationBtn.contains(e.target)) {
            notificationsModal.classList.remove('active');
        }
    });
 
    // 監聽選單項目點擊
    menuItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const pageId = item.dataset.page;
            if (pageId && pageId !== 'notifications') {
                switchPage(pageId);
            }
        });
    });
 
    // 添加新通知的函數
    function addNotification(type, user, postId, content) {
        const notification = {
            id: Date.now(),
            type,
            user,
            postId,
            content,
            read: false,
            time: new Date()
        };
 
        notifications.unshift(notification);
        updateNotificationsUI();
        updateNotificationCount();
    }
 
    // 更新通知UI的函數
    function updateNotificationsUI() {
        const notificationsList = document.querySelector('.notifications-list');
        notificationsList.innerHTML = notifications.map(notification => `
            <div class="notification-item ${notification.read ? '' : 'unread'}" 
                 data-notification-id="${notification.id}" 
                 data-post-id="${notification.postId}">
                <img src="${notification.user.avatar}" class="notification-avatar" alt="${notification.user.name}">
                <div class="notification-content">
                    <div class="notification-text">
                        <strong>${notification.user.name}</strong> 
                        ${notification.type === 'like' ? '喜歡了你的貼文' : '評論了你的貼文'}
                        ${notification.content ? `："${notification.content}"` : ''}
                    </div>
                    <div class="notification-time">
                        ${formatTime(notification.time)}
                    </div>
                </div>
            </div>
        `).join('');
 
        addNotificationListeners();
    }
 
    // 更新通知數量標記
    function updateNotificationCount() {
        const unreadCount = notifications.filter(n => !n.read).length;
        const notificationBadge = notificationBtn.querySelector('.notification-badge');
        
        if (unreadCount > 0) {
            if (!notificationBadge) {
                const badge = document.createElement('span');
                badge.className = 'notification-badge';
                badge.textContent = unreadCount;
                notificationBtn.appendChild(badge);
            } else {
                notificationBadge.textContent = unreadCount;
            }
        } else if (notificationBadge) {
            notificationBadge.remove();
        }
    }
 
    // 添加通知點擊監聽器
    function addNotificationListeners() {
        document.querySelectorAll('.notification-item').forEach(item => {
            item.addEventListener('click', () => {
                const notificationId = parseInt(item.dataset.notificationId);
                const postId = item.dataset.postId;
                markAsRead(notificationId);
                scrollToPost(postId);
                notificationsModal.classList.remove('active');
            });
        });
    }
 
    // 創建貼文按鈕點擊事件
    createPostBtn.addEventListener('click', () => {
        modal.style.display = 'flex';
    });
 
    // 取消按鈕點擊事件
    cancelBtn.addEventListener('click', () => {
        modal.style.display = 'none';
        resetModal();
    });

    // 點擊模態框外部關閉
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
            resetModal();
        }
    });

    // 文件選擇處理
    fileInput.addEventListener('change', handleFileSelect);

    // 拖放區域事件處理
    uploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadArea.style.border = '2px dashed #0095f6';
    });
 
    uploadArea.addEventListener('dragleave', (e) => {
        e.preventDefault();
        uploadArea.style.border = 'none';
    });
 
    uploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadArea.style.border = 'none';
        const files = Array.from(e.dataTransfer.files).filter(file => file.type.startsWith('image/'));
        validateAndHandleFiles(files);
    });
 
    // 輪播控制事件
    prevBtn.addEventListener('click', () => {
        uiState.currentIndex = Math.max(0, uiState.currentIndex - 1);
        updateCarousel();
    });
 
    nextBtn.addEventListener('click', () => {
        uiState.currentIndex = Math.min(uiState.selectedFiles.length - 1, uiState.currentIndex + 1);
        updateCarousel();
    });
 
    // 檔案驗證和處理
    function validateAndHandleFiles(files) {
        const imageFiles = Array.from(files).filter(file => file.type.startsWith('image/'));
 
        if (imageFiles.length === 0) {
            alert('請至少選擇一張圖片');
            return;
        }
 
        uiState.selectedFiles = imageFiles;
        uploadArea.style.display = 'none';
        carouselContainer.style.display = 'block';
        shareBtn.disabled = false;
        uiState.currentIndex = 0;
        updateCarousel();
    }
 
    // 文件選擇處理函數
    function handleFileSelect(e) {
        validateAndHandleFiles(e.target.files);
    }
 
    // 更新輪播顯示
    function updateCarousel() {
        carousel.innerHTML = '';
        indicators.innerHTML = '';
 
        uiState.selectedFiles.forEach((file, index) => {
            const img = document.createElement('img');
            img.src = URL.createObjectURL(file);
 
            // 圖片載入後處理圖片大小比例
            img.onload = function () {
                const imgAspectRatio = img.naturalWidth / img.naturalHeight;
                const containerAspectRatio = carousel.clientWidth / carousel.clientHeight;
 
                if (imgAspectRatio > containerAspectRatio) {
                    img.style.width = '100%';
                    img.style.height = 'auto';
                } else {
                    img.style.width = 'auto';
                    img.style.height = '100%';
                }
            };
            carousel.appendChild(img);
 
            // 創建輪播指示器
            const indicator = document.createElement('div');
            indicator.className = `indicator ${index === uiState.currentIndex ? 'active' : ''}`;
            indicator.addEventListener('click', () => {
                uiState.currentIndex = index;
                updateCarousel();
            });
            indicators.appendChild(indicator);
        });
 
        // 更新輪播位置
        carousel.style.transform = `translateX(-${uiState.currentIndex * 100}%)`;
 
        // 更新導航按鈕顯示狀態
        prevBtn.style.display = uiState.currentIndex > 0 ? 'block' : 'none';
        nextBtn.style.display = uiState.currentIndex < uiState.selectedFiles.length - 1 ? 'block' : 'none';
    }
 
    // 設置貼文輪播功能
    function setupPostCarousel(postElement) {
        const carousel = postElement.querySelector('.carousel');
        const prevBtn = postElement.querySelector('.carousel-btn.prev');
        const nextBtn = postElement.querySelector('.carousel-btn.next');
        const indicators = postElement.querySelectorAll('.indicator');
        let currentSlide = 0;
 
        // 處理圖片載入和大小調整
        const images = postElement.querySelectorAll('.carousel img');
        images.forEach(img => {
            img.onload = function() {
                const imgAspectRatio = img.naturalWidth / img.naturalHeight;
                const containerAspectRatio = carousel.clientWidth / carousel.clientHeight;
                
                if (imgAspectRatio > containerAspectRatio) {
                    img.style.width = '100%';
                    img.style.height = 'auto';
                } else {
                    img.style.width = 'auto';
                    img.style.height = '100%';
                }
            };
        });
 
        // 如果只有一張圖片，隱藏導航按鈕
        if (images.length <= 1) {
            if (prevBtn) prevBtn.style.display = 'none';
            if (nextBtn) nextBtn.style.display = 'none';
            return;
        }

        // 更新輪播圖片顯示
        function updateSlide() {
            carousel.style.transform = `translateX(-${currentSlide * 100}%)`;
            indicators.forEach((indicator, index) => {
                indicator.classList.toggle('active', index === currentSlide);
            });
            prevBtn.style.display = currentSlide > 0 ? 'flex' : 'none';
            nextBtn.style.display = currentSlide < images.length - 1 ? 'flex' : 'none';
        }
 
        // 上一張按鈕點擊事件
        prevBtn?.addEventListener('click', (e) => {
            e.stopPropagation();
            if (currentSlide > 0) {
                currentSlide--;
                updateSlide();
            }
        });
 
        // 下一張按鈕點擊事件
        nextBtn?.addEventListener('click', (e) => {
            e.stopPropagation();
            if (currentSlide < images.length - 1) {
                currentSlide++;
                updateSlide();
            }
        });
 
        // 輪播指示器點擊事件
        indicators.forEach((indicator, index) => {
            indicator.addEventListener('click', (e) => {
                e.stopPropagation();
                currentSlide = index;
                updateSlide();
            });
        });
 
        // 初始化輪播顯示
        updateSlide();
    }
 
    // 重置模態框狀態
    function resetModal() {
        fileInput.value = '';              // 清空文件輸入
        postText.value = '';               // 清空文字輸入
        uiState.selectedFiles = [];        // 清空已選擇的文件
        uiState.currentIndex = 0;          // 重置輪播索引
        uploadArea.style.display = 'block'; // 顯示上傳區域
        carouselContainer.style.display = 'none'; // 隱藏輪播容器
        carousel.innerHTML = '';           // 清空輪播內容
        indicators.innerHTML = '';         // 清空指示器
        shareBtn.disabled = true;          // 禁用分享按鈕
        uploadArea.style.border = 'none';  // 重置上傳區域邊框
    }
 
    // 將通知標記為已讀
    function markAsRead(notificationId) {
        const notification = notifications.find(n => n.id === notificationId);
        if (notification) {
            notification.read = true;
            updateNotificationsUI();
            updateNotificationCount();
        }
    }
 
    // 滾動到指定貼文
    function scrollToPost(postId) {
        const post = document.querySelector(`.post[data-post-id="${postId}"]`);
        if (post) {
            post.scrollIntoView({ behavior: 'smooth' });
            post.classList.add('highlight');
            setTimeout(() => post.classList.remove('highlight'), 2000);
        }
    }
 
    // 格式化時間顯示
    function formatTime(date) {
        const now = new Date();
        const diff = now - date;
        const minutes = Math.floor(diff / 60000);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);
 
        if (minutes < 60) return `${minutes} 分鐘前`;
        if (hours < 24) return `${hours} 小時前`;
        return `${days} 天前`;
    }
 
    // 導出UI助手函數供其他模組使用
    window.uiHelpers = {
        getSelectedFiles: () => uiState.selectedFiles,
        postText,
        modal,
        resetModal,
        addNotification,
        setupPostCarousel
    };
 
    // 初始化頁面
    const savedPage = localStorage.getItem('currentPage');
    if (savedPage && pages[savedPage]) {
        switchPage(savedPage);
    } else {
        switchPage('home');
    }
});