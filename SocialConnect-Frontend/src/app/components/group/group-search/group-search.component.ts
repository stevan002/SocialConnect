import { Component } from '@angular/core';
import { SearchService } from '../../../services/search.service';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-group-search',
  templateUrl: './group-search.component.html',
  styleUrl: './group-search.component.css'
})
export class GroupSearchComponent {
  searchParams: any = {
    name: '',
    description: '',
    fileContent: '',
    minPosts: null,
    maxPosts: null,
    operator: 'OR'
  };

  searchResults: any[] = []; // Adjust the type if you have a specific interface

  constructor(private searchService: SearchService) {}

  onSearch() {
    let params = new HttpParams();
    
    if (this.searchParams.name) {
      params = params.append('name', this.searchParams.name);
    }
    if (this.searchParams.description) {
      params = params.append('description', this.searchParams.description);
    }
    if (this.searchParams.fileContent) {
      params = params.append('fileContent', this.searchParams.fileContent);
    }
    if (this.searchParams.minPosts !== null && this.searchParams.minPosts !== undefined) {
      params = params.append('minNumberOfPosts', this.searchParams.minPosts.toString());
    }
    if (this.searchParams.maxPosts !== null && this.searchParams.maxPosts !== undefined) {
      params = params.append('maxNumberOfPosts', this.searchParams.maxPosts.toString());
    }
    if (this.searchParams.operator) {
      params = params.append('operator', this.searchParams.operator);
    }

    this.searchService.searchGroups(params).subscribe(
      results => this.searchResults = results,
      error => console.error('Greška prilikom pretrage:', error)
    );
  }

  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }
}
