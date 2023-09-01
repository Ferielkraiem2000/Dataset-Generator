import { Component } from "@angular/core";
import { DatasetsService } from "src/app/services/dataset.service";
import { DatasetStatistics } from 'src/app/interfaces/datasetstatistics.interface';
import { NzMessageService } from "ng-zorro-antd/message";

@Component({
  selector: 'app-statistics-card',
  templateUrl: './statistic-card.component.html',
  styleUrls: ['./statistic-card.component.css'],
  providers: [ DatasetsService,NzMessageService]
})
export class StatisticCardComponent  {
  statistics: DatasetStatistics[] = [];
  constructor(private datasetService: DatasetsService,     private nzMessageService:NzMessageService
    ) {}

  ngOnInit(): void {
    this.getDatasetStatistics();
  }

  getDatasetStatistics(): void {
    this.datasetService.getStatistics().subscribe(data => {
      this.statistics = data;
    },
    error =>{this.nzMessageService.error("Error Fetching Dataset Statistics!");
  }
    );
  }
}
