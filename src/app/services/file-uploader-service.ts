import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploaderService {

  private baseUrl = 'http://localhost:8080/file-parsing';
  constructor(private http: HttpClient) {}
  uploadFiles(files: File[]): Observable<any> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i], files[i].name);
    }

    return this.http.post(`${this.baseUrl}`, formData);
  }
}
