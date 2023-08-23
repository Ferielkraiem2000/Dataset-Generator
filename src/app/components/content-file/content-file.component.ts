import { Component, OnInit } from '@angular/core';
import { CommunicationService } from 'src/app/services/communication.service';

@Component({
  selector: 'app-content-file',
  templateUrl: './content-file.component.html',
  styleUrls: ['./content-file.component.css'],
})
export class ContentFileComponent implements OnInit{
  constructor(private communicationService:CommunicationService){}
  content:any[]=[];
  isContent(){
    return this.communicationService.content;
  }
 ngOnInit(): void {
   this.content=this.isContent();
 }
}
