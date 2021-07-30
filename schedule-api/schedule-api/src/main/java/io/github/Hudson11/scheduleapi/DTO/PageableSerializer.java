package io.github.Hudson11.scheduleapi.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Setter @Getter
public class PageableSerializer<T> {

    private Boolean lastPage;
    private Integer totalPage;
    private Long totalElements;
    private Integer size;
    private Integer number;

    public PageableSerializer(Page<T> pageable) {
        this.totalPage = pageable.getTotalPages();
        this.totalElements = pageable.getTotalElements();
        this.size = pageable.getSize();
        this.number = pageable.getNumber();
        this.lastPage = pageable.getNumber() == (pageable.getTotalPages() - 1);
    }

}
