import { Component } from '@angular/core';
import { FileUploaderService } from 'src/app/services/file-uploader-service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzMessageService } from 'ng-zorro-antd/message';
import { FilesComponent } from '../files/files.component';
import { FileOverwriterService } from 'src/app/services/file-overwriter.service';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader-component.html',
  styleUrls: ['./file-uploader-component.css'],
  providers: [FileUploaderService,FileOverwriterService, NzModalService, NzMessageService, FilesComponent]
})
export class FileUploaderComponent {

  selectedFiles: File[] = [];
  dropAreaActive = false;
  constructor(private fileUploadService: FileUploaderService,private fileOverwriteService: FileOverwriterService, private modalService: NzModalService, private nzMessageService: NzMessageService, private filesComponent: FilesComponent) { }

  onDragOver(event: any): void {
    event.preventDefault();
    this.dropAreaActive = true;
  }

  onDragLeave(): void {
    this.dropAreaActive = false;
  }

  onFileDropped(event: any): void {
    event.preventDefault();
    this.selectedFiles = event.dataTransfer.files;
    this.dropAreaActive = true;
  }

  openFileInput(): void {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }
  onUpload(): void {
    if (this.selectedFiles.length > 0) {
      this.fileUploadService.uploadFiles(this.selectedFiles).subscribe(
        (response) => {
        },
        (error) => {
          if (error.status === 400) {
            this.nzMessageService.error('Only STM Files are allowed!');
          }
       if(error.status===500)  { this.showOverwritingAlert(this.selectedFiles);}

          }

      );

    } else {
      this.nzMessageService.info('Choose Files!');
    }
  }

  showOverwritingAlert(files: File[]){
   
      this.modalService.confirm({
        nzTitle: 'Confirmation',
        nzContent: 'File already exists, do you want to overwrite it?',
        nzOkText: 'OK',
        nzCancelText: 'Cancel',
        nzOnOk: () => {
          for(let file of files){
        this.fileOverwriteService.overwriteFiles(file).subscribe((response: any)=>{},
          error=>{})
          }
      },
        nzOnCancel: () => {
          this.nzMessageService.info('Overwriting Canceled!');  
        }
      });
  
  }
  onCancel(): void {
    this.showConfirmationAlert();
    this.selectedFiles = [];

  }
  existsFile(file: File): boolean {
    return this.selectedFiles.includes(file);
  }

  onFilesAdded(event: any): void {
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      const file: File = files[i];

      this.selectedFiles.push(file);

    }
  }


  onConfirm(): void {
    this.onUpload();
    this.selectedFiles = [];
  }
  showConfirmationAlert(): void {
    this.modalService.confirm({
      nzTitle: 'Confirmation',
      nzContent: 'Are you sure to cancel this upload task?',
      nzOkText: 'OK',
      nzCancelText: 'Cancel',
      nzOnOk: () => {
        this.nzMessageService.info('Uploading canceled');
      },
      nzOnCancel: () => {
        this.nzMessageService.info('Try again!');
      }
    });
  }

}