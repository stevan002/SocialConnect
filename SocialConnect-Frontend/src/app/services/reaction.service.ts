import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreateReactionRequest } from '../model/CreateReactionRequest';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  private baseUrl = `http://localhost:8088/reactions`;

  constructor(private http: HttpClient) { }

  createReaction(reactionRequest: CreateReactionRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-reaction`, reactionRequest);
  }
}
