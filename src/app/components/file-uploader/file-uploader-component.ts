import { Component, OnDestroy, OnInit } from '@angular/core';
import { FileUploaderService } from 'src/app/services/file-uploader-service';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzMessageService } from 'ng-zorro-antd/message';
import { FilesComponent } from '../files/files.component';
import { FileOverwriterService } from 'src/app/services/file-overwriter.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader-component.html',
  styleUrls: ['./file-uploader-component.css'],
  providers: [FileUploaderService,FileOverwriterService, NzModalService, NzMessageService, FilesComponent]
})
export class FileUploaderComponent implements OnInit {
  confirmationAlertVisible = false;
  overwriteAlertVisible = false;
  selectedFiles!: File[];
  dropAreaActive = false;
  
  constructor(private fileUploadService: FileUploaderService,private fileOverwriteService: FileOverwriterService, private modalService: NzModalService, private nzMessageService: NzMessageService, private filesComponent: FilesComponent) { }
  ngOnInit(): void {
  this.selectedFiles=[]  }
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
        (response: string[]) => {
          if (response.includes("File Uploaded Successfully!")) {
            this.nzMessageService.success('File Uploaded Successfully');
          }
            else if(response.includes("file exists")){
          this.showOverwritingAlert(this.selectedFiles);
          this.overwriteAlertVisible = true;
             }
             this.selectedFiles=[];

        },
        (error) => {
          if (error.status === 400) {
            this.nzMessageService.warning('Only STM Files are allowed!');
          }
          }

      );

    } else {
      this.nzMessageService.info('Choose Files!');
    }

  }

  showOverwritingAlert(files: File[]){

      this.modalService.confirm({
        nzTitle: 'Confirmation Overwrite',
        nzContent: 'File already exists, do you want to overwrite it?',
        nzOkText: 'OK',
        nzCancelText: 'Cancel',
        nzOnOk: () => {     
        this.fileOverwriteService.overwriteFile(files).subscribe((response: any)=>{
  

        },
          error=>{

          })
          this.nzMessageService.success('File Overwritten Successfully');

          this.selectedFiles=[];
      },
        nzOnCancel: () => {
          this.nzMessageService.info('Overwriting Canceled');  
        }
      });
  
  }
onCancel(): void {
  if (this.selectedFiles.length > 0) {
    this.showConfirmationAlert();
  }
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

  }
  showConfirmationAlert(): void {
    this.modalService.confirm({
      nzTitle: 'Confirmation Cancel',
      nzContent: 'Are you sure to cancel this upload task?',
      nzOkText: 'OK',
      nzCancelText: 'Cancel',
      nzOnOk: () => {
        this.nzMessageService.info('Uploading Canceled');
        this.selectedFiles = [];
      },
      nzOnCancel:()=>{

      }
    });
  }

}