import { CommentResponse } from "./CommentResponse";

export interface PostResponse {
  id: number;
  content: string;
  username: string;
  groupName: string;
  createDate: string;
  likeCount: number;
  loveCount: number;
  dislikeCount: number;
  comments: CommentResponse[]; 
}
