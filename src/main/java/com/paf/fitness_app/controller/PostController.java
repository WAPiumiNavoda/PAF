package com.paf.fitness_app.controller;

;
import com.paf.fitness_app.entity.PostNewEntity;
import com.paf.fitness_app.service.PostService;
import com.paf.fitness_app.service.UserService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public static String uploadfileDir = System.getProperty("user.dir") + "/src/main/resources/static" + "/";

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }



    @PostMapping("/add")
    public ResponseEntity<?> savenewPost(
            @ModelAttribute PostNewEntity postnewEntity,
            @RequestParam("image") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User principal = oauthToken.getPrincipal();
            String userEmail = principal.getAttribute("email");

            // Convert MultipartFile to String (filename)
            String filename = file.getOriginalFilename();
            Path filePath = Paths.get(uploadfileDir, filename);
            Files.write(filePath ,file.getBytes());


            if (userEmail != null) {
                postnewEntity.setPostimage(filename);
                postnewEntity.setUserEmail(userEmail);
                PostNewEntity savedPostEntity = postService.saveData(postnewEntity);
                return ResponseEntity.ok(savedPostEntity);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User email not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 authentication token not found");
        }
    }



    @GetMapping("/posts/getPost/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws IOException {

        PostNewEntity postEntity = postService.getPostById(id);
        Path imagePath = Paths.get(uploadfileDir , postEntity.getPostimage());
        Resource resource = new FileSystemResource(imagePath.toFile());
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }



    @GetMapping("/posts")
    public ResponseEntity<List<PostNewEntity>> getPostsForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User principal = oauthToken.getPrincipal();
            String userEmail = principal.getAttribute("email");

            if (userEmail != null) {
                List<PostNewEntity> userPosts = postService.getPostsByUserEmail(userEmail);
                return ResponseEntity.ok(userPosts);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostNewEntity> updatePost(@PathVariable Long id, @ModelAttribute PostNewEntity postnewEntity,
                                                    @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            // Fetch the existing post by id
            PostNewEntity existingPost = postService.findById(id);
            if (existingPost == null) {
                return ResponseEntity.notFound().build();
            }

            // Update the fields if provided
            if (postnewEntity.getPostname() != null) {
                existingPost.setPostname(postnewEntity.getPostname());
            }
            // Update other fields similarly...

            // Update the image if provided
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                Path filePath = Paths.get(uploadfileDir, filename);
                Files.write(filePath, file.getBytes());
                existingPost.setPostimage(filename);
            }

            // Save the updated post
            PostNewEntity updatedPost = postService.saveData(existingPost);
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            e.printStackTrace(); // Handle file IO exception
            return ResponseEntity.badRequest().build();
        }
    }

}
