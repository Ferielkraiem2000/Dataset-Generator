import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { HistogramData } from 'src/app/interfaces/histogramdata.interface';
import { IntervalData } from 'src/app/interfaces/interval.interface';
import { Statistics } from 'src/app/interfaces/statistics.interface';
import { CommunicationService } from 'src/app/services/communication.service';
import { FilesService } from 'src/app/services/files.service';
import { HistogramService } from 'src/app/services/histogram.service';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.css'],
  providers: [HistogramService, NzMessageService]
})
export class HistogramComponent implements OnInit {
  single: any[] = [];
  histogramData!: HistogramData;
  noDataResult = [
    {
      name: '0.0',
      value: 0,
    }
  ];
  colorScheme = 'black';
  sliderValue: number = 1;
  sliderClicked: boolean = false;
  constructor(private filesService: FilesService,
    private histogramService: HistogramService, private nzMessageService: NzMessageService, private cdr: ChangeDetectorRef   , public communicationService: CommunicationService) { }

  ngOnInit(): void {
    this.getStatistics();
    this.fetchHistogramData();
  
  }

  fetchHistogramData(): void {
    this.histogramService.getHistogramData(this.sliderValue).subscribe(data => {
      this.histogramData = data;
      this.formatDataForChart();
    },
      error => {
        if(this.histogramData!=null){this.nzMessageService.error("Error Fetching Histogram Data!",error);}
      }

    );

  }

  formatDataForChart(): void {
    const labels: string[] = [];
    const intervals: IntervalData[] = this.histogramData.intervals;
    for (const intervalData of intervals) {
      labels.push(`${intervalData.start}-${intervalData.end}`);
    }

    const values = this.histogramData.segmentCountPerInterval;
    this.single = labels.map((label, index) => ({
      name: label,
      value: parseInt(values[index].toString(), 10)
    }));
  }
  updateSliderValue() {
    this.cdr.detectChanges();
    this.sliderClicked = true;
    this.fetchHistogramData();   
}
getStatistics(): void {
  this.filesService.getStatistics().subscribe(
    result => {
      this.communicationService.statistics = result.data;     
    },
    error => {
    }
  );
}

isStats(): Statistics[] {
  return this.communicationService.statistics;
}
}