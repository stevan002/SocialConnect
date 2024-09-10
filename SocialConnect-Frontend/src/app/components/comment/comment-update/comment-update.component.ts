import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommentResponse } from '../../../model/CommentResponse';
import { CommentService } from '../../../services/comment.service';
import { UdpateCommentRequest } from '../../../model/UpdateCommentRequest';

@Component({
  selector: 'app-comment-update',
  templateUrl: './comment-update.component.html',
  styleUrl: './comment-update.component.css'
})
export class CommentUpdateComponent implements OnInit{
  commentId!: number;
  comment: CommentResponse | null = null;
  updateRequest: UdpateCommentRequest = { text: '' };

  constructor(
    private route: ActivatedRoute,
    private commentService: CommentService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.commentId = +this.route.snapshot.paramMap.get('id')!;
    this.loadComment();
  }

  loadComment(): void {
    this.commentService.getComment(this.commentId).subscribe({
      next: (comment) => {
        this.comment = comment;
        this.updateRequest.text = comment.text;
      },
      error: (err) => {
        console.error('Error loading comment', err);
        this.toastr.error('Error loading comment', 'Error');
      }
    });
  }

  updateComment(): void {
    if (!this.updateRequest.text.trim()) {
      this.toastr.error('Comment text cannot be empty', 'Error');
      return;
    }

    this.commentService.updateComment(this.commentId, this.updateRequest).subscribe({
      next: (response) => {
        this.toastr.success(response.message, 'Success');
        this.router.navigate(['/comments']);
      },
      error: (err) => {
        console.error('Error updating comment', err);
        this.toastr.error('Error updating comment', 'Error');
      }
    });
  }
}
