package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;

public interface PostService {

	public PostDTO createPost(PostDTO postDto);

	public PostResponse getAllPosts(int pazeNo, int pageSize, String sortBy, String sortDir);

	public PostDTO getPostById(long id);

	public PostDTO updatePost(PostDTO postdto, long id);

	public void deletePostById(long id);
}
