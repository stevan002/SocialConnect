import { Component, Input, OnInit } from '@angular/core';
import { CommentResponse } from '../../../model/CommentResponse';
import { UserService } from '../../../services/user-service.service';
import { CommentService } from '../../../services/comment.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {
  currentUsername: string | null = null;
  @Input() comments: CommentResponse[] = [];
  @Input() postId?: number;

  constructor(
    private userService: UserService,
    private commentService: CommentService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUsername = this.userService.getUsernameFromToken();
  }

  canDeleteComment(comment: CommentResponse): boolean {
    return this.currentUsername === comment.username;
  }

  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe({
      next: (response) => {
        this.toastr.success(response.message, 'Success');
        this.comments = this.comments.filter(comment => comment.id !== commentId);
      },
      error: (err) => {
        console.error('Error deleting comment', err);
        this.toastr.error('Error deleting comment', 'Error');
      }
    });
  }

  onReactionCreated(): void {
    if (this.postId) {
      this.loadCommentsForPost();
    }
  }

  loadCommentsForPost(): void {
    if (this.postId) {
      this.commentService.getCommentsForPost(this.postId).subscribe({
        next: (comments) => {
          this.comments = comments;
        },
        error: (err) => {
          console.error('Error loading comments', err);
          this.toastr.error('Error loading comments', 'Error');
        }
      });
    }
  }

  goToUpdatePage(commentId: number): void {
    this.router.navigate([`comments/update/${commentId}`]);
  }
}
