import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogInfoComponent } from './dialog-info/dialog-info.component';
import { MatDialogModule } from '@angular/material/dialog';


@NgModule({
  declarations: [
    DialogInfoComponent
  ],
  imports: [
    CommonModule,
    MatDialogModule
  ],
  exports: [
    DialogInfoComponent
  ]
})
export class SharedModule { }
