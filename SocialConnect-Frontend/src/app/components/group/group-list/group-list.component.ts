import { Component, OnInit } from '@angular/core';
import { GroupResponse } from '../../../model/GroupResponse';
import { GroupService } from '../../../services/group.service';
import { UserService } from '../../../services/user-service.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrl: './group-list.component.css'
})
export class GroupListComponent implements OnInit {
  groups: GroupResponse[] = [];
  currentUsername: string | null = null;
  
  constructor(private groupService: GroupService, private userService: UserService, private router: Router, private toastr: ToastrService ){
    if (!this.userService.isLogged()) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.loadGroups();
    this.currentUsername = this.userService.getUsernameFromToken();
  }

  loadGroups(): void {
    this.groupService.getAllGroups().subscribe({
      next: (data) => {
        this.groups = data;
      },
      error: (err) => {
        console.log('Error fetching groups', err);
      }
    })
  }

  deleteGroup(groupId: number): void {
    if (confirm('Are you sure you want to delete this group?')) {
      this.groupService.deleteGroup(groupId).subscribe({
        next: () => {
          this.groups = this.groups.filter(group => group.id !== groupId);
          this.toastr.success('Group successfully deleted.');
        },
        error: (error) => {
          console.log('Error deleting group', error);
          this.toastr.error('Failed to delete group: ' + (error.error?.message || 'Unknown error'));
        }
      });
    }
  }

  canDeleteGroup(group: GroupResponse): boolean {
    return this.currentUsername === group.username;
  }
}
