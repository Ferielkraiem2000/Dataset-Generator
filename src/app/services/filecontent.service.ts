import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FilecontentService {
  private baseUrl = 'http://localhost:8080/file-parsing';

  constructor(private http: HttpClient) {}

  showContent(fileId: number): Observable<any[]> {
    let url = `${this.baseUrl}/{id}?&fileId=${fileId}`;
    return this.http.get<any[]>(url, );
  }
}
