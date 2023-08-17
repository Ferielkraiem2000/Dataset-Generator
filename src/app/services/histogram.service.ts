import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistogramData } from '../interfaces/histogramdata.interface';

@Injectable({
  providedIn: 'root'
})
export class HistogramService {

  private url = 'http://localhost:8080/histogram'; 
  constructor(private http: HttpClient) { }
  getHistogramData(): Observable<HistogramData> {
    return this.http.get<HistogramData>(`${this.url}`);
  }
}
