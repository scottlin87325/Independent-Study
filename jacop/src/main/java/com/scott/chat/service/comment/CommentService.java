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

/**
* 評論服務層
* 處理評論的新增、刪除、查詢等業務邏輯
*/
@Service
public class CommentService {

   @Autowired
   private CommentRepository commentRepository;    // 評論資料存取
   
   @Autowired
   private MemberRepository memberRepository;      // 會員資料存取
   
   @Autowired
   private PostRepository postRepository;          // 貼文資料存取
   
   @Autowired
   private PostService postService;                // 貼文服務

   /**
    * 建立新評論
    * @param postId 貼文ID
    * @param memberId 會員ID
    * @param content 評論內容
    * @return 評論DTO
    */
   @Transactional
   public CommentDTO createComment(Integer postId, Integer memberId, String content) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new RuntimeException("找不到貼文"));
       
       Member member = memberRepository.findById(memberId)
           .orElseThrow(() -> new RuntimeException("找不到會員"));
       
       // 建立評論DTO
       CommentDTO dto = new CommentDTO();
       dto.setPostId(postId);
       dto.setMemberId(memberId);
       dto.setContent(content);
       dto.setCreatedAt(new Date().toString());
       dto.setMemberName(member.getMembername());
       dto.setMemberAvatar("/members/avatar/" + memberId);
       dto.setOwnComment(true);
       dto.setLikeCount(0);
       dto.setLiked(false);
       
       // 更新貼文的評論數
       postService.updateMessageCount(postId, true);
       
       return dto;
   }

   /**
    * 刪除評論
    * @param commentId 評論ID
    * @param memberId 會員ID
    */
   @Transactional
   public void deleteComment(Long commentId, Integer memberId) {
       commentRepository.deleteById(commentId);
   }

   /**
    * 取得貼文的所有評論
    * @param postId 貼文ID
    * @return 評論DTO列表
    */
   public List<CommentDTO> getPostComments(Integer postId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new RuntimeException("找不到貼文"));
           
       List<Object[]> comments = commentRepository.findCommentsWithMemberInfo(postId);
       
       return comments.stream()
           .map(row -> {
               CommentDTO dto = new CommentDTO();
               Integer commentMemberId = (Integer)row[1];  
               
               dto.setId((Long)row[0]);           // 評論ID
               dto.setPostId(postId);
               dto.setMemberId(commentMemberId);   // 會員ID
               dto.setContent((String)row[2]);     // 評論內容
               dto.setCreatedAt(row[3].toString());// 建立時間
               dto.setMemberName((String)row[4]);  // 會員名稱
               dto.setMemberAvatar("/members/avatar/" + commentMemberId);
               dto.setOwnComment(false);           // 預設非本人評論
               dto.setLikeCount(0);
               dto.setLiked(false);
               
               return dto;
           })
           .collect(Collectors.toList());
   }
}