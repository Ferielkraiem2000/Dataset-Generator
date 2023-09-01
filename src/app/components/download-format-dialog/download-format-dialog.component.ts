import { Component } from '@angular/core';
import { NzModalRef } from 'ng-zorro-antd/modal';
import { FileDownloaderService } from 'src/app/services/file-downloader.service';
import { CommunicationService } from 'src/app/services/communication.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-download-format-dialog',
  templateUrl: './download-format-dialog.component.html',
  providers: [FileDownloaderService,NzMessageService]
})
export class DownloadFormatDialogComponent {
  path?: string;
  selectedFormat!: string;

  constructor(
    private modalRef: NzModalRef,
    private fileDownloaderService: FileDownloaderService,
    private communicationService: CommunicationService,
    private nzMessageService:NzMessageService
  ) {}

  onDownloadClick(): void {
    const fileIds: number[] = [];
    for (const stat of this.communicationService.selectedStats) {
      fileIds.push(stat.fileId);
    }
    
    this.fileDownloaderService.downloadManifestFile(this.selectedFormat, fileIds, this.path).subscribe(
      response => {
      const blob = response.body;
        const downloadUrl = URL.createObjectURL(blob!);
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
      },
      error => {
         this.nzMessageService.error("Error downloading Manifest File!")
      }
    );
  }

  onNoClick(): void {
    this.modalRef.destroy();
  }
}
