import { Injectable } from '@angular/core';
import { Statistics } from '../interfaces/statistics.interface';
@Injectable({
  providedIn: 'root'
})
export class CommunicationService {
  selectedStats: Statistics[] = [];
  showDownloadInputs = false;
  showProgress=false;
  content:any[]=[];
}
