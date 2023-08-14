import { Component } from "@angular/core";
import { DatasetsService } from "src/app/services/dataset.service";
import { DatasetStatistics } from 'src/app/interfaces/datasetstatistics.interface';

@Component({
  selector: 'app-statistics-card',
  templateUrl: './statistic-card.component.html',
  styleUrls: ['./statistic-card.component.css'],
  providers: [ DatasetsService]
})
export class StatisticCardComponent  {
  statistics: DatasetStatistics[] = [];
  constructor(private datasetService: DatasetsService) {}

  ngOnInit(): void {
    this.getStatistics();
  }

  getStatistics(): void {
    this.datasetService.getStatistics().subscribe(data => {
      this.statistics = data;
    },
    error =>{console.log(error);}
    );
  }
}
