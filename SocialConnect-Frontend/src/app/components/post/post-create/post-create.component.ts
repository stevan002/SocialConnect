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
  title: string = '';
  content: string = '';
  groupId: number | null = null;
  groups: any[] = [];
  selectedFile: File | null = null;

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

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  createPost(): void {
    const newPost: CreatePostRequest = {
      title: this.title,
      content: this.content,
      groupId: this.groupId,
    };

    const formData: FormData = new FormData();
    formData.append('post', new Blob([JSON.stringify(newPost)], { type: 'application/json' }));
    
    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.postService.createPost(formData).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success(response.message);
        this.title = '';
        this.content = '';
        this.groupId = null;
        this.selectedFile = null;
      },
      error: (err) => {
        console.error('Error creating post', err);
        this.toastr.error('Error creating post');
      },
    });
  }
}
