package com.anee.module5.SecurityApp.SecurityApplication.utils;

import com.anee.module5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.anee.module5.SecurityApp.SecurityApplication.entities.PostEntity;
import com.anee.module5.SecurityApp.SecurityApplication.entities.User;
import com.anee.module5.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwnerOfPost(Long postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }
}
