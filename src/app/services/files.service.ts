import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Statistics } from '../interfaces/statistics.interface'
@Injectable({
  providedIn: 'root'
})
export class FilesService {
  private baseUrl = 'http://localhost:8080/files/statistics';
  constructor(private http: HttpClient) {}

  getStatistics(): Observable<Statistics[]> {
    return this.http.get<any[]>(this.baseUrl).pipe(
      map(data => {
        return data.map(item => {
          const totalDurationInSeconds = parseFloat(item.totalDuration) / 1000; 
          const averageDurationInSeconds = parseFloat(item.averageDuration) / 1000; 
          return {
            fileName: item.fileName,
            totalDuration: totalDurationInSeconds.toFixed(3),
            averageDuration:averageDurationInSeconds.toFixed(3), 
            segmentCount: item.segmentCount,
            speakerCount: item.speakerCount,
            uploadTime:item.uploadTime,
            fileId:item.fileId
          } as unknown as Statistics;
        });
      })
    );
  }
}
