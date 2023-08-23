import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FileUploaderComponent } from './components/file-uploader/file-uploader-component';
import { AppTitleComponent } from './components/app-title/app-title.component';
import {  HttpClientModule } from '@angular/common/http';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table'; 
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { StatisticCardComponent } from './components/statistic-card/statistic-card.component';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzFormModule } from 'ng-zorro-antd/form'; 

import { FilesComponent } from './components/files/files.component';
import { CardModule, TableModule, TabsModule } from '@coreui/angular';
import { RouterModule } from '@angular/router';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { DownloadFormatDialogComponent } from './components/download-format-dialog/download-format-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { NzModalService } from 'ng-zorro-antd/modal';
import { CommunicationService } from './services/communication.service';
import { SelectedFilesStatisticService } from './services/selected-files-statistic.service';
import { NzProgressModule } from 'ng-zorro-antd/progress';
import { HistogramComponent } from './components/histogram/histogram.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { CommonModule } from '@angular/common'; //
import { ContentFileComponent } from './components/content-file/content-file.component';
registerLocaleData(en);

@NgModule({
  declarations: [
 AppComponent,
FileUploaderComponent, AppTitleComponent,
 SideBarComponent,
 StatisticCardComponent,
FilesComponent,
DashboardComponent,
DownloadFormatDialogComponent,
HistogramComponent,
ContentFileComponent

   ],
  imports: [
      HttpClientModule,
      BrowserModule,
      AppRoutingModule,
      NzLayoutModule,
      NzMenuModule,
      NzIconModule,
      NzCardModule,
      NzAvatarModule,
      NzTableModule,
      NzUploadModule,
      NzProgressModule,
      CardModule,
      TableModule,
      TabsModule,
      RouterModule,
      FormsModule,
      BrowserAnimationsModule,
      MatDialogModule,
      NzSelectModule,
      NzFormModule,
      NgxChartsModule,
      CommonModule,
    ],
  providers: [
    NzModalService,
    SelectedFilesStatisticService,
    CommunicationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }