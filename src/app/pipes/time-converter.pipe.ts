import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeConverter'
})
export class TimeConverterPipe implements PipeTransform {
  transform(inputDate: Date): string {
    const formattedDate = `${inputDate.getFullYear()}/${inputDate.getMonth() + 1}/${inputDate.getDate()} ${inputDate.getHours()}:${inputDate.getMinutes()}:${inputDate.getSeconds()}:${inputDate.getMilliseconds()}`;
    return formattedDate;
  }
}
