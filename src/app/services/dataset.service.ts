import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DatasetStatistics } from '../interfaces/datasetstatistics.interface';


@Injectable({
  providedIn: 'root'
})
export class DatasetsService {
  private baseUrl = 'http://localhost:8080/dataset/statistics';

  constructor(private http: HttpClient) {}

  getStatistics(): Observable<DatasetStatistics[]> {
    return this.http.get<any[]>(this.baseUrl).pipe(
      map(data => {
        return data.map(item => {
          const totalDurationInSeconds = parseFloat(item.totalDuration) / 1000; 
          const averageDurationInSeconds = parseFloat(item.averageDuration) / 1000; 
          return {
            speakerCount: item.speakerCount,
            totalDuration:  totalDurationInSeconds.toFixed(3),
            averageDuration: averageDurationInSeconds.toFixed(3),
            segmentCount: item.segmentCount,
          } as unknown as DatasetStatistics;
        });
      })
    );
  }
}
