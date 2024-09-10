import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastNoAnimation, ToastrModule } from 'ngx-toastr';
import { GroupListComponent } from './components/group/group-list/group-list.component';
import { GroupCreateComponent } from './components/group/group-create/group-create.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { PostListComponent } from './components/post/post-list/post-list.component';
import { PostCreateComponent } from './components/post/post-create/post-create.component';
import { CommentCreateComponent } from './components/comment/comment-create/comment-create.component';
import { CommentListComponent } from './components/comment/comment-list/comment-list.component';
import { ReactionCreateComponent } from './components/reaction/reaction-create/reaction-create.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PostSearchComponent } from './components/post/post-search/post-search.component';
import { GroupSearchComponent } from './components/group/group-search/group-search.component';
import { PostUpdateComponent } from './components/post/post-update/post-update.component';
import { GroupUpdateComponent } from './components/group/group-update/group-update.component';
import { CommentUpdateComponent } from './components/comment/comment-update/comment-update.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    GroupListComponent,
    GroupCreateComponent,
    PostListComponent,
    PostCreateComponent,
    CommentCreateComponent,
    CommentListComponent,
    ReactionCreateComponent,
    NavbarComponent,
    PostSearchComponent,
    GroupSearchComponent,
    PostUpdateComponent,
    GroupUpdateComponent,
    CommentUpdateComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
