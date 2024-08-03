import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserRegisterRequest } from '../model/UserRegisterRequest';
import { UserLoginRequest } from '../model/UserLoginRequest';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly apiUrl = 'http://localhost:8088';

  constructor(private http: HttpClient) {}

  register(user: UserRegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(user: UserLoginRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user).pipe(
      map((response: any) => {
        localStorage.setItem('token', response.token);
        return response;
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isLogged(): boolean {
    return !!localStorage.getItem('token');
  }

  getUsernameFromToken(): string | null {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        return decoded.sub || null;
      } catch (e) {
        console.error('Failed to decode token', e);
        return null;
      }
    }
    return null;
  }
}
