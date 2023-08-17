import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FilesComponent } from './components/files/files.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AppTitleComponent } from './components/app-title/app-title.component';
import { FileUploaderComponent } from './components/file-uploader/file-uploader-component';

const routes: Routes = [

  { path: 'dashboard', component: DashboardComponent },
  { path: 'upload-files', component: FileUploaderComponent },
  { path: 'files', component: FilesComponent },
  { path: 'about', component: AppTitleComponent},
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'top',
      anchorScrolling: 'enabled',
      initialNavigation: 'enabledNonBlocking'
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}


