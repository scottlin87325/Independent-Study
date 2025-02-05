package com.scott.chat.dto.comment;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    private Long id;
    private Integer postId;
    private Integer memberId;
    
    // 評論內容和時間
    private String content;
    private String createdAt;
    
    // 評論者資訊
    private String memberName;
    private String memberAvatar;
    
    // 權限控制
    private boolean isOwnComment;
    
    // 統計資訊
    private Integer likeCount;
    private boolean isLiked;
    
    // 互動資訊（可選）
    private String lastEditTime;
    private List<ReplyDTO> replies;  // 如果支援巢狀回覆
    
    // 無參構造函數
    public CommentDTO() {
    }
    
    // 全參構造函數
    public CommentDTO(Long id, Integer postId, Integer memberId, String content, String createdAt,
                     String memberName, String memberAvatar, boolean isOwnComment,
                     Integer likeCount, boolean isLiked, String lastEditTime,
                     List<ReplyDTO> replies) {
        this.id = id;
        this.postId = postId;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = createdAt;
        this.memberName = memberName;
        this.memberAvatar = memberAvatar;
        this.isOwnComment = isOwnComment;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.lastEditTime = lastEditTime;
        this.replies = replies;
    }
    
    // Getter方法
    public Long getId() {
        return id;
    }
    
    public Integer getPostId() {
        return postId;
    }
    
    public Integer getMemberId() {
        return memberId;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getMemberName() {
        return memberName;
    }
    
    public String getMemberAvatar() {
        return memberAvatar;
    }
    
    public boolean isOwnComment() {
        return isOwnComment;
    }
    
    public Integer getLikeCount() {
        return likeCount;
    }
    
    public boolean isLiked() {
        return isLiked;
    }
    
    public String getLastEditTime() {
        return lastEditTime;
    }
    
    public List<ReplyDTO> getReplies() {
        return replies;
    }
    
    // Setter方法
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    
    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }
    
    public void setOwnComment(boolean ownComment) {
        this.isOwnComment = ownComment;
    }
    
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
    
    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }
    
    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
    
    public void setReplies(List<ReplyDTO> replies) {
        this.replies = replies;
    }
    
    // 內部類 ReplyDTO
    public static class ReplyDTO {
        private Long id;
        private String content;
        private String createdAt;
        private Integer memberId;
        private String memberName;
        private String memberAvatar;
        private boolean isOwnReply;
        
        // 無參構造函數
        public ReplyDTO() {
        }
        
        // 全參構造函數
        public ReplyDTO(Long id, String content, String createdAt, Integer memberId,
                       String memberName, String memberAvatar, boolean isOwnReply) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.memberId = memberId;
            this.memberName = memberName;
            this.memberAvatar = memberAvatar;
            this.isOwnReply = isOwnReply;
        }
        
        // Getter方法
        public Long getId() {
            return id;
        }
        
        public String getContent() {
            return content;
        }
        
        public String getCreatedAt() {
            return createdAt;
        }
        
        public Integer getMemberId() {
            return memberId;
        }
        
        public String getMemberName() {
            return memberName;
        }
        
        public String getMemberAvatar() {
            return memberAvatar;
        }
        
        public boolean isOwnReply() {
            return isOwnReply;
        }
        
        // Setter方法
        public void setId(Long id) {
            this.id = id;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
        
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
        
        public void setMemberId(Integer memberId) {
            this.memberId = memberId;
        }
        
        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }
        
        public void setMemberAvatar(String memberAvatar) {
            this.memberAvatar = memberAvatar;
        }
        
        public void setOwnReply(boolean ownReply) {
            this.isOwnReply = ownReply;
        }
    }
}