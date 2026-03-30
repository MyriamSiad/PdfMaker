import {FormsModule} from '@angular/forms';
import {NgClass} from '@angular/common';
import {Component} from '@angular/core';
import {TxtToPdfComponent} from '@features/conversion/conversion.txt.component';
import {ImageToPdfComponent} from '@features/conversion/image-conversion';

@Component({
  selector: 'app-to-pdf',
  imports: [NgClass, TxtToPdfComponent, ImageToPdfComponent],
  templateUrl: './conversion.component.html',
})
export class ConversionComponent{
  mode: 'txt' | 'image' = 'txt';

}
