import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UpdateFileService {

  private baseUrl = 'http://localhost:8080/file-parsing';

  constructor(private http: HttpClient) { }

  updateFileName( fileId: number, fileName: string): Observable<string> {
    const url = `${this.baseUrl}/{id}/{name}?fileId=${fileId}&fileName=${fileName}`;
    return this.http.put<string>(url, {});
  }
}
