import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileOverwriterService {

  private baseUrl = 'http://localhost:8080/file-overwriting';
  constructor(private http: HttpClient) {}
  overwriteFiles(file: File): Observable<any> {
    const formData: FormData = new FormData();
      formData.append('file', file, file.name);
    return this.http.put(`${this.baseUrl}`, formData);
  }
}