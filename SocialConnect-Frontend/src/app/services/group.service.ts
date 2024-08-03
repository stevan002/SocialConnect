import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreateGroupRequest } from '../model/CreateGroupRequest';
import { GroupResponse } from '../model/GroupResponse';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private baseUrl = `http://localhost:8088/groups`;

  constructor(private http: HttpClient) { }

  createGroup(groupRequest: CreateGroupRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-group`, groupRequest);
  }

  getAllGroups(): Observable<GroupResponse[]> {
    return this.http.get<GroupResponse[]>(this.baseUrl);
  }

  getGroup(groupId: number): Observable<GroupResponse> {
    return this.http.get<GroupResponse>(`${this.baseUrl}/${groupId}`);
  }

  deleteGroup(groupId: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.baseUrl}/delete-group/${groupId}`);
  }
}
