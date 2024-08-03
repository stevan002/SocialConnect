import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ReactionService } from '../../../services/reaction.service';
import { ToastrService } from 'ngx-toastr';
import { CreateReactionRequest } from '../../../model/CreateReactionRequest';

@Component({
  selector: 'app-reaction-create',
  templateUrl: './reaction-create.component.html',
  styleUrl: './reaction-create.component.css'
})
export class ReactionCreateComponent {

  @Input() postId?: number;
  @Input() commentId? : number;
  @Output() reactionCreated = new EventEmitter<void>();

  constructor(
    private reactionService: ReactionService,
    private toastr: ToastrService
  ){}

  createReaction(reactionType: string): void {
    if(!this.postId && !this.commentId){
      this.toastr.error('Post ID or Comment ID must be provided', 'Error');
      return;
    }

    const reactionRequest: CreateReactionRequest = {
      reactionType,
      postId: this.postId || null,
      commentId: this.commentId || null
    };

    this.reactionService.createReaction(reactionRequest).subscribe({
      next: (response) => {
        this.toastr.success(response.message, 'Success');
        this.reactionCreated.emit();
      },
      error: (err) => {
        console.error('Error creating reaction', err);
        this.toastr.error('Error creating reaction', 'Error');
      }
    });

  }

}
