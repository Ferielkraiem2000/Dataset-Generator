import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistogramData } from '../interfaces/histogramdata.interface';

@Injectable({
  providedIn: 'root'
})
export class HistogramService {

  private url = 'http://localhost:8080/histogram/{size}'; 
  constructor(private http: HttpClient) { }
  getHistogramData(intervalSize: number): Observable<HistogramData> {
    let params = new HttpParams();
    params = params.set('intervalSize', intervalSize.toString());
    return this.http.get<HistogramData>(`${this.url}`,{ params });
  }
}
