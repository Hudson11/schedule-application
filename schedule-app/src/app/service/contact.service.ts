import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ContactType } from '../types/ContactType';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  requestUrl = environment.apiUrl + '/api/contacts'

  constructor(private httpClient: HttpClient) { }

  listAll (): Observable<any[]> {
    return this.httpClient.get<any[]>(this.requestUrl)
  }

  create (contactType: ContactType): Observable<ContactType> {
    return this.httpClient.post<ContactType>(this.requestUrl, contactType)
  }

  setFavorite (contact: ContactType): Observable<any> {
    return this.httpClient.patch(this.requestUrl + `/${contact.id}/favorite`, { favorite: !contact.favorite })
  }

  upload (contact: ContactType, formData: FormData): Observable<any> {
    return this.httpClient.patch(this.requestUrl + `/${contact.id}/image`, formData, { responseType: 'blob' })
  }
}
