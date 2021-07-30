import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ContactService } from '../service/contact.service';
import { ContactType } from '../types/ContactType';
import { MatSnackBar } from '@angular/material/snack-bar'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { MatDialog } from '@angular/material/dialog';
import { DialogInfoComponent } from '../shared/dialog-info/dialog-info.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';


@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit, AfterViewInit {

  displayColumns = ['image', 'id', 'name', 'email', 'favorite', 'View']
  createSuccess = false
  durationSnackInSeconds = 5
  
  public contacts = new MatTableDataSource<ContactType>()
  public form: FormGroup
  public contactSelected: ContactType

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    private contactService: ContactService,
    private snack: MatSnackBar,
    private formBuilder: FormBuilder,
    private dialog: MatDialog
  ) { }

  ngAfterViewInit(): void {
    this.contacts.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    })
    this.initListContact()
  }

  initListContact (): void {
    this.contactService.listAll().subscribe((response: any) => this.contacts = response.content)
  }

  createContact (name: string, email: string): void {
    const contact: ContactType = { name, email, favorite: false }
    this.contactService.create(contact).subscribe(response => {
      this.showSnack("Success Create Contact", "Close")
      const newContacts = [...this.contacts.data, response]
      this.contacts.data = newContacts
    })
  }

  setContactFavorite (contact: ContactType): void  {
    this.contactService.setFavorite(contact).subscribe(response => {
      !contact.favorite ? this.showSnack("Favorite Contact", "Close")
                : this.showSnack("Not Favorite Contact", "Close")
      contact.favorite = !contact.favorite
    }, error => {
      this.showSnack("Erro Favorite Contact", "Close")
    })
  }

  showSnack (message: string, action: string): void {
    this.snack.open(message, action, {duration: this.durationSnackInSeconds * 1000})
  }

  onSubmit (): void {
    this.createContact(
      this.form.value.name,
      this.form.value.email
    )
  }

  uploadImage (event: any, contact: ContactType): void {
    const file = event.target.files;
    if(file) {
      const image = file[0]
      const formData = new FormData()
      formData.append("image", image)
      this.contactService.upload(contact, formData).subscribe(response => {
        this.initListContact();
      })
    }
  }

  viewContact (contact: ContactType): void {
    this.dialog.open(DialogInfoComponent, {
      width: '500px',
      data: contact
    })
  }
}
