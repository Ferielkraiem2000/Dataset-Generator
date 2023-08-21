import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Statistics } from 'src/app/interfaces/statistics.interface';
import { DeleteFileService } from 'src/app/services/delete-file.service';
import { FilesService } from 'src/app/services/files.service';
import { UpdateFileService } from 'src/app/services/update-file.service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { DownloadFormatDialogComponent } from '../download-format-dialog/download-format-dialog.component';
import { SelectedFilesStatisticService } from 'src/app/services/selected-files-statistic.service';
import { CommunicationService } from 'src/app/services/communication.service';
import { FilecontentService } from 'src/app/services/filecontent.service';
import { FileDownloaderService } from 'src/app/services/file-downloader.service';
import { HttpEventType } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NzMessageService } from 'ng-zorro-antd/message';
@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.css'],
  providers: [FilesService, UpdateFileService, DeleteFileService, NzMessageService]
})
export class FilesComponent implements OnInit {
  statistics: Statistics[] = [];
  currentPage: number = 1;
  startIndex: number = 0;
  pageSize: number = 5;
  statisticsPerPage: Statistics[] = [];
  totalPages: number = 0;
  totalItemsCount: number = 0;
  selectedStatistics: Statistics[] = [];
  selectedStat: Statistics | null = null;
  newFileName: string = '';
  editMode = false;
  showDownloadCombined = false;
  path?: string;
  selectedFormat!: string;
  downloadedFileData: any;
  downloadInterval: any;
  isCanceled: boolean = false;
  isDownloadInProgress: boolean = false;
  downloadSubscription: Subscription | undefined;
  content: any[] = [];
  percent = 0;
  interval: any;
  searchFileName: string = '';
  constructor(
    private filesService: FilesService,
    private updateFileService: UpdateFileService,
    private deleteFileService: DeleteFileService,
    public modalService: NzModalService,
    private selectedFilesStatisticsService: SelectedFilesStatisticService,
    public communicationService: CommunicationService,
    private fileContentService: FilecontentService,
    private fileDownloaderService: FileDownloaderService,
    private nzMessageService: NzMessageService
  ) { }


  ngOnInit(): void {
this.getStatistics();

}
  isDownloadView(): boolean {
    return this.communicationService.showDownloadInputs;
  }
  isSelectedStats(): Statistics[] {
    return this.communicationService.selectedStats;
  }
  openView(): void {
    if (this.isSelectedStats().length === 0) {
      this.showSelectMessage();
    }
    else { this.communicationService.showDownloadInputs = true; }
  }
  isProgressView(): boolean {
    return this.communicationService.showProgress;
  }

  openProgressView(): void {
    this.communicationService.showDownloadInputs = false;
    this.communicationService.showProgress = true;
    this.isCanceled = false;
    this.updatePercentAutomatically();
  }



  onDownloadClick(): void {
    const fileIds: number[] = this.communicationService.selectedStats.map(stat => stat.fileId);
    this.downloadSubscription = this.fileDownloaderService.downloadManifestFile(this.selectedFormat, fileIds, this.path)
      .subscribe(
        response => {
          if (response.type === HttpEventType.Response) {
            this.downloadedFileData = response.body;
            if (this.percent === 100) {
              this.downloadFile();
            }

          }
        },
        error => {

        }
      );
  }
  cancelDownload(): void {
    this.isCanceled = true;
    clearInterval(this.interval);
  }

  updatePercentAutomatically(): void {
    this.percent = 0;
    this.interval = setInterval(() => {
      if (!this.isCanceled && this.percent < 100) {
        this.percent += 10;
      } else {
        clearInterval(this.interval);
        if (!this.isCanceled) {
          this.onDownloadClick();
        }
      }
    }, 1000);
  }


  downloadFile(): void {
    if (!this.downloadedFileData) {
      return;
    }

    const blob = new Blob([this.downloadedFileData], { type: 'application/octet-stream' });
    const downloadUrl = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = downloadUrl;

    if (this.selectedFormat.toLowerCase() === 'json') {
      a.download = 'combined_manifest.json';
    } else if (this.selectedFormat.toLowerCase() === 'csv') {
      a.download = 'combined_manifest.csv';
    } else if (this.selectedFormat.toLowerCase() === 'espnet') {
      a.download = 'fileForESPnet.zip';
    }

    document.body.appendChild(a);
    a.click();

    window.URL.revokeObjectURL(downloadUrl);
    a.remove();
  }
  changePage(offset: number): void {
  
      this.currentPage += offset;
      this.getStatisticsPerPage(this.currentPage);
    
  }
  getPages(): number {
    if (this.totalItemsCount <= this.pageSize) {
      this.totalPages += 1;
    }
    else {
      if (this.totalItemsCount % 5 == 0) {
        this.totalPages += Math.floor(this.totalItemsCount / 5);

      } else {
        this.totalPages += Math.floor(this.totalItemsCount / 5) + 1;
      }
    }
    return this.totalPages;
  }
  getStatisticsPerPage(pageNumber:number) {
  
    let startIndex = (pageNumber - 1) * 5;
    let endIndex = Math.min(startIndex + 5, this.totalItemsCount);
  for(let i=startIndex ;i<endIndex ;i++){
  this.statisticsPerPage[i]=this.statistics[i];
 
  }

  }
  getStatistics(): void {
    this.filesService.getStatistics().subscribe(
      result => {
        this.statistics = result.data;
        this.totalItemsCount = result.totalCount;
        this.totalPages = this.getPages();

   this.getStatisticsPerPage(this.currentPage);
      },
      error => {
        this.nzMessageService.error("Error Fetching Files Statistics!");
      }
    );
  }
  
  searchByFileName() {
    if (this.searchFileName.trim() === '') {
      this.getStatistics();
    } else {
      this.filesService.getStatisticsByFileName(this.searchFileName).subscribe(
        data => {
          this.statistics = data;
        },
        error => {
          this.nzMessageService.error("Error Fetching File Statistics with this name!");
        },

      );
    }
  }


  selectRow(stat: Statistics): void {
    this.selectedStat = stat;
  }

  isSelected(stat: Statistics): boolean {
    return this.communicationService.selectedStats.includes(stat);
  }

  toggleSelection(stat: Statistics): void {
    if (this.isSelected(stat)) {
      this.communicationService.selectedStats = this.communicationService.selectedStats.filter(item => item !== stat);
    } else {
      this.communicationService.selectedStats.push(stat);
    }
    this.fetchStatistics();
  }


  onIconClick(stat: Statistics) {
    this.editMode = true;
    this.newFileName = stat.fileName;
  }

  updateFileName(stat: Statistics): void {
    this.updateFileService.updateFileName(stat.fileId, this.newFileName).subscribe(response => {
    },
      error => {
        if (error.status != 200) {
          this.nzMessageService.error('File Name should not be null!');
        }
      });
  }

  onCancel() {
    this.editMode = false;
  }
  onConfirm(stat: Statistics) {
    this.updateFileName(stat);
    if (this.newFileName != "") {
      stat.fileName = this.newFileName;
      this.editMode = false;
    }
    this.editMode = false;
  }
  deleteFile(stat: Statistics) {
    const fileIds: number[] = [];
    for (const stat of this.communicationService.selectedStats) {
      fileIds.push(stat.fileId);
    }
    this.deleteFileService.deleteFile(fileIds).subscribe(response => {
    },
      error => {
        if (error.status != 200) {
          this.nzMessageService.error('File Name should not be null!');
           this.nzMessageService.error('Error Deleting File!');} 
      }
    );

  }

  openFormatSelectionDialog(): void {
    this.modalService.create({
      nzTitle: 'Download Manifest File',
      nzContent: DownloadFormatDialogComponent,
      nzFooter: null,
      nzStyle: { width: '800px', 'align-items': 'center', 'justify-content': 'center' },
    });
  }

  fetchStatistics() {
    const fileIds: number[] = [];
    for (const stat of this.communicationService.selectedStats) {
      fileIds.push(stat.fileId);
    }
    this.selectedFilesStatisticsService.getStatistics(fileIds).subscribe(
      data => {
        this.selectedStatistics = data;
      },
      error => {
   this.nzMessageService.error("Error Fetching Combined Statistics!");
      }
    );
  }
  showContent(stat: Statistics) {
    this.fileContentService.showContent(stat.fileId).subscribe(
      (response) => {
        this.content = response;
        this.openContentDialog();
      },
      error => {
        this.nzMessageService.error('Error Fetching File Content!');

      }
    );
  }


  openContentDialog(): void {
    const tableRows = this.content.map(item => `
        <tr>
          <td>${item.file_name}</td>
          <td></td>
          <td></td>
          <td></td>
          <td>${item.speaker}</td>
          <td></td>
          <td></td>
          <td></td>
          <td>${item.segment_start}</td>
          <td></td>
          <td></td>
          <td></td>
          <td>${item.segment_end}</td>
          <td></td>
          <td></td>
          <td></td>

          <td>${item.transcription}</td>
        </tr>
      `).join('');

    const tableHtml = `
        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>File Name</th>
                <th></th>
                <th></th>
                <th></th>
                <th>Speaker</th>
                <th></th>
                <th></th>
                <th></th>
                <th>Segment Start</th>
                <th></th>
                <th></th>
                <th></th>
                <th>Segment End</th>
                <th></th>
                <th></th><th></th>
                <th>Transcription</th>
              </tr>
            </thead>
            <tbody>
              ${tableRows}
            </tbody>
          </table>
        </div>
      `;

    this.modalService.create({
      nzTitle: 'Audio File',
      nzContent: tableHtml,
      nzFooter: null,
      nzStyle: { width: '1400px', height: '800px' },
    });
  }



  showDeletionAlert(stat: Statistics): void {
    this.modalService.confirm({
      nzTitle: 'Delete File',
      nzContent: 'Are you sure to delete this file?',
      nzOkText: 'OK',
      nzCancelText: 'Cancel',
      nzOnOk: () => {
        this.deleteFile(stat);
        this.statisticsPerPage = this.statisticsPerPage.filter(s => s !== stat);
      },
      nzOnCancel: () => {
      }
    });
  }
  showTapMessage() {
    if (!this.selectedFormat) {
      this.nzMessageService.info('Format is required!')
    }
  }
  showSelectMessage() {
    this.nzMessageService.info('Select desired Files!')
  }

}
