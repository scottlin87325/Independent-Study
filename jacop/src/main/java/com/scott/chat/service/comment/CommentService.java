package com.scott.chat.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scott.chat.dto.comment.CommentDTO;
import com.scott.chat.model.Comment;
import com.scott.chat.model.Member;
import com.scott.chat.model.Post;
import com.scott.chat.repository.CommentRepository;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.service.post.PostService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

// 評論服務類，處理所有與評論相關的業務邏輯
@Service
public class CommentService {

   // 注入所需的資源庫
   @Autowired
   private CommentRepository commentRepository;
   
   @Autowired
   private MemberRepository memberRepository;
   
   @Autowired
   private PostRepository postRepository;
   
   @Autowired
   private PostService postService;

   // 創建新評論
   @Transactional
   public CommentDTO createComment(Integer postId, Integer memberId, String content) {
       // 檢查貼文是否存在
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new RuntimeException("Post not found"));
       // 檢查用戶是否存在
       Member member = memberRepository.findById(memberId)
           .orElseThrow(() -> new RuntimeException("Member not found"));
           
       Comment comment = new Comment();
       // 使用反射設置評論屬性
       try {
           comment.getClass().getDeclaredField("post").set(comment, post);
           comment.getClass().getDeclaredField("member").set(comment, member);
           comment.getClass().getDeclaredField("content").set(comment, content);
           comment.getClass().getDeclaredField("createdAt").set(comment, new Date());
       } catch (Exception e) {
           throw new RuntimeException("Error setting comment fields", e);
       }
       
       // 保存評論並更新貼文的評論計數
       Comment savedComment = commentRepository.save(comment);
       postService.updateMessageCount(postId, true);
       
       // 將評論轉換為DTO返回
       return CommentDTO.builder()
           .postId(postId)
           .memberId(memberId)
           .content(content)
           .memberName(member.getMembername())
           .memberAvatar("/members/avatar/" + member.getMemberid())
           .createdAt(new Date().toString())
           .build();
   }

   // 刪除評論
   @Transactional
   public void deleteComment(Long commentId, Integer memberId) {
       // 查找評論
       Comment comment = commentRepository.findById(commentId)
           .orElseThrow(() -> new RuntimeException("Comment not found"));
           
       // 檢查權限
       Member commentMember;
       try {
           commentMember = (Member) comment.getClass().getDeclaredField("member").get(comment);
       } catch (Exception e) {
           throw new RuntimeException("Error getting comment member", e);
       }
       
       if (!commentMember.getMemberid().equals(memberId)) {
           throw new RuntimeException("Not authorized to delete this comment");
       }
       
       // 刪除評論
       commentRepository.delete(comment);
       
       // 更新貼文的評論計數
       try {
           Post post = (Post) comment.getClass().getDeclaredField("post").get(comment);
           postService.updateMessageCount(post.getPostid(), false);
       } catch (Exception e) {
           throw new RuntimeException("Error getting comment post", e);
       }
   }

   // 獲取貼文的所有評論
   public List<CommentDTO> getPostComments(Integer postId) {
       // 檢查貼文是否存在
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new RuntimeException("Post not found"));
           
       // 獲取並轉換所有評論
       return commentRepository.findByPostOrderByCreatedAtDesc(post)
           .stream()
           .map(comment -> {
               try {
                   // 使用反射獲取評論的相關資訊
                   Member member = (Member) comment.getClass().getDeclaredField("member").get(comment);
                   Post commentPost = (Post) comment.getClass().getDeclaredField("post").get(comment);
                   String content = (String) comment.getClass().getDeclaredField("content").get(comment);
                   Date createdAt = (Date) comment.getClass().getDeclaredField("createdAt").get(comment);
                   
                   // 構建並返回DTO
                   return CommentDTO.builder()
                       .id((Long) comment.getClass().getDeclaredField("id").get(comment))
                       .postId(commentPost.getPostid())
                       .memberId(member.getMemberid())
                       .content(content)
                       .createdAt(createdAt.toString())
                       .memberName(member.getMembername())
                       .memberAvatar("/members/avatar/" + member.getMemberid())
                       .build();
               } catch (Exception e) {
                   throw new RuntimeException("Error converting comment to DTO", e);
               }
           })
           .collect(Collectors.toList());
   }
}