import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Statistics } from '../interfaces/statistics.interface';

@Injectable({
  providedIn: 'root'
})
export class SelectedFilesStatisticService {

  private baseUrl: string = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  getStatistics(fileIds: number[]): Observable<Statistics[]> {
    const url = `${this.baseUrl}/files/statistics/{ids}`;
    
    let params = new HttpParams();
    for (const fileId of fileIds) {
      params = params.append('fileIds', fileId.toString());
    }
    
    return this.http.get<any[]>(url, { params }).pipe(
      map(data => {
        return data.map(item => {
          const totalDurationInSeconds = parseFloat(item.totalDuration) / 1000; 
          const averageDurationInSeconds = parseFloat(item.averageDuration) / 1000; 
          return {
            fileName: null,
            totalDuration: totalDurationInSeconds.toFixed(3),
            averageDuration: averageDurationInSeconds.toFixed(3), 
            segmentCount: item.segmentCount,
            speakerCount: item.speakerCount,
            uploadTime: null,
            fileId: item.fileId
          } as unknown as Statistics;
        });
      })
    );
  }
}
