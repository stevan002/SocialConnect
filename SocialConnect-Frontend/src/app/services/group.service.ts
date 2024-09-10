import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../model/ApiResponse';
import { CreateGroupRequest } from '../model/CreateGroupRequest';
import { GroupResponse } from '../model/GroupResponse';
import { UpdateGroupRequest } from '../model/UpdateGroupRequest';

@Injectable({
  providedIn: 'root'
})
export class GroupService {

  private baseUrl = `http://localhost:8088/groups`;

  constructor(private http: HttpClient) { }

  createGroup(formData: FormData): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.baseUrl}/create-group`, formData);
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

  updateGroup(groupId: number, updateRequest: UpdateGroupRequest): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.baseUrl}/update-group/${groupId}`, updateRequest);
  }
}
