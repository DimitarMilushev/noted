package com.d_m.noted.shared.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

/**
 * An entity that tracks dates of creation, update and soft delete
 */
@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class BaseDatedEntity extends BaseEntity {
    @Column
    ZonedDateTime createdAt;

    @Column
    ZonedDateTime updatedAt;

    @Column
    ZonedDateTime deletedAt;

    @PrePersist
    protected void onPrePersist() {
        this.setCreatedAt(ZonedDateTime.now());
        this.setUpdatedAt(ZonedDateTime.now());
    }

    @PreUpdate
    protected void onPostUpdate() {
        this.setUpdatedAt(ZonedDateTime.now());
    }

    @PreRemove
    protected void onPostRemove() {
        this.setDeletedAt(ZonedDateTime.now());
    }
}
