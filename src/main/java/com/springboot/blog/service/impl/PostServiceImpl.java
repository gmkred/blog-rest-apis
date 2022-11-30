package com.springboot.blog.service.impl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@Service

public class PostServiceImpl implements PostService {

	private PostRepository postRepo;

	// @Autowired - it can be omitted if this class has only one constructor.
	public PostServiceImpl(PostRepository postRepo) {
		this.postRepo = postRepo;
	}

	@Override
	public PostDTO createPost(PostDTO postDto) {
		// convert DTO to Entity
		Post post = mapToEntity(postDto);
		Post newPost = postRepo.save(post);
		// convert entity into DTO
		PostDTO postdto = mapToDTO(newPost);

		return postdto;
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = postRepo.findAll(pageable);
		// get content for page object
		List<Post> listOfPost = posts.getContent();
		List<PostDTO> allPosts = listOfPost.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse(allPosts, pageNo, pageSize, posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
		return postResponse;
	}

	private PostDTO mapToDTO(Post post) {
		PostDTO postdto = new PostDTO();
		postdto.setId(post.getId());
		postdto.setTitle(post.getTitle());
		postdto.setContent(post.getContent());
		postdto.setDescription(post.getDescription());
		return postdto;
	}

	// convert DTO to Entity
	private Post mapToEntity(PostDTO postDto) {
		Post post = new Post();
		post.setId(postDto.getId());
		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());
		return post;
	}

	@Override
	public PostDTO getPostById(long id) {
		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		return mapToDTO(post);
	}

	@Override
	public PostDTO updatePost(PostDTO postdto, long id) {
		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		post.setTitle(postdto.getTitle());
		post.setDescription(postdto.getDescription());
		post.setContent(postdto.getContent());
		Post updatedPost = postRepo.save(post);
		return mapToDTO(updatedPost);

	}

	@Override
	public void deletePostById(long id) {

		Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
		postRepo.delete(post);
	}

}
