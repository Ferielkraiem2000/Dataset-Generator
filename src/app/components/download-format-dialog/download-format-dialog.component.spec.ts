import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DownloadFormatDialogComponent } from './download-format-dialog.component';

describe('DownloadFormatDialogComponent', () => {
  let component: DownloadFormatDialogComponent;
  let fixture: ComponentFixture<DownloadFormatDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DownloadFormatDialogComponent]
    });
    fixture = TestBed.createComponent(DownloadFormatDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
