import { Component, OnInit } from '@angular/core';
import { PostService } from '../../../services/post.service';
import { GroupService } from '../../../services/group.service';
import { CreatePostRequest } from '../../../model/CreatePostRequest';
import { ApiResponse } from '../../../model/ApiResponse';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.css'],
})
export class PostCreateComponent implements OnInit {
  content: string = '';
  groupId: number | null = null;
  groups: any[] = [];

  constructor(
    private postService: PostService,
    private groupService: GroupService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadGroups();
  }

  loadGroups(): void {
    this.groupService.getAllGroups().subscribe((groups) => {
      this.groups = groups;
    });
  }

  createPost(): void {
    const newPost: CreatePostRequest = {
      content: this.content,
      groupId: this.groupId,
    };

    this.postService.createPost(newPost).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success(response.message);
        this.content = '';
        this.groupId = null;
      },
      error: (err) => {
        console.error('Error creating post', err);
        this.toastr.error('Error creating post');
      },
    });
  }
}
