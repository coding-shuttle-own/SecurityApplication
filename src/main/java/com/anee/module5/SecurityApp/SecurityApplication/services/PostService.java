package com.anee.module5.SecurityApp.SecurityApplication.services;


import com.anee.module5.SecurityApp.SecurityApplication.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);
}
