import { Component, OnInit } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { HistogramData} from 'src/app/interfaces/histogramdata.interface';
import { IntervalData } from 'src/app/interfaces/interval.interface';
import { HistogramService } from 'src/app/services/histogram.service';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.css'],
  providers: [HistogramService,NzMessageService]
})
export class HistogramComponent implements OnInit {
  single: any[] = [];
  histogramData!: HistogramData;

  constructor(private histogramService: HistogramService,private nzMessageService:NzMessageService) {}

  ngOnInit(): void {
    this.fetchHistogramData();
  }

  fetchHistogramData(): void {
    this.histogramService.getHistogramData().subscribe(data => {
      this.histogramData = data;
      this.formatDataForChart();
    }, 
    error=>{
      this.nzMessageService.error("Error Fetching Histogram Data!")
    }
    
    );
 
  }

  formatDataForChart(): void {
    const labels: string[] = [];
    const intervals:IntervalData[]=this.histogramData.intervals;
    for (const intervalData of intervals) {
      labels.push(`${intervalData.start}-${intervalData.end}`);
    }
   
    const values = this.histogramData.segmentCountPerInterval;
    this.single = labels.map((label, index) => ({
      name: label,
      value: values[index]
    }));
  }
  
}
