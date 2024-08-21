interface Highlight {
    title?: string[];
    full_content?: string[];
    file_content?: string[];
    comment_content?: string[];
    [key: string]: string[] | undefined;
}

  
interface Source {
    id: string;
    title: string;
    fullContent: string;
    fileContent: string;
    numberOfLikes: number;
    numberOfComments: number;
    commentContent: string[];
    databaseId: number;
}

export interface SearchResponse {
    highlights: Highlight;
    source: Source;
}