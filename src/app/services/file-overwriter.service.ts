import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileOverwriterService {
 private baseUrloverwrite='http://localhost:8080/file-overwriting';
 constructor(private http: HttpClient) {}
 overwriteFile(files: File[]): Observable<any> {
  const formData: FormData = new FormData();
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i], files[i].name);
  }
   return this.http.put(`${this.baseUrloverwrite}`, formData);
 }
}
