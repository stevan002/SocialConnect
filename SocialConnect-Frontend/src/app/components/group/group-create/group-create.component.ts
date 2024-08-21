import { Component } from '@angular/core';
import { GroupService } from '../../../services/group.service';
import { CreateGroupRequest } from '../../../model/CreateGroupRequest';
import { ApiResponse } from '../../../model/ApiResponse';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-group-create',
  templateUrl: './group-create.component.html',
  styleUrls: ['./group-create.component.css']
})
export class GroupCreateComponent {

  groupName: string = '';
  description: string = '';
  selectedFile: File | null = null;

  constructor(private groupService: GroupService, private toastr: ToastrService) {}

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  createGroup(): void {
    const newGroup: CreateGroupRequest = {
      name: this.groupName,
      description: this.description
    };

    const formData: FormData = new FormData();
    formData.append('group', new Blob([JSON.stringify(newGroup)], { type: 'application/json' }));

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.groupService.createGroup(formData).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success(response.message, 'Success');
        this.groupName = '';
        this.description = '';
        this.selectedFile = null;
      },
      error: (err) => {
        console.error('Error creating group', err);
        this.toastr.error('Error creating group', 'Error');
      }
    });
  }
}
