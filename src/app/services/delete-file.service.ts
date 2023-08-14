import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeleteFileService {
  private baseUrl: string = 'http://localhost:8080'; 
  constructor(private http: HttpClient) { }
  deleteFile(fileIds: number[]):Observable<any>{
    const url = `${this.baseUrl}/file-parsing/{ids}`;
    let params = new HttpParams();
    for (const fileId of fileIds) {
      params = params.append('fileIds', fileId.toString());
    }
    
    return this.http.delete<any>(url, { params });
}
}
