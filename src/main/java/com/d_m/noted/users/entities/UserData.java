package com.d_m.noted.users.entities;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.shared.entities.BaseDatedEntity;
import com.d_m.noted.users.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_data")
public class UserData extends BaseDatedEntity {
    @NonNull
    @Column(nullable = false)
    String email;

    @NonNull
    @Column(nullable = false)
    String password;

    @NonNull
    @Column(nullable = false)
    String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Notebook> notebooks;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    UserRole role = UserRole.USER;
}
