import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistogramData } from '../interfaces/histogramdata.interface';

@Injectable({
  providedIn: 'root'
})

export class HistogramCombinedService {

  private baseUrl = 'http://localhost:8080/histogram';

  constructor(private http: HttpClient) { }

  getCombinedHistogramData(fileIds: number[], intervalSize: number): Observable<HistogramData> {
    let params = new HttpParams();
    for (const fileId of fileIds) {
      params = params.append('fileIds', fileId.toString());
    }
    params = params.set('intervalSize', intervalSize.toString());

    const url = `${this.baseUrl}/{ids}/{size}`;
    
    return this.http.get<HistogramData>(url, { params });
  }
}
