import { Component } from '@angular/core';
import { UserRegisterRequest } from '../../model/UserRegisterRequest';
import { UserService } from '../../services/user-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerRequest: UserRegisterRequest = { username: '', password: '', firstName: '', lastName: '', email: '', description: ''};
  responseMessage: string | null = null;

  constructor(
    private userService: UserService,
    private router: Router,
  ) {}

  register(): void {
    this.userService.register(this.registerRequest).subscribe(
      response => {
        this.responseMessage = 'Register successful';
        this.router.navigate(['/login']);
      },
      error => {
        this.responseMessage = error.error.message || 'Register failed';
      }
    );
  }
}
