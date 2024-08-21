import { HttpClient, HttpParams } from '@angular/common/http';
import { Component } from '@angular/core';
import { SearchService } from '../../../services/search.service';
import { SearchResponse } from '../../../model/PostSearchResponse';

@Component({
  selector: 'app-post-search',
  templateUrl: './post-search.component.html',
  styleUrls: ['./post-search.component.css']
})
export class PostSearchComponent {

  searchParams: any = {
    title: '',
    content: '',
    fileContent: '',
    minLikes: null,
    maxLikes: null,
    commentContent: '',
    minComments: null,
    maxComments: null,
    operator: 'OR'
  };

  searchResults: SearchResponse[] = [];

  constructor(private searchService: SearchService) {}

  onSearch() {
    let params = new HttpParams();
    
    if (this.searchParams.title) {
      params = params.append('title', this.searchParams.title);
    }
    if (this.searchParams.content) {
      params = params.append('content', this.searchParams.content);
    }
    if (this.searchParams.fileContent) {
      params = params.append('fileContent', this.searchParams.fileContent);
    }
    if (this.searchParams.minLikes !== null && this.searchParams.minLikes !== undefined) {
      params = params.append('minLikes', this.searchParams.minLikes.toString());
    }
    if (this.searchParams.maxLikes !== null && this.searchParams.maxLikes !== undefined) {
      params = params.append('maxLikes', this.searchParams.maxLikes.toString());
    }
    if (this.searchParams.commentContent) {
      params = params.append('commentContent', this.searchParams.commentContent);
    }
    if (this.searchParams.minComments !== null && this.searchParams.minComments !== undefined) {
      params = params.append('minComments', this.searchParams.minComments.toString());
    }
    if (this.searchParams.maxComments !== null && this.searchParams.maxComments !== undefined) {
      params = params.append('maxComments', this.searchParams.maxComments.toString());
    }
    if (this.searchParams.operator) {
      params = params.append('operator', this.searchParams.operator);
    }

    this.searchService.searchPosts(params).subscribe(
      results => this.searchResults = results,
      error => console.error('Greška prilikom pretrage:', error)
    );
  }

  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }
}
