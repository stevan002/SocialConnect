import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UpdateGroupRequest } from '../../../model/UpdateGroupRequest';
import { GroupService } from '../../../services/group.service';

@Component({
  selector: 'app-group-update',
  templateUrl: './group-update.component.html',
  styleUrl: './group-update.component.css'
})
export class GroupUpdateComponent {
  groupId: number | null = null;
  updateRequest: UpdateGroupRequest = {
    name: '',
    description: ''
  };

  constructor(
    private route: ActivatedRoute,
    private groupService: GroupService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.groupId = id ? +id : null;
    this.loadGroup();
  }

  loadGroup(): void {
    if (this.groupId !== null) {
      this.groupService.getGroup(this.groupId).subscribe({
        next: (group) => {
          this.updateRequest.name = group.name;
          this.updateRequest.description = group.description;
        },
        error: (err) => {
          console.error('Failed to load group', err);
          this.toastr.error('Failed to load group', 'Error');
        }
      });
    } else {
      this.toastr.error('Group ID is invalid', 'Error');
      this.router.navigate(['/groups']);
    }
  }

  updateGroup(): void {
    if (this.updateRequest.name.trim() && this.updateRequest.description.trim() && this.groupId !== null) {
      this.groupService.updateGroup(this.groupId, this.updateRequest).subscribe({
        next: () => {
          this.toastr.success('Group updated successfully', 'Success');
          this.router.navigate(['/groups']);
        },
        error: (err) => {
          console.error('Failed to update group', err);
          this.toastr.error('Failed to update group', 'Error');
        }
      });
    } else {
      this.toastr.error('Name and description cannot be empty', 'Validation Error');
    }
  }
}
