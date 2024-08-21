import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrl = 'http://localhost:8088';

  constructor(private http: HttpClient) {}

  searchPosts(params: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/search/posts`, { params });
  }

  searchGroups(params: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/search/groups`, { params });
  }
}
