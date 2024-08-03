import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommentService } from '../../../services/comment.service';
import { CreateCommentRequest } from '../../../model/CreateCommentRequest';

@Component({
  selector: 'app-comment-create',
  templateUrl: './comment-create.component.html',
  styleUrl: './comment-create.component.css'
})
export class CommentCreateComponent {

  @Input() postId!: number;
  @Output() commentCreated = new EventEmitter<void>();
  commentForm: FormGroup;

  constructor(private fb: FormBuilder, private commentService: CommentService, private toastr: ToastrService){
    this.commentForm = this.fb.group({
      text: ['', Validators.required]
    });
  }

  submitComment(): void {
    if(this.commentForm.invalid) {
      return;
    }

    const commentRequest: CreateCommentRequest = {
      text: this.commentForm.get('text')?.value,
      postId: this.postId
    }

    this.commentService.createComment(commentRequest).subscribe({
      next: (response) => {
        this.toastr.success('Comment created successfully', 'Success');
        this.commentForm.reset();
        this.commentCreated.emit(); 
      },
      error: (err) => {
        console.error('Error creating comment', err);
        this.toastr.error('Error creating comment', 'Error');
      }
    });
  }
}
