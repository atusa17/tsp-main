package edu.msudenver.tsp.persistence.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity(name = "definitions")
@Table(name = "definitions")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class Definition extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class, message = "A name must be specified")
    @Size(min = 1, max = 200, message = "Must be between 1 and 200 characters")
    private String name;

    @NotNull(groups = Insert.class, message = "At least one (1) definition must be specified")
    @Type(type = "json") @Column(columnDefinition = "jsonb") private List<String> definition;

    @Type(type = "json") @Column(columnDefinition = "jsonb") private List<String> notation;

    private static final long serialVersionUID = -5314619286352932857L;

    public interface Insert {}
}
