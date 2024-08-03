export interface CreateReactionRequest {
  reactionType: string;
  postId?: number | null;
  commentId?: number | null;
}
