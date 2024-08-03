import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { GroupListComponent } from './components/group/group-list/group-list.component';
import { AuthGuard } from './auth.guard';
import { GroupCreateComponent } from './components/group/group-create/group-create.component';
import { PostListComponent } from './components/post/post-list/post-list.component';
import { PostCreateComponent } from './components/post/post-create/post-create.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent },
  { path: 'groups', component: GroupListComponent, canActivate: [AuthGuard]},
  { path: 'create-group', component: GroupCreateComponent, canActivate: [AuthGuard]},
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard]},
  { path: 'create-post', component: PostCreateComponent, canActivate: [AuthGuard]}
];;

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
