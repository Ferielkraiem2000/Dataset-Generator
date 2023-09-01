import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistogramData } from '../interfaces/histogramdata.interface';

@Injectable({
  providedIn: 'root'
})
export class HistogramService {

  private baseUrl = 'http://localhost:8080/histogram/{size}'; 
  constructor(private http: HttpClient) { }
  getHistogramData(intervalSize: number): Observable<HistogramData> {
    let url = `${this.baseUrl}?size=${intervalSize}`;
    return this.http.get<HistogramData>(url);
  }
}
