package io.github.Hudson11.scheduleapi.DTO;

import io.github.Hudson11.scheduleapi.models.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class ContactPageable {

    private PageableSerializer pageable;
    private List<Contact> content;

}
