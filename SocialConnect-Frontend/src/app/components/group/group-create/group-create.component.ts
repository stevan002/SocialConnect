import { Component, OnInit } from '@angular/core';
import { GroupService } from '../../../services/group.service';
import { CreateGroupRequest } from '../../../model/CreateGroupRequest';
import { ApiResponse } from '../../../model/ApiResponse';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-group-create',
  templateUrl: './group-create.component.html',
  styleUrl: './group-create.component.css'
})
export class GroupCreateComponent {

  groupName: string = '';
  description: string = '';

  constructor(private groupService: GroupService, private toastr: ToastrService){
  }

  createGroup(): void {
    const newGroup: CreateGroupRequest = {
      name: this.groupName,
      description: this.description
    };

    this.groupService.createGroup(newGroup).subscribe({
      next: (response: ApiResponse) => {
        this.toastr.success(response.message, 'Success');
        this.groupName = '';
        this.description = '';
      },
      error: (err) => {
        console.error('Error creating group', err);
        this.toastr.error('Error creating group', 'Error'); 
      }
    });
  }
}
