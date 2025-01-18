let page = 1;
        const imagesPerPage = 12;
        let isLoading = false;
        let currentSearchTerm = '';

        // 模擬從 API 獲取數據的函數
        async function fetchImages(pageNum, searchTerm) {
            // 實際使用時替換成你的 API
            // return fetch(`你的API網址/images?page=${pageNum}&search=${searchTerm}&limit=${imagesPerPage}`)
            //     .then(response => response.json());
            
            // 模擬搜尋結果
            return new Promise(resolve => {
                setTimeout(() => {
                    // 模擬搜尋邏輯
                    if (searchTerm.trim() === '') {
                        resolve([]);
                        return;
                    }
                    
                    resolve(Array.from({ length: imagesPerPage }, (_, i) => ({
                        id: (pageNum - 1) * imagesPerPage + i + 1,
                        url: `https://picsum.photos/400/300?random=${(pageNum - 1) * imagesPerPage + i + 1}`,
                        title: `${searchTerm} - 圖片 ${(pageNum - 1) * imagesPerPage + i + 1}`
                    })));
                }, 500);
            });
        }

        function createImageElement(image) {
            const div = document.createElement('div');
            div.className = 'image-box';
            
            div.innerHTML = `
                <img src="${image.url}" alt="${image.title}">
                <div class="image-title">${image.title}</div>
            `;
            
            return div;
        }

        async function loadImages() {
            if (isLoading) return;
            
            const btn = document.getElementById('loadMoreBtn');
            btn.disabled = true;
            btn.textContent = '載入中...';
            isLoading = true;

            try {
                const images = await fetchImages(page, currentSearchTerm);
                const container = document.getElementById('gridContainer');
                
                if (images.length === 0 && page === 1) {
                    container.innerHTML = '<div class="no-results">沒有找到相關圖片</div>';
                    btn.style.display = 'none';
                    return;
                }
                
                images.forEach(image => {
                    container.appendChild(createImageElement(image));
                });
                
                btn.style.display = images.length === imagesPerPage ? 'block' : 'none';
                page++;
            } catch (error) {
                console.error('載入圖片失敗:', error);
            }

            btn.disabled = false;
            btn.textContent = '載入更多';
            isLoading = false;
        }

        function startNewSearch() {
            const searchTerm = document.getElementById('searchInput').value;
            if (searchTerm.trim() === currentSearchTerm) return;
            
            currentSearchTerm = searchTerm.trim();
            page = 1;
            const container = document.getElementById('gridContainer');
            container.innerHTML = '';
            loadImages();
        }

        // 事件監聽
        document.getElementById('loadMoreBtn').addEventListener('click', loadImages);
        document.getElementById('searchButton').addEventListener('click', startNewSearch);
        
        // 按 Enter 鍵也可以搜尋
        document.getElementById('searchInput').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                startNewSearch();
            }
        });