import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreatePostRequest } from '../model/CreatePostRequest';
import { PostResponse } from '../model/PostResponse';
import { UpdatePostRequest } from '../model/UpdatePostRequest';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = `http://localhost:8088/posts`;

  constructor(private http: HttpClient) { }

  createPost(formData: FormData): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-post`, formData);
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

  updatePost(postId: number, updateRequest: UpdatePostRequest): Observable<ApiResponse> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });

    return this.http.put<ApiResponse>(`${this.baseUrl}/update/${postId}`, updateRequest, { headers });
  }
}
