import { Component } from '@angular/core';
import { CommunicationService } from 'src/app/services/communication.service';

@Component({
  selector: 'app-content-file',
  templateUrl: './content-file.component.html',
  styleUrls: ['./content-file.component.css'],
  providers:[CommunicationService]
})
export class ContentFileComponent {
  constructor(private communicationService:CommunicationService){}
}
