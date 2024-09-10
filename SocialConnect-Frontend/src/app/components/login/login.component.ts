import { Component, OnInit } from '@angular/core';
import { UserLoginRequest } from '../../model/UserLoginRequest';
import { UserService } from '../../services/user-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  loginRequest: UserLoginRequest = { username: '', password: '' };
  responseMessage: string | null = null;

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.router.navigate(['/posts']);
    }
  }

  login(): void {
    this.userService.login(this.loginRequest).subscribe(
      response => {
        this.responseMessage = 'Login successful';
        this.router.navigate(['/groups']);
      },
      error => {
        this.responseMessage = error.error.message || 'Login failed';
      }
    );
  }
}
