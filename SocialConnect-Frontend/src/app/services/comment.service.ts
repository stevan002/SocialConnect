import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreateCommentRequest } from '../model/CreateCommentRequest';
import { CommentResponse } from '../model/CommentResponse';
import { UdpateCommentRequest } from '../model/UpdateCommentRequest';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private baseUrl = `http://localhost:8088/comments`;

  constructor(private http: HttpClient) { }

  createComment(commentRequest: CreateCommentRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-comment`, commentRequest);
  }

  getCommentsForPost(postId: number): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`${this.baseUrl}/post/${postId}`);
  }

  getComment(commentId: number): Observable<CommentResponse> {
    return this.http.get<CommentResponse>(`${this.baseUrl}/${commentId}`);
  }

  deleteComment(commentId: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/delete-comment/${commentId}`);
  }

  updateComment(commentId: number, updateRequest: UdpateCommentRequest): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.baseUrl}/update-comment/${commentId}`, updateRequest);
  }
}
