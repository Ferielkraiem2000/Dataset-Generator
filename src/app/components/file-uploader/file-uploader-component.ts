import { Component } from '@angular/core';
import { FileUploaderService } from 'src/app/services/file-uploader-service';

@Component({
  selector: 'app-file-uploader',
  templateUrl: './file-uploader-component.html',
  styleUrls: ['./file-uploader-component.css'],
  providers: [FileUploaderService]
})
export class FileUploaderComponent {

  selectedFiles: File[] = [];
  dropAreaActive = false;
  constructor(private fileUploadService: FileUploaderService) { }

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
      const overwrite = this.fileUploadService.getOverwriteValue();
      this.fileUploadService.uploadFiles(this.selectedFiles).subscribe(
        (response) => {
          console.log('Files uploaded successfully!', response);
        },
        (error) => {
          console.error('Error uploading files', error);
        }
      );
    } else {
      console.log('No files selected.');
    }
  }
  onConfirm(): void {
    console.log('Fichiers sélectionnés :', this.selectedFiles);
    this.onUpload();
    this.selectedFiles = [];

  }

  onCancel(): void {
    this.selectedFiles = [];
  }

  onFilesAdded(event: any): void {
    const files: FileList = event.target.files;
    for (let i = 0; i < files.length; i++) {
      this.selectedFiles.push(files[i]);
    }
  }
}