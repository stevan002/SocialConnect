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
import { GroupSearchComponent } from './components/group/group-search/group-search.component';
import { PostSearchComponent } from './components/post/post-search/post-search.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'groups', component: GroupListComponent, canActivate: [AuthGuard]},
  { path: 'create-group', component: GroupCreateComponent, canActivate: [AuthGuard]},
  { path: 'posts', component: PostListComponent, canActivate: [AuthGuard]},
  { path: 'create-post', component: PostCreateComponent, canActivate: [AuthGuard]},
  { path: 'search-groups', component: GroupSearchComponent, canActivate: [AuthGuard]},
  { path: 'search-posts', component: PostSearchComponent, canActivate: [AuthGuard]},
  { path: '**', redirectTo: 'posts' } 
];;

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
