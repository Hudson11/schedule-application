package io.github.Hudson11.scheduleapi.controller;

import io.github.Hudson11.scheduleapi.DTO.ContactFavoriteDTO;
import io.github.Hudson11.scheduleapi.DTO.ContactPageable;
import io.github.Hudson11.scheduleapi.DTO.PageableSerializer;
import io.github.Hudson11.scheduleapi.models.Contact;
import io.github.Hudson11.scheduleapi.repository.ContactRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin("*")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contact create (@RequestBody Contact contact) {
        Optional<Contact> contactByName = this.contactRepository.findByName(contact.getName());
        if(contactByName.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A contact with this name already exists");
        return this.contactRepository.save(contact);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete ( @PathVariable Integer id) {
        this.contactRepository.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ContactPageable listAll (
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer pageSize
    ) {
        Page<Contact> response = this.contactRepository.findAll(PageRequest.of(page, pageSize));
        PageableSerializer<Contact> serializer = new PageableSerializer<Contact>(response);
        List<Contact> content = response.getContent();
        return new ContactPageable(serializer, content);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Contact listById ( @PathVariable Integer id ) {
        return this.contactRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
    }

    @PatchMapping("{id}/favorite")
    public void favorite (@PathVariable Integer id, @RequestBody ContactFavoriteDTO contactFavoriteDTO) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));

        contact.setFavorite(contactFavoriteDTO.getFavorite());
        this.contactRepository.save(contact);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Contact update (@PathVariable Integer id, @RequestBody Contact contactRequest) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
        contactRequest.setId(contact.getId());
        contactRequest.setCreated_at(contact.getCreated_at());
        return this.contactRepository.save(contactRequest);
    }

    @PatchMapping("{id}/image")
    @ResponseStatus(HttpStatus.OK)
    public byte[] addPhoto (@PathVariable Integer id, @RequestParam("image") Part file) {
        Contact contact = this.contactRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Not Found"));
        try {
            InputStream inputStream = file.getInputStream();
            byte[] bytes = new byte[(int) file.getSize()];
            IOUtils.readFully(inputStream, bytes);
            contact.setImage(bytes);
            this.contactRepository.save(contact);
            inputStream.close();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error file upload");
        }

        return contact.getImage();
    }

}
