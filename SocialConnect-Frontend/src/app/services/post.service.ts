import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreatePostRequest } from '../model/CreatePostRequest';
import { PostResponse } from '../model/PostResponse';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = `http://localhost:8088/posts`;

  constructor(private http: HttpClient) { }

  createPost(postRequest: CreatePostRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-post`, postRequest);
  }

  getPosts(): Observable<PostResponse[]> {
    return this.http.get<PostResponse[]>(this.baseUrl);
  }

  getPost(postId: number): Observable<PostResponse> {
    return this.http.get<PostResponse>(`${this.baseUrl}/${postId}`);
  }

  deletePost(postId: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/delete-post/${postId}`);
  }
}
