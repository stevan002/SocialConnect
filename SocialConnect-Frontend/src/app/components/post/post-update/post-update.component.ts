import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { PostResponse } from '../../../model/PostResponse';
import { UpdatePostRequest } from '../../../model/UpdatePostRequest';
import { PostService } from '../../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-post-update',
  templateUrl: './post-update.component.html',
  styleUrls: ['./post-update.component.css']
})
export class PostUpdateComponent implements OnInit {
  postId: number | null = null;
  post: PostResponse | undefined;
  updateRequest: UpdatePostRequest = {
    title: '',
    content: ''
  };

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.postId = id ? +id : null;
    this.loadPost();
  }

  loadPost(): void {
    if (this.postId !== null) {
      this.postService.getPost(this.postId).subscribe({
        next: (post) => {
          this.post = post;
          this.updateRequest.title = post.title;
          this.updateRequest.content = post.content;
        },
        error: (err) => {
          console.error('Failed to load post', err);
          this.toastr.error('Failed to load post', 'Error');
        }
      });
    } else {
      this.toastr.error('Post ID is invalid', 'Error');
      this.router.navigate(['/posts']);
    }
  }

  updatePost(): void {
    if (this.updateRequest.title.trim() && this.updateRequest.content.trim() && this.postId !== null) {
      this.postService.updatePost(this.postId, this.updateRequest).subscribe({
        next: () => {
          this.toastr.success('Post updated successfully', 'Success');
          this.router.navigate(['/posts']);
        },
        error: (err) => {
          console.error('Failed to update post', err);
          this.toastr.error('Failed to update post', 'Error');
        }
      });
    } else {
      this.toastr.error('Title and content cannot be empty', 'Validation Error');
    }
  }
}
