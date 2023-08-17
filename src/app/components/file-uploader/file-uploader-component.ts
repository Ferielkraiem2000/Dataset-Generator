import { Component } from '@angular/core';
import { FileUploaderService } from 'src/app/services/file-uploader-service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzMessageService } from 'ng-zorro-antd/message';
import { FilesComponent } from '../files/files.component';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader-component.html',
  styleUrls: ['./file-uploader-component.css'],
  providers: [FileUploaderService , NzModalService, NzMessageService ,FilesComponent]
})
export class FileUploaderComponent {

  selectedFiles: File[] = [];
  dropAreaActive = false;
  constructor(private fileUploadService: FileUploaderService, private modalService: NzModalService , private nzMessageService: NzMessageService , private filesComponent: FilesComponent) { }

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
          } else {
            console.error('Error uploading files', error);
          }
        }
      );
    } else {
      this.nzMessageService.info('Choose Files!');
    }
  }

  onCancel(): void {
    this.showConfirmationAlert();
    this.selectedFiles = [];

  }


  onFilesAdded(event: any): void {
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      const file: File = files[i];
      this.selectedFiles.push(file);
    
    }
  }

  overwriteFile(newFile: File): void {
   
    this.selectedFiles = this.selectedFiles.filter(existingFile => existingFile.name !== newFile.name);
    this.selectedFiles.push(newFile);
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
        this.nzMessageService.info('Uploading canceled'); },
      nzOnCancel: () => {
        this.nzMessageService.info('Try again!');  
      }
    });
  }

}