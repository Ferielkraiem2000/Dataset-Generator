import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileDownloaderService {
  private baseUrl = 'http://localhost:8080/file-parsing';

  constructor(private http: HttpClient) {}

  downloadManifestFile(format: string, fileIds: number[], path?: string): Observable<HttpResponse<Blob>> {
    let url = `${this.baseUrl}/{format}/{ids}/{path}?format=${format}&file_id=${fileIds}`;
    if(!path){
      return this.http.get(url, {
        responseType: 'blob',
        observe: 'response',
      });
    }
    else{
      url+=`&path=${path}`;
      return this.http.get(url, {
        responseType: 'blob',
        observe: 'response',
   
      });
    }
   
  }
}
