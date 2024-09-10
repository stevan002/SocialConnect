import { Component, OnInit } from '@angular/core';
import { PostResponse } from '../../../model/PostResponse';
import { PostService } from '../../../services/post.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/user-service.service';
import { CommentService } from '../../../services/comment.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit{

  posts: PostResponse[] = [];
  currentUsername: string | null = null;

  constructor(private postService: PostService, private commentService: CommentService, private userService: UserService, private toastr: ToastrService, private router: Router){}

  ngOnInit(): void {
    this.loadPosts();
    this.currentUsername = this.userService.getUsernameFromToken();
  }

  loadPosts(): void {
    this.postService.getPosts().subscribe({
      next: (posts) => {
        this.posts = posts.map(post => ({
          ...post,
          comments: post.comments || []
        }));
        this.posts.forEach(post => this.loadCommentsForPost(post));
      },
      error: (err) => {
        console.error('Error loading posts', err);
        this.toastr.error('Error loading posts', 'Error');
      }
    });
  }

  deletePost(postId: number): void {
    this.postService.deletePost(postId).subscribe({
      next: (response) => {
        this.toastr.success(response.message, 'Success');
        this.loadPosts();
      },
      error: (err) => {
        console.log('Error deleting post', err);
        this.toastr.error('Error deleting post', 'Error');
      }
    });
  }

  loadCommentsForPost(post: PostResponse): void {
    this.commentService.getCommentsForPost(post.id).subscribe({
      next: (comments) => {
        post.comments = comments;
      },
      error: (err) => {
        console.error('Error loading comments for post', err);
      }
    });
  }

  onCommentCreated(post: PostResponse): void {
    this.loadCommentsForPost(post);
  }

  onReactionCreated(): void {
    this.loadPosts();
  }

  hideButton(post: PostResponse): boolean {
    return this.currentUsername === post.username;
  }

  goToUpdatePage(postId: number): void {
    this.router.navigate([`/posts/update/${postId}`]);
  }
}
